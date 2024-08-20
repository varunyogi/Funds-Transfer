package com.funds.transfer.service;

import com.funds.transfer.model.TransactionDto;

import java.util.List;

public interface TransactionService {

    public TransactionDto createTransaction(TransactionDto transactionDto);

    public List<TransactionDto> getAllTransactions();
}
