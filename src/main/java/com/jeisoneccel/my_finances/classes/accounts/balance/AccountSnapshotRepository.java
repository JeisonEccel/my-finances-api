package com.jeisoneccel.my_finances.classes.accounts.balance;


import com.jeisoneccel.my_finances.classes.accounts.Account;
import com.jeisoneccel.my_finances.core.repositories.BasicRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountSnapshotRepository extends BasicRepository<AccountSnapshot> {

    Optional<AccountSnapshot> findByAccountAndYearMonth(Account account, int yearMonth);

    Optional<AccountSnapshot> findFirstByAccountAndYearMonthLessThanOrderByYearMonthDesc(
            Account account, int yearMonth
    );

}
