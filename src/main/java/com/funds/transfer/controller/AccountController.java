package com.funds.transfer.controller;

import com.funds.transfer.model.AccountDto;
import com.funds.transfer.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/funds/v1")
public class AccountController {

    @Autowired
    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/accounts")
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto) {

        AccountDto savedAccount = accountService.createAccount(accountDto);
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        List<AccountDto> accounts = accountService.getAllAccounts();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @DeleteMapping("/accounts")
    public void deleteAccount() {

    }

    @PutMapping("/accounts")
    public void createTransfer() {

    }

    @GetMapping("/accounts/balance/{accountId}")
    public ResponseEntity<Map<String,String>> checkBalance(@PathVariable Integer accountId) {
        Map<String,String> accountDetails=accountService.checkBalance(accountId);
        return new ResponseEntity<>(accountDetails, HttpStatus.OK);

    }

}
