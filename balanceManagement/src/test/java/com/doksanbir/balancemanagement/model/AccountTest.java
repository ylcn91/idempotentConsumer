package com.doksanbir.balancemanagement.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    public void test_account_creation() {
        // Given
        String accountId = "1234";
        double balance = 1000.0;

        // When
        Account account = new Account(accountId, balance);

        // Then
        assertEquals(accountId, account.getId());
        assertEquals(balance, account.getBalance(), 0.0);
    }

    @Test
    public void test_positive_balance_update() {
        // Given
        String accountId = "1234";
        double initialBalance = 1000.0;
        double amount = 100.0;
        Account account = new Account(accountId, initialBalance);

        // When
        account.setBalance(account.getBalance() + amount);

        // Then
        assertEquals(initialBalance + amount, account.getBalance(), 0.0);
    }

}