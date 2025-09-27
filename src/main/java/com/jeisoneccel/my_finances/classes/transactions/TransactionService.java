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

import java.time.LocalDate;
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
        entity.setIndex(getLastIndex(entity));
        validate(entity);
        return repository.save(entity);
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

        if (!initialAccount.equals(updatedTransaction.getAccount())) {
            log.info("Updated account from {} to {}", initialAccount.getName(), updatedTransaction.getAccount().getName());
            updateDateIndexes(owner, initialAccount, initialDate);
            updatedTransaction.setIndex(getLastIndex(updatedTransaction));
        } else if (!initialDate.equals(updatedTransaction.getDate())) {
            log.info("Changed date from {} to {}", initialDate, updatedTransaction.getDate());
            updateDateIndexes(owner, initialAccount, initialDate);
            updatedTransaction.setIndex(getLastIndex(updatedTransaction));
        } else if (initialIndex != updatedTransaction.getIndex()) {
            log.info("Changed index from {} to {}", initialIndex, updatedTransaction.getIndex());
            int adjustedIndex = updateDateIndexes(updatedTransaction);
            updatedTransaction.setIndex(adjustedIndex);
        }

        validate(updatedTransaction);

        return repository.save(updatedTransaction);
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

        repository.delete(transaction);

        updateDateIndexes(owner, account, date);
    }

    @Override
    public void validate(@NonNull Transaction entity) {
        entity.validateSchema();
        validateAccountIsValid(entity.getAccount().getId());
        validateCategoryIsValid(entity.getCategory().getId());
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

    private int getLastIndex(Transaction transaction) {
        Transaction lastTransaction = repository.findFirstByOwnerAndAccountAndDateAndIdNotOrderByIndexDesc(
                transaction.getOwner(), transaction.getAccount(), transaction.getDate(), transaction.getId()
        ).orElse(null);

        return lastTransaction != null ? lastTransaction.getIndex() + 1 : 1;
    }

    private void updateDateIndexes(User owner, Account account, LocalDate date) {
        List<Transaction> transactions = repository.findByOwnerAndAccountAndDateOrderByIndexAsc(
                owner, account, date
        );

        int i = 1;
        for (Transaction t : transactions) {
            t.setIndex(i);
            i++;
        }

        repository.saveAll(transactions);
    }

    private int updateDateIndexes(Transaction transaction) {
        List<Transaction> transactions = repository.findByOwnerAndAccountAndDateAndIdNotOrderByIndexAsc(
                transaction.getOwner(), transaction.getAccount(), transaction.getDate(), transaction.getId()
        );

        int referenceIndex = Math.min(transaction.getIndex(), transactions.size() + 1);
        int i = 1;
        for (Transaction t : transactions) {
            if (i == referenceIndex) {
                i++;
            }
            t.setIndex(i);
            i++;
        }

        repository.saveAll(transactions);

        return referenceIndex;
    }

}
