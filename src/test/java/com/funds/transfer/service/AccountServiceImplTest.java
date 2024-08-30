package com.funds.transfer.service;

import com.funds.transfer.entity.Account;
import com.funds.transfer.exception.AccountNotFoundException;
import com.funds.transfer.exception.CurrencyNotSupportedException;
import com.funds.transfer.mapper.AccountMapper;
import com.funds.transfer.model.AccountDto;
import com.funds.transfer.model.AccountType;
import com.funds.transfer.repository.AccountRepository;
import com.funds.transfer.service.serviceImpl.AccountServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {


    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountServiceImpl accountService;


    @Test
    public void createAccount_returnAccountDto() {

        Account johnAccount = Account.builder().accountName("John").accountType(AccountType.CREDIT).balance(200).currency("EUR").build();

        AccountDto accountDto = AccountDto.builder().userName("John").accountType(AccountType.CREDIT).balance(200).currency("USD").build();

        when(accountRepository.save(any(Account.class))).thenReturn(johnAccount);

        AccountDto savedAccount = accountService.createAccount(accountDto);
        Assertions.assertThat(savedAccount).isNotNull();

    }

    @Test
    public void getAllAccounts_returnAllAccountsDto() {
        AccountDto accountDto = Mockito.mock(AccountDto.class);
    }


    @Test
    public void testCreateAccount() {
        AccountDto accountDto = new AccountDto(1, "varun", "EUR", 1000, AccountType.CREDIT, LocalDateTime.now(), LocalDateTime.now());
        Account account = AccountMapper.mapToAccount(accountDto);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        AccountDto createdAccount = accountService.createAccount(accountDto);
        assertNotNull(createdAccount);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void testFindAccountById() {
        AccountDto accountDto = new AccountDto(1, "varun", "EUR", 1000, AccountType.CREDIT, LocalDateTime.now(), LocalDateTime.now());
        Account account = AccountMapper.mapToAccount(accountDto);
        when(accountRepository.findById(account.getAccountId())).thenReturn(Optional.of(account));
        AccountDto foundAccount = accountService.findAccountById(accountDto.getUserID());
        verify(accountRepository, times(1)).findById(account.getAccountId());
    }

    @Test
    public void testFindAccountById_NotFound() {
        int accountId = 1;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountService.findAccountById(accountId));
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    public void CurrencyNotFoundDuringAccountCreation() {

        AccountDto accountDto = AccountDto.builder().userName("John").accountType(AccountType.CREDIT).balance(200).currency("U3D").build();
        assertThrows(CurrencyNotSupportedException.class, () -> accountService.createAccount(accountDto));


    }

}
