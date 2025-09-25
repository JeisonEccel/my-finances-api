package com.jeisoneccel.my_finances.classes.accounts;

import com.jeisoneccel.my_finances.auth.LoggedUser;
import com.jeisoneccel.my_finances.classes.users.User;
import com.jeisoneccel.my_finances.core.services.BasicService;
import com.jeisoneccel.my_finances.exceptions.custom.RecordAlreadyExistsException;
import com.jeisoneccel.my_finances.exceptions.custom.RecordNotFoundException;
import com.jeisoneccel.my_finances.utils.ServiceUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService implements BasicService<Account, AccountModel> {

    private static final String TYPE = Account.class.getSimpleName();

    private final AccountRepository repository;
    private final ServiceUtils serviceUtils;
    private final LoggedUser loggedUser;

    public List<Account> getAll() {
        log.info(TYPE + ": Fetching all");
        return repository.findByOwner(loggedUser.getUser());
    }

    @Override
    public Account getById(@NonNull String id) {
        log.info(TYPE + ": Fetching by id ({})", id);
        return repository.findByIdAndOwner(id, loggedUser.getUser())
                .orElseThrow(() -> new RecordNotFoundException(TYPE));
    }

    @Override
    public Account create(@NonNull AccountModel model) {
        log.info(TYPE + ": Creating new with model ({})", model);
        Account entity = serviceUtils.mapModelToEntity(model, new Account());
        entity.setOwner(loggedUser.getUser());
        validate(entity);
        return repository.save(entity);
    }

    @Override
    public Account update(@NonNull String id, @NonNull HashMap<String, Object> updates) {
        log.info(TYPE + ": Updating id ({})", id);
        Account entity = serviceUtils.mapHashToEntity(updates, getById(id));
        validate(entity);
        return repository.save(entity);
    }

    @Override
    public void delete(@NonNull String id) {
        log.info(TYPE + ": Deleting id ({})", id);
        Account account = repository.findByIdAndOwner(id, loggedUser.getUser()).orElse(null);
        if (account == null) return;

        repository.delete(account);
    }

    @Override
    public void validate(@NonNull Account entity) {
        entity.validateSchema();
        validateNameIsAvailable(entity.getId(), entity.getName(), entity.getOwner());
    }

    public void validateNameIsAvailable(String id, String name, User owner) {
        log.info("Validating name is available: {}", name);
        String ignoreId = id != null ? id : "";
        if (!repository.findByIdNotAndNameIgnoreCaseAndOwner(ignoreId, name, owner).isEmpty()) {
            throw new RecordAlreadyExistsException(TYPE, "name");
        }
    }

}
