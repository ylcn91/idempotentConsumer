package com.doksanbir.balancemanagement.repository;

import com.doksanbir.balancemanagement.model.Account;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository extends ReactiveCrudRepository<Account, String> {

    @Query("INSERT INTO accounts (id, balance) VALUES (:id, :balance) ON CONFLICT (id) DO UPDATE SET balance = accounts.balance + :balance")
    Mono<Account> upsertBalance(String id, double balance);
}


