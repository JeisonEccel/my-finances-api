package com.jeisoneccel.my_finances.classes.transactions;

import com.jeisoneccel.my_finances.auth.LoggedUser;
import com.jeisoneccel.my_finances.classes.accounts.Account;
import com.jeisoneccel.my_finances.classes.accounts.AccountService;
import com.jeisoneccel.my_finances.classes.categories.CategoryService;
import com.jeisoneccel.my_finances.classes.users.User;
import com.jeisoneccel.my_finances.core.services.BasicService;
import com.jeisoneccel.my_finances.exceptions.custom.RecordNotFoundException;
import com.jeisoneccel.my_finances.utils.ServiceUtils;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.jeisoneccel.my_finances.exceptions.ErrorCode.ERR0200001;
import static com.jeisoneccel.my_finances.exceptions.ErrorCode.ERR0200002;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService implements BasicService<Transaction, TransactionModel> {

    private static final String TYPE = Transaction.class.getSimpleName();

    private final TransactionRepository repository;
    private final ServiceUtils serviceUtils;
    private final LoggedUser loggedUser;
    private final AccountService accountService;
    private final CategoryService categoryService;

    public List<Transaction> getAll() {
        log.info(TYPE + ": Fetching all");
        return repository.findByOwnerOrderByDateDescIndexDesc(loggedUser.getUser());
    }

    @Override
    public Transaction getById(@NonNull String id) {
        log.info(TYPE + ": Fetching by id ({})", id);
        return repository.findByIdAndOwner(id, loggedUser.getUser())
                .orElseThrow(() -> new RecordNotFoundException(TYPE));
    }

    @Override
    @Transactional
    public Transaction create(@NonNull TransactionModel model) {
        log.info(TYPE + ": Creating new with model ({})", model);
        Transaction entity = serviceUtils.mapModelToEntity(model, new Transaction());
        entity.setOwner(loggedUser.getUser());
        updateIndexAndBalance(entity);
        validate(entity);
        Transaction savedTransaction = repository.save(entity);
        updateFutureTransactions(savedTransaction);
        return savedTransaction;
    }

    @Override
    @Transactional
    public Transaction update(@NonNull String id, @NonNull HashMap<String, Object> updates) {
        log.info(TYPE + ": Updating id ({})", id);
        Transaction initialTransaction = getById(id);

        User owner = initialTransaction.getOwner();
        LocalDate initialDate = initialTransaction.getDate();
        int initialIndex = initialTransaction.getIndex();
        Account initialAccount = initialTransaction.getAccount();

        Transaction updatedTransaction = serviceUtils.mapHashToEntity(updates, initialTransaction);
        if (!initialDate.equals(updatedTransaction.getDate())) {
            updatedTransaction.setIndex(Integer.MAX_VALUE);
        }
        updateIndexAndBalance(updatedTransaction);
        validate(updatedTransaction);
        Transaction savedTransaction = repository.save(updatedTransaction);

        if (!initialAccount.equals(savedTransaction.getAccount())) {
            log.info("Updated account from {} to {}", initialAccount.getName(), savedTransaction.getAccount().getName());
            updateFutureTransactions(savedTransaction); // New Account Transactions
            updateFutureTransactions(owner, initialAccount, initialDate, initialIndex); // Old Account Transactions
        } else if (initialDate.isBefore(savedTransaction.getDate())) {
            log.info("Moved transaction from {} to {}", initialDate, savedTransaction.getDate());
            updateFutureTransactions(owner, initialAccount, initialDate, initialIndex); // Update from old date
        } else if (initialDate.equals(savedTransaction.getDate()) && initialIndex < savedTransaction.getIndex()) {
            log.info("Moved position from {} to {}", initialIndex, savedTransaction.getIndex());
            updateFutureTransactions(owner, initialAccount, initialDate, initialIndex); // Update from old position
        } else {
            log.info("Updating transaction balances");
            updateFutureTransactions(savedTransaction); // Update from new position
        }

        return savedTransaction;
    }

    @Override
    @Transactional
    public void delete(@NonNull String id) {
        log.info(TYPE + ": Deleting id ({})", id);
        Transaction transaction = repository.findByIdAndOwner(id, loggedUser.getUser()).orElse(null);
        if (transaction == null) return;

        User owner = transaction.getOwner();
        Account account = transaction.getAccount();
        LocalDate date = transaction.getDate();
        int index = transaction.getIndex();

        repository.delete(transaction);

        updateFutureTransactions(owner, account, date, index);
    }

    @Override
    public void validate(@NonNull Transaction entity) {
        entity.validateSchema();
        validateAccountIsValid(entity.getAccount().getId());
        validateCategoryIsValid(entity.getCategory().getId());
    }

    public void updateIndexAndBalance(Transaction transaction) {
        Transaction lastTransaction = getLastTransaction(
                transaction.getOwner(), transaction.getAccount(), transaction.getDate(), transaction.getIndex()
        );

        if (lastTransaction != null) {
            transaction.setIndex(calculateIndex(transaction, lastTransaction.getDate(), lastTransaction.getIndex()));
            transaction.setBalance(calculateBalance(transaction, lastTransaction.getBalance()));
        } else {
            transaction.setIndex(1);
            transaction.setBalance(transaction.getAmount());
        }
    }

    public Transaction getLastTransaction(User owner, Account account, LocalDate date, int index) {
        return switch (index) {
            case 0 -> repository.findFirstByOwnerAndAccountAndDateLessThanEqualOrderByDateDescIndexDesc(
                    owner, account, date
            ).orElse(null);
            case 1 -> repository.findFirstByOwnerAndAccountAndDateLessThanOrderByDateDescIndexDesc(
                    owner, account, date
            ).orElse(null);
            default -> repository.findFirstByOwnerAndAccountAndDateAndIndexLessThanOrderByIndexDesc(
                    owner, account, date, index
            ).orElse(null);
        };
    }

    public void updateFutureTransactions(User owner, Account account, LocalDate date, int index) {
        Transaction lastTransaction = getLastTransaction(owner, account, date, index);

        if (lastTransaction != null) {
            updateFutureTransactions(lastTransaction);
        } else {
            updateAllTransactions(owner, account);
        }
    }

    public void updateFutureTransactions(Transaction transaction) {
        List<Transaction> futureTransactions = repository.findByOwnerAndAccountAndDateGreaterThanEqualAndIdNotOrderByDateAscIndexAsc(
                transaction.getOwner(), transaction.getAccount(), transaction.getDate(), transaction.getId()
        );

        List<Transaction> updatedTransactions = new ArrayList<>();

        Transaction lt = transaction;
        for (Transaction t : futureTransactions) {
            if (t.getDate().equals(lt.getDate()) && t.getIndex() < lt.getIndex()) {
                continue;
            }

            t.setIndex(calculateIndex(t, lt.getDate(), lt.getIndex()));
            t.setBalance(calculateBalance(t, lt.getBalance()));
            updatedTransactions.add(t);

            lt = t;
        }

        repository.saveAll(updatedTransactions);
    }

    public void updateAllTransactions(User owner, Account account) {
        List<Transaction> futureTransactions = repository.findByOwnerAndAccountOrderByDateAscIndexAsc(owner, account);

        List<Transaction> updatedTransactions = new ArrayList<>();

        LocalDate lastDate = null;
        int lastIndex = 0;
        BigDecimal lastBalance = BigDecimal.ZERO;
        for (Transaction t : futureTransactions) {
            if (lastDate == null) {
                t.setIndex(1);
                t.setBalance(t.getAmount());
            } else {
                t.setIndex(calculateIndex(t, lastDate, lastIndex));
                t.setBalance(calculateBalance(t, lastBalance));
            }

            updatedTransactions.add(t);

            lastDate = t.getDate();
            lastIndex = t.getIndex();
            lastBalance = t.getBalance();
        }

        repository.saveAll(updatedTransactions);
    }

    private int calculateIndex(Transaction transaction, LocalDate lastDate, int lastIndex) {
        if (lastDate == null || !lastDate.equals(transaction.getDate())) {
            return 1;
        }

        return lastIndex + 1;
    }

    private BigDecimal calculateBalance(Transaction transaction, BigDecimal lastBalance) {
        if (lastBalance == null) {
            return transaction.getAmount();
        }

        return lastBalance.add(transaction.getAmount());
    }

    public List<Transaction> getTransactionsBetween(Account account, LocalDate start, LocalDate end) {
        return repository.findByOwnerAndAccountAndDateBetweenOrderByDateAscIndexAsc(
                loggedUser.getUser(), account, start, end
        );
    }

    public void validateAccountIsValid(String accountId) {
        log.info("Validating account belongs to user: {}", accountId);
        try {
            accountService.getById(accountId);
        } catch (RecordNotFoundException e) {
            throw new IllegalArgumentException(ERR0200001.name());
        }
    }

    public void validateCategoryIsValid(String categoryId) {
        log.info("Validating category belongs to user: {}", categoryId);
        try {
            categoryService.getById(categoryId);
        } catch (RecordNotFoundException e) {
            throw new IllegalArgumentException(ERR0200002.name());
        }
    }

}
