package com.funds.transfer.controller;

import com.funds.transfer.entity.Account;
import com.funds.transfer.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funds/v1")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return null;
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        return null;
    }

    @DeleteMapping
    public void deleteAccount(){

    }
    @PutMapping
    public void createTransfer(){

    }
    @GetMapping
    public void checkBalance(){

    }


}
