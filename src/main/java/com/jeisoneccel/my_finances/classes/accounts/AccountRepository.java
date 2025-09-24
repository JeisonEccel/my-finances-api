package com.jeisoneccel.my_finances.classes.accounts;

import com.jeisoneccel.my_finances.classes.users.User;
import com.jeisoneccel.my_finances.core.repositories.BasicRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends BasicRepository<Account> {

    List<Account> findByOwner(User owner);

    Optional<Account> findByIdAndOwner(String id, User owner);

    List<Account> findByIdNotAndNameIgnoreCaseAndOwner(String ignoreId, String name, User owner);

}
