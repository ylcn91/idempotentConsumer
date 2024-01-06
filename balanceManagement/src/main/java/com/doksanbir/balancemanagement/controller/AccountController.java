package com.doksanbir.balancemanagement.controller;

import com.doksanbir.balancemanagement.service.AccountService;
import com.doksanbir.balancemanagement.model.BalanceUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @PutMapping("/{accountId}/balance")
    public Mono<ResponseEntity<Void>> updateBalance(@PathVariable String accountId, @RequestBody BalanceUpdateRequest request) {
        log.info("Received request to update balance for account ID: {} with amount: {}", accountId, request.getAmount());

        return accountService.updateBalance(accountId, request.getAmount())
                .log()
                .map(updatedAccount -> {
                    log.info("Balance updated successfully for account ID: {}", accountId);
                    return ResponseEntity.ok().<Void>build();
                })
                .onErrorResume(e -> {
                    log.error("Error occurred while updating balance for account ID: {}", accountId, e);
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
    }
}
