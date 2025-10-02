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

    Optional<Transaction> findFirstByOwnerAndAccountAndDateLessThanEqualOrderByDateDescIndexDesc(
            User owner, Account account, LocalDate date
    );

    Optional<Transaction> findFirstByOwnerAndAccountAndDateLessThanOrderByDateDescIndexDesc(
            User user, Account account, LocalDate date
    );

    Optional<Transaction> findFirstByOwnerAndAccountAndDateAndIndexLessThanOrderByIndexDesc(
            User owner, Account account, LocalDate date, int index
    );

    List<Transaction> findByOwnerAndAccountAndDateGreaterThanEqualAndIdNotOrderByDateAscIndexAsc(
            User owner, Account account, LocalDate date, String id
    );

    List<Transaction> findByOwnerAndAccountAndDateBetweenOrderByDateAscIndexAsc(
            User owner, Account account, LocalDate start, LocalDate end
    );

    List<Transaction> findByOwnerAndAccountOrderByDateAscIndexAsc(User owner, Account account);

}
