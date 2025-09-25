package com.jeisoneccel.my_finances.classes.transactions;

import com.jeisoneccel.my_finances.classes.users.User;
import com.jeisoneccel.my_finances.core.repositories.BasicRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends BasicRepository<Transaction> {

    List<Transaction> findByOwner(User owner);

    Optional<Transaction> findByIdAndOwner(String id, User owner);

}
