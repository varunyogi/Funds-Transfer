package com.funds.transfer.service;

import com.funds.transfer.entity.Account;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {

    public ResponseEntity<Account> createAccount();

    public ResponseEntity<List<Account>> getAllAccounts();

    public void deleteAccount();

    public void createTransfer();

    public void checkBalance();
}
