package com.funds.transfer.service;

import com.funds.transfer.entity.Account;
import com.funds.transfer.model.AccountDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {

    public AccountDto createAccount(AccountDto account);

    public List<AccountDto> getAllAccounts();

    public void deleteAccount();

    public void createTransfer();

    public void checkBalance();
}
