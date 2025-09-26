package com.jeisoneccel.my_finances.classes.transactions;

import com.jeisoneccel.my_finances.auth.LoggedUser;
import com.jeisoneccel.my_finances.classes.accounts.AccountService;
import com.jeisoneccel.my_finances.classes.categories.CategoryService;
import com.jeisoneccel.my_finances.core.services.BasicService;
import com.jeisoneccel.my_finances.exceptions.custom.RecordNotFoundException;
import com.jeisoneccel.my_finances.utils.ServiceUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        return repository.findByOwner(loggedUser.getUser());
    }

    @Override
    public Transaction getById(@NonNull String id) {
        log.info(TYPE + ": Fetching by id ({})", id);
        return repository.findByIdAndOwner(id, loggedUser.getUser())
                .orElseThrow(() -> new RecordNotFoundException(TYPE));
    }

    @Override
    public Transaction create(@NonNull TransactionModel model) {
        log.info(TYPE + ": Creating new with model ({})", model);
        Transaction entity = serviceUtils.mapModelToEntity(model, new Transaction());
        entity.setOwner(loggedUser.getUser());
        validate(entity);
        return repository.save(entity);
    }

    @Override
    public Transaction update(@NonNull String id, @NonNull HashMap<String, Object> updates) {
        log.info(TYPE + ": Updating id ({})", id);
        Transaction entity = serviceUtils.mapHashToEntity(updates, getById(id));
        validate(entity);
        return repository.save(entity);
    }

    @Override
    public void delete(@NonNull String id) {
        log.info(TYPE + ": Deleting id ({})", id);
        Transaction Transaction = repository.findByIdAndOwner(id, loggedUser.getUser()).orElse(null);
        if (Transaction == null) return;

        repository.delete(Transaction);
    }

    @Override
    public void validate(@NonNull Transaction entity) {
        entity.validateSchema();
        validateAccountIsValid(entity.getAccount().getId());
        validateCategoryIsValid(entity.getCategory().getId());
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
