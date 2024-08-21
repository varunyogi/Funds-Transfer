package com.funds.transfer.service;

import com.funds.transfer.model.AccountDto;

import java.util.List;
import java.util.Map;

public interface AccountService {

    public AccountDto createAccount(AccountDto account);

    public List<AccountDto> getAllAccounts();

    public void deleteAccount();

    public void createTransfer();

    public Map<String, String> checkBalance(int accountId);

    public AccountDto findAccountById(int accountId);

    public boolean isValidAccount(int accountId);
}
