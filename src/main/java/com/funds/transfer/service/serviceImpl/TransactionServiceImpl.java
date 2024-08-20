package com.funds.transfer.service.serviceImpl;

import com.funds.transfer.entity.Transaction;
import com.funds.transfer.mapper.TransactionMapper;
import com.funds.transfer.model.TransactionDto;
import com.funds.transfer.repository.TransactionRepository;
import com.funds.transfer.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public TransactionDto createTransaction(TransactionDto transactionDto) {
        Transaction savedTransaction = transactionRepository.save(TransactionMapper.mapToTransaction(transactionDto));
        return TransactionMapper.mapToTransactionDto(savedTransaction);
    }

    @Override
    public List<TransactionDto> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream().map(TransactionMapper::mapToTransactionDto).toList();
    }
}
