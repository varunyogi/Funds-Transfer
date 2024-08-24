package com.funds.transfer.service;

import com.funds.transfer.model.AccountDto;
import com.funds.transfer.model.TransactionDto;

import java.util.List;

public interface TransactionService {

    public TransactionDto createTransaction(TransactionDto transactionDto);

    public List<TransactionDto> getAllTransactions();

    public TransactionDto deposit(int accountId, double amount);

    public TransactionDto withdraw(int accountId, double amount);
}
