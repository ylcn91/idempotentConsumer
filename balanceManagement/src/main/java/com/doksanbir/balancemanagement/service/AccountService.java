package com.doksanbir.balancemanagement.service;

import com.doksanbir.balancemanagement.model.Account;
import com.doksanbir.balancemanagement.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Mono<Account> updateBalance(String accountId, double amount) {
        log.info("Updating balance for account ID: {}", accountId);
        if (accountId == null || accountId.isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
        }
        return accountRepository.upsertBalance(accountId, amount)
                .log()
                .switchIfEmpty(Mono.error(new RuntimeException("Failed to update balance for account ID: " + accountId)))
                .doOnSuccess(account -> log.info("Processed balance successfully for account ID: {}", accountId))
                .doOnError(error -> log.error("Failed to process balance for account ID: {}", accountId, error));
    }

    /*
    public Mono<Account> updateBalance(String accountId, double amount) {
        log.info("Updating balance for account ID: {}", accountId);
        return accountRepository.findById(accountId)
                .log()
                .switchIfEmpty(Mono.defer(() -> {
                    // Create new account if not exists
                    log.info("Creating new account with ID: {}", accountId);
                    Account newAccount = new Account(accountId, amount);
                    return accountRepository.save(newAccount);
                }))
                .flatMap(account -> {
                    // Update balance if account exists
                    account.setBalance(account.getBalance() + amount);
                    log.info("Updated balance for account ID {} is {}", accountId, account.getBalance());
                    return accountRepository.save(account);
                })

                .doOnSuccess(account -> log.info("Processed balance successfully for account ID: {}", accountId))
                .doOnError(error -> log.error("Failed to process balance for account ID: {}", accountId, error));
    }
     */
}
