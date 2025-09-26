package com.jeisoneccel.my_finances.classes.transactions;

import com.jeisoneccel.my_finances.classes.accounts.Account;
import com.jeisoneccel.my_finances.classes.users.User;
import com.jeisoneccel.my_finances.core.repositories.BasicRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends BasicRepository<Transaction> {

    List<Transaction> findByOwnerOrderByDateDescIndexDesc(User owner);

    Optional<Transaction> findByIdAndOwner(String id, User owner);

    Optional<Transaction> findFirstByOwnerAndAccountAndDateAndIdNotOrderByIndexDesc(
            User owner, Account account, LocalDate date, String id
    );

    List<Transaction> findByOwnerAndAccountAndDateOrderByIndexAsc(
            User owner, Account account, LocalDate date
    );

    List<Transaction> findByOwnerAndAccountAndDateAndIdNotOrderByIndexAsc(
            User owner, Account account, LocalDate date, String id
    );

}
