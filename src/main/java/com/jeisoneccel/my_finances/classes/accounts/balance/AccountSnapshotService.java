package com.jeisoneccel.my_finances.classes.accounts.balance;

import com.jeisoneccel.my_finances.classes.accounts.Account;
import com.jeisoneccel.my_finances.classes.transactions.Transaction;
import com.jeisoneccel.my_finances.classes.transactions.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountSnapshotService {

    private final AccountSnapshotRepository repository;
    private final TransactionService transactionService;

    public void upsertBalance(Account account, LocalDate referenceDate) {
        int yearMonth = getYearMonth(referenceDate);
        AccountSnapshot snapshot = repository.findByAccountAndYearMonth(account, yearMonth).orElse(null);

        if (snapshot == null) {
            log.info("Creating snapshot for {} at {}", account.getName(), yearMonth);
            createSnapshot(account, yearMonth);
        } else {
            log.info("Updating snapshot for {} at {}", account.getName(), yearMonth);
            updateSnapshot(snapshot);
        }
    }

    public void createSnapshot(Account account, int yearMonth) {
        AccountSnapshot snapshot = new AccountSnapshot();
        snapshot.setAccount(account);
        snapshot.setYearMonth(yearMonth);

        snapshot.setBalance(calculateBalance(snapshot));
        repository.save(snapshot);
    }

    public void updateSnapshot(AccountSnapshot snapshot) {
        snapshot.setBalance(calculateBalance(snapshot));
        repository.save(snapshot);
    }

    private BigDecimal calculateBalance(AccountSnapshot snapshot) {
        BigDecimal initialBalance = repository.findFirstByAccountAndYearMonthLessThanOrderByYearMonthDesc(
                        snapshot.getAccount(), snapshot.getYearMonth()
                )
                .map(AccountSnapshot::getBalance)
                .orElse(BigDecimal.ZERO);

        YearMonth yearMonth = snapshot.convertYearMonth();
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();
        return transactionService.getTransactionsBetween(snapshot.getAccount(), start, end)
                .stream()
                .map(Transaction::getAmount)
                .reduce(initialBalance, BigDecimal::add);
    }

    private int getYearMonth(LocalDate date) {
        int year = date.getYear();
        int month = date.getMonthValue();
        return year * 100 + month;
    }
}
