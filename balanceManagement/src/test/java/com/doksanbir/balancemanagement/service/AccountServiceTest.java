package com.doksanbir.balancemanagement.service;

import com.doksanbir.balancemanagement.model.Account;
import com.doksanbir.balancemanagement.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void updateBalance_success() {
        String accountId = "1234";
        double amount = 100.0;
        Account account = new Account(accountId, 1000.0); // Initial balance is 1000.0

        when(accountRepository.upsertBalance(accountId, amount)).thenReturn(Mono.just(account));

        StepVerifier.create(accountService.updateBalance(accountId, amount))
                .expectNext(account)
                .verifyComplete();
    }

    @Test
    void updateBalance_emptyAccount() {
        String accountId = "4567";
        double amount = 50.0;

        when(accountRepository.upsertBalance(anyString(), anyDouble())).thenReturn(Mono.empty());

        StepVerifier.create(accountService.updateBalance(accountId, amount))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void updateBalance_invalidAccountId() {
        double amount = 25.0;

        // Expect the exception to be thrown directly
        assertThatThrownBy(() -> accountService.updateBalance(null, amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Account ID cannot be null or empty");
    }

}
