package com.funds.transfer.service.serviceImpl;

import com.funds.transfer.entity.Transaction;
import com.funds.transfer.exception.AccountNotFoundException;
import com.funds.transfer.exception.CurrencyNotSupportedException;
import com.funds.transfer.exception.InsufficientAmountException;
import com.funds.transfer.mapper.TransactionMapper;
import com.funds.transfer.model.AccountDto;
import com.funds.transfer.model.Currency;
import com.funds.transfer.model.TransactionDto;
import com.funds.transfer.repository.TransactionRepository;
import com.funds.transfer.service.AccountService;
import com.funds.transfer.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountService accountService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    @Override
    public TransactionDto createTransaction(TransactionDto transactionDto) {

        Transaction savedTransaction = null;

        if (accountService.isValidAccount(transactionDto.getReceiver()) && accountService.isValidAccount(transactionDto.getSender())) {
            if (Double.valueOf(accountService.checkBalance(transactionDto.getSender()).get("balance")) > transactionDto.getAmount()) {
                if (isValidCurrency(transactionDto.getReceiverCurrency()) && isValidCurrency(transactionDto.getSenderCurrency())) {
                    savedTransaction = transactionRepository.save(TransactionMapper.mapToTransaction(transactionDto));
                } else {
                    throw new CurrencyNotSupportedException("This currency is not supported by our platform");
                }

            } else {
                throw new InsufficientAmountException("Your account does not have enough balance to make this transaction");
            }
        } else {
            throw new AccountNotFoundException("Please provide a valid SENDER/RECEIVER account ID");
        }

        return TransactionMapper.mapToTransactionDto(savedTransaction);
    }

    @Override
    public List<TransactionDto> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream().map(TransactionMapper::mapToTransactionDto).toList();
    }

    @Override
    public AccountDto deposit(int accountId, double amount) {
        AccountDto accountById = accountService.findAccountById(accountId);
        accountById.setBalance(accountById.getBalance() + amount);
        return accountService.updateAccount(accountById);

    }

    @Override
    public AccountDto withdraw(int accountId, double amount) {
        return null;
    }

    public boolean isValidCurrency(String currency) {
        try {
            Currency.valueOf(currency.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
