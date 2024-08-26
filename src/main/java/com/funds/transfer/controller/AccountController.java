package com.funds.transfer.controller;

import com.funds.transfer.model.AccountDto;
import com.funds.transfer.service.AccountService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/funds/v1")
public class AccountController {
    private static final Logger logger =  LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/accounts")
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody AccountDto accountDto) {
        logger.info("calling createAccount method with "+accountDto);
        AccountDto savedAccount = accountService.createAccount(accountDto);
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        logger.info("Fetching list of all the accounts");
        List<AccountDto> accounts = accountService.getAllAccounts();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }


    @GetMapping("/accounts/balance/{accountId}")
    public ResponseEntity<Map<String,String>> checkBalance(@PathVariable Integer accountId) {
        logger.info("calling checkBalance method with "+accountId);
        Map<String,String> accountDetails=accountService.checkBalance(accountId);
        return new ResponseEntity<>(accountDetails, HttpStatus.OK);

    }

}
