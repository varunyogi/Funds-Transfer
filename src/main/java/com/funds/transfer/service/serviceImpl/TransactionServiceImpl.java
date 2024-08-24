package com.funds.transfer.service.serviceImpl;

import com.funds.transfer.entity.Transaction;
import com.funds.transfer.exception.AccountNotFoundException;
import com.funds.transfer.exception.CurrencyNotSupportedException;
import com.funds.transfer.exception.InsufficientAmountException;
import com.funds.transfer.exception.TransactionTypeNotSupportedException;
import com.funds.transfer.mapper.TransactionMapper;
import com.funds.transfer.model.*;
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
        if (transactionDto.getTypeOfTransaction().equals(TypeOfTransaction.TRANSFER)) {
            return makeTransaction(transactionDto);
        } else if (transactionDto.getTypeOfTransaction().equals(TypeOfTransaction.DEPOSIT)) {
            return deposit(transactionDto.getSender(), transactionDto.getAmount());
        } else if (transactionDto.getTypeOfTransaction().equals(TypeOfTransaction.WITHDRAWAL)) {
            return withdraw(transactionDto.getSender(), transactionDto.getAmount());
        } else {
            throw new TransactionTypeNotSupportedException("Our Application only supports account type as CREDIT or DEBIT");
        }
    }


    public TransactionDto makeTransaction(TransactionDto transactionDto) {
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
    public TransactionDto deposit(int accountId, double amount) {
        Transaction savedTransaction = null;
        AccountDto accountById = accountService.findAccountById(accountId);
        accountById.setBalance(accountById.getBalance() + amount);
        if (accountService.updateAccount(accountById) != null) {
            Transaction transaction = Transaction.builder().
                    fromCurrency(accountById.getCurrency()).
                    toCurrency(accountById.getCurrency()).
                    status(TxStatus.COMPLETED).
                    senderAccountId(accountId).
                    amount(amount).
                    typeOfTransaction(TypeOfTransaction.DEPOSIT).
                    build();


            savedTransaction = transactionRepository.save(transaction);
        }
        return TransactionMapper.mapToTransactionDto(savedTransaction);


    }

    @Override
    public TransactionDto withdraw(int accountId, double amount) {
        Transaction savedTransaction = null;
        AccountDto accountById = accountService.findAccountById(accountId);

        if (accountById.getBalance() >= amount) {
            accountById.setBalance(accountById.getBalance() - amount);
            if (accountService.updateAccount(accountById) != null) {
                Transaction transaction = Transaction.builder().
                        fromCurrency(accountById.getCurrency()).
                        toCurrency(accountById.getCurrency()).
                        status(TxStatus.COMPLETED).
                        senderAccountId(accountId).
                        typeOfTransaction(TypeOfTransaction.WITHDRAWAL).
                        amount(amount).
                        build();


                savedTransaction = transactionRepository.save(transaction);

            }
        } else {
            throw new InsufficientAmountException("Insufficient amount to withdraw");
        }

        return TransactionMapper.mapToTransactionDto(savedTransaction);
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
