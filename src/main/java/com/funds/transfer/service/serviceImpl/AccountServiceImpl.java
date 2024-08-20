package com.funds.transfer.service.serviceImpl;

import com.funds.transfer.entity.Account;
import com.funds.transfer.exception.AccountNotFoundException;
import com.funds.transfer.mapper.AccountMapper;
import com.funds.transfer.model.AccountDto;
import com.funds.transfer.repository.AccountRepository;
import com.funds.transfer.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        Account savedAccount = accountRepository.save(AccountMapper.mapToAccount(accountDto));
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    @Transactional
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(AccountMapper::mapToAccountDto).toList();

    }

    @Override
    public void deleteAccount() {

    }

    @Override
    public void createTransfer() {

    }

    @Override
    public void checkBalance() {

    }

    public AccountDto findAccountById(int accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException("Account ID does not exist"));
        return AccountMapper.mapToAccountDto(account);
    }
}
