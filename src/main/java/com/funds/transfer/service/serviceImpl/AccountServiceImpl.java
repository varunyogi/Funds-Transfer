package com.funds.transfer.service.serviceImpl;

import com.funds.transfer.entity.Account;
import com.funds.transfer.exception.AccountNotFoundException;
import com.funds.transfer.exception.AccountTypeNotSupportedException;
import com.funds.transfer.mapper.AccountMapper;
import com.funds.transfer.model.AccountDto;
import com.funds.transfer.model.AccountType;
import com.funds.transfer.repository.AccountRepository;
import com.funds.transfer.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public AccountDto createAccount(AccountDto accountDto) {
        Account savedAccount = null;
        try {
            if (accountDto.getAccountType().equals(AccountType.CREDIT) || accountDto.getAccountType().equals(AccountType.DEBIT)) {
                savedAccount = accountRepository.save(AccountMapper.mapToAccount(accountDto));

            }
        } catch (IllegalArgumentException e) {
            throw new AccountTypeNotSupportedException("Our Application only supports account type as CREDIT or DEBIT");
        }
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    @Transactional
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(AccountMapper::mapToAccountDto).toList();

    }

    public boolean isValidAccount(int accountId) {
        List<Integer> accounts = getAllAccounts().stream().map(AccountDto::getUserID).toList();
        return accounts.contains(accountId);
    }

    @Override
    public void deleteAccount() {

    }


    @Override
    public Map<String, String> checkBalance(int accountId) {
        AccountDto accountById = findAccountById(accountId);
        Map<String, String> userDetails = new HashMap<>();
        userDetails.put("balance", String.valueOf(accountById.getBalance()));
        userDetails.put("username", accountById.getUserName());
        userDetails.put("currency", accountById.getCurrency());
        return userDetails;

    }

    @Transactional
    public AccountDto findAccountById(int accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException("Account ID does not exist"));
        return AccountMapper.mapToAccountDto(account);
    }

    public AccountDto updateAccount(AccountDto accountDto) {
        Account savedAccount = accountRepository.save(AccountMapper.mapToAccount(accountDto));
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Transactional
    public List<AccountDto> getAccountsByIds(List<Integer> accountIds) {
        return accountRepository.findByIdIn(accountIds).stream().map(AccountMapper::mapToAccountDto).toList();

    }
}
