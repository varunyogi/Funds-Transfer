package com.funds.transfer.service;

import com.funds.transfer.entity.CurrencyExchanger;
import com.funds.transfer.entity.Transaction;
import com.funds.transfer.exception.InsufficientAmountException;
import com.funds.transfer.exception.InvalidAmountException;
import com.funds.transfer.exception.TransactionTypeNotSupportedException;
import com.funds.transfer.model.AccountDto;
import com.funds.transfer.model.TransactionDto;
import com.funds.transfer.model.TxStatus;
import com.funds.transfer.model.TypeOfTransaction;
import com.funds.transfer.repository.TransactionRepository;
import com.funds.transfer.service.serviceImpl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTransaction_Transfer() {
        TransactionDto transactionDto = TransactionDto.builder().typeOfTransaction(TypeOfTransaction.TRANSFER).sender(1).receiver(2).amount(100).build();


        AccountDto senderAccount = new AccountDto();
        senderAccount.setUserID(1);
        senderAccount.setBalance(1000);
        senderAccount.setCurrency("USD");

        AccountDto receiverAccount = new AccountDto();
        receiverAccount.setUserID(2);
        receiverAccount.setBalance(500);
        receiverAccount.setCurrency("EUR");

        Map<String, String> accountMap = new HashMap<>();
        accountMap.put("balance", "1000");
        accountMap.put("username", "John");
        accountMap.put("currency", "USD");


        when(accountService.isValidAccount(1)).thenReturn(true);
        when(accountService.isValidAccount(2)).thenReturn(true);
        when(accountService.getAccountsByIds(any())).thenReturn(Arrays.asList(senderAccount, receiverAccount));
        when(accountService.checkBalance(1)).thenReturn(accountMap);
        when(exchangeRateService.getExchangeRateAmountForPair("USD", "EUR", 100)).thenReturn(new CurrencyExchanger("USD", "USD", 0.90, 90));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction(101, 1, 2, TypeOfTransaction.TRANSFER, 100, "USD", "USD", LocalDateTime.now(), TxStatus.COMPLETED));

        TransactionDto result = transactionService.createTransaction(transactionDto);

        assertNotNull(result);
        verify(accountService, times(2)).updateAccount(any());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testCreateTransaction_InvalidTransactionType() {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTypeOfTransaction(null); // Invalid type

        assertThrows(TransactionTypeNotSupportedException.class, () -> transactionService.createTransaction(transactionDto));
    }

    @Test
    void testDeposit_Success() {
        int accountId = 1;
        double amount = 100;

        AccountDto accountDto = new AccountDto();
        accountDto.setUserID(accountId);
        accountDto.setBalance(500);
        accountDto.setCurrency("USD");

        when(accountService.findAccountById(accountId)).thenReturn(accountDto);
        when(accountService.updateAccount(any(AccountDto.class))).thenReturn(accountDto);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        TransactionDto result = transactionService.deposit(accountId, amount);

        assertNotNull(result);
        assertEquals(accountDto.getBalance(), 600); // 500 + 100
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testDeposit_InvalidAmount() {

        int accountId = 1;
        double amount = -100; // Invalid amount

        AccountDto accountDto = new AccountDto();
        accountDto.setUserID(accountId);
        accountDto.setBalance(500);
        accountDto.setCurrency("USD");

        when(accountService.findAccountById(accountId)).thenReturn(accountDto);
        assertThrows(InvalidAmountException.class, () -> transactionService.deposit(accountId, amount));
    }

    @Test
    void testWithdraw_InsufficientBalance() {
        int accountId = 1;
        double amount = 1000; // Amount greater than balance

        AccountDto accountDto = new AccountDto();
        accountDto.setUserID(accountId);
        accountDto.setBalance(500);
        accountDto.setCurrency("USD");

        when(accountService.findAccountById(accountId)).thenReturn(accountDto);

        assertThrows(InsufficientAmountException.class, () -> transactionService.withdraw(accountId, amount));
    }
}
