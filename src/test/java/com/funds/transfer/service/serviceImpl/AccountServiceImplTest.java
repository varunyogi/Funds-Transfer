package com.funds.transfer.service.serviceImpl;

import com.funds.transfer.entity.Account;
import com.funds.transfer.exception.AccountNotFoundException;
import com.funds.transfer.mapper.AccountMapper;
import com.funds.transfer.model.AccountDto;
import com.funds.transfer.model.AccountType;
import com.funds.transfer.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AccountServiceImplTest {


    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAccount() {
        AccountDto accountDto = new AccountDto(1, "varun", "EUR", 1000, AccountType.CREDIT, LocalDateTime.now(), LocalDateTime.now());
        Account account = AccountMapper.mapToAccount(accountDto);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        AccountDto createdAccount = accountService.createAccount(accountDto);
        assertNotNull(createdAccount);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testFindAccountById() {
        AccountDto accountDto = new AccountDto(1, "varun", "EUR", 1000, AccountType.CREDIT, LocalDateTime.now(), LocalDateTime.now());
        Account account = AccountMapper.mapToAccount(accountDto);
        when(accountRepository.findById(account.getAccountId())).thenReturn(Optional.of(account));
        AccountDto foundAccount = accountService.findAccountById(accountDto.getUserID());
        verify(accountRepository, times(1)).findById(account.getAccountId());
    }

    @Test
    void testFindAccountById_NotFound() {
        int accountId = 1;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountService.findAccountById(accountId));
        verify(accountRepository, times(1)).findById(accountId);
    }

}
