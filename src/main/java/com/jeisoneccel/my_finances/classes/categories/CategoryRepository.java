package com.jeisoneccel.my_finances.classes.categories;

import com.jeisoneccel.my_finances.classes.users.User;
import com.jeisoneccel.my_finances.core.repositories.BasicRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends BasicRepository<Category> {

    List<Category> findByOwner(User owner);

    Optional<Category> findByIdAndOwner(String id, User owner);

    List<Category> findByIdNotAndNameIgnoreCaseAndOwner(String ignoreId, String name, User owner);

}
