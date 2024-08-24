package com.funds.transfer.service;

import com.funds.transfer.entity.Account;
import com.funds.transfer.model.AccountDto;

import java.util.List;
import java.util.Map;

public interface AccountService {

    public AccountDto createAccount(AccountDto account);

    public List<AccountDto> getAllAccounts();

    public void deleteAccount();

    public Map<String, String> checkBalance(int accountId);

    public AccountDto findAccountById(int accountId);

    public boolean isValidAccount(int accountId);

    public AccountDto updateAccount(AccountDto accountDto);

    public List<AccountDto> getAccountsByIds(List<Integer> accountIds);
}
