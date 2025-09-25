package com.jeisoneccel.my_finances.classes.categories;

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
public class CategoryService implements BasicService<Category, CategoryModel> {

    private static final String TYPE = Category.class.getSimpleName();

    private final CategoryRepository repository;
    private final ServiceUtils serviceUtils;
    private final LoggedUser loggedUser;

    public List<Category> getAll() {
        log.info(TYPE + ": Fetching all");
        return repository.findByOwner(loggedUser.getUser());
    }

    @Override
    public Category getById(@NonNull String id) {
        log.info(TYPE + ": Fetching by id ({})", id);
        return repository.findByIdAndOwner(id, loggedUser.getUser())
                .orElseThrow(() -> new RecordNotFoundException(TYPE));
    }

    @Override
    public Category create(@NonNull CategoryModel model) {
        log.info(TYPE + ": Creating new with model ({})", model);
        Category entity = serviceUtils.mapModelToEntity(model, new Category());
        entity.setOwner(loggedUser.getUser());
        validate(entity);
        return repository.save(entity);
    }

    @Override
    public Category update(@NonNull String id, @NonNull HashMap<String, Object> updates) {
        log.info(TYPE + ": Updating id ({})", id);
        Category entity = serviceUtils.mapHashToEntity(updates, getById(id));
        validate(entity);
        return repository.save(entity);
    }

    @Override
    public void delete(@NonNull String id) {
        log.info(TYPE + ": Deleting id ({})", id);
        Category Category = repository.findByIdAndOwner(id, loggedUser.getUser()).orElse(null);
        if (Category == null) return;

        repository.delete(Category);
    }

    @Override
    public void validate(@NonNull Category entity) {
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
