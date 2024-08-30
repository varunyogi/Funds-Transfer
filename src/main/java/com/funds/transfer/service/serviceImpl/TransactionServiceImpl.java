package com.funds.transfer.service.serviceImpl;

import com.funds.transfer.entity.CurrencyExchanger;
import com.funds.transfer.entity.Transaction;
import com.funds.transfer.exception.AccountNotFoundException;
import com.funds.transfer.exception.InsufficientAmountException;
import com.funds.transfer.exception.InvalidAmountException;
import com.funds.transfer.exception.TransactionTypeNotSupportedException;
import com.funds.transfer.mapper.TransactionMapper;
import com.funds.transfer.model.*;
import com.funds.transfer.repository.TransactionRepository;
import com.funds.transfer.service.AccountService;
import com.funds.transfer.service.ExchangeRateService;
import com.funds.transfer.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);


    private TransactionRepository transactionRepository;
    private AccountService accountService;
    private ExchangeRateService exchangeRateService;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountService accountService, ExchangeRateService exchangeRateService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.exchangeRateService = exchangeRateService;
    }

    @Override
    public TransactionDto createTransaction(TransactionDto transactionDto) {
        if (transactionDto.getTypeOfTransaction().equals(TypeOfTransaction.TRANSFER)) {
            logger.info("Create Transaction with transaction type as TRANSFER");
            return makeTransaction(transactionDto);
        } else if (transactionDto.getTypeOfTransaction().equals(TypeOfTransaction.DEPOSIT)) {
            logger.info("Create Transaction with transaction type as DEPOSIT");
            return deposit(transactionDto.getSender(), transactionDto.getAmount());
        } else if (transactionDto.getTypeOfTransaction().equals(TypeOfTransaction.WITHDRAWAL)) {
            logger.info("Create Transaction with transaction type as WITHDRAWAL");
            return withdraw(transactionDto.getSender(), transactionDto.getAmount());
        } else {
            logger.error("Invalid transaction type inserted by user " + transactionDto.getTypeOfTransaction());
            throw new TransactionTypeNotSupportedException("Our Application only supports account type as CREDIT or DEBIT");
        }
    }


    public TransactionDto makeTransaction(TransactionDto transactionDto) {
        Transaction savedTransaction = null;
        if (accountService.isValidAccount(transactionDto.getReceiver()) && accountService.isValidAccount(transactionDto.getSender())) {
            if (transactionDto.getAmount() > 1) {

                if (Double.valueOf(accountService.checkBalance(transactionDto.getSender()).get("balance")) > transactionDto.getAmount()) {
                    List<AccountDto> accountDtos = updateAccountForTransferTransaction(transactionDto);
                    transactionDto.setSenderCurrency(getCurrencyType(accountDtos, transactionDto, 'S'));
                    transactionDto.setReceiverCurrency(getCurrencyType(accountDtos, transactionDto, 'R'));
                    transactionDto.setTransactionStatus(TxStatus.COMPLETED);
                    logger.info("Transaction object created, under process");
                    savedTransaction = transactionRepository.save(TransactionMapper.mapToTransaction(transactionDto));
                    logger.info("Transaction completed with transaction: " + savedTransaction);


                } else {
                    logger.error("user " + transactionDto.getSender() + " don't have sufficient balance in the account");
                    throw new InsufficientAmountException("Your account does not have enough balance to make this transaction");

                }
            } else {
                logger.error(transactionDto.getAmount() + " invalid amount inserted by user");
                throw new InvalidAmountException("Please provide a valid amount to initiate this transaction");
            }
        } else {
            logger.error("SENDER/RECEIVER ID provided is invalid, sender :" + transactionDto.getSender() + "receiver:" + transactionDto.getReceiver());
            throw new AccountNotFoundException("Please provide a valid SENDER/RECEIVER account ID");
        }


        return TransactionMapper.mapToTransactionDto(savedTransaction);

    }

    private String getCurrencyType(List<AccountDto> accountDtoList, TransactionDto transactionDto, char type) {
        if (type == 'R') {
            return accountDtoList.stream().filter(accountDto -> accountDto.getUserID() == transactionDto.getReceiver()).findFirst().orElse(null).getCurrency();
        } else {
            return accountDtoList.stream().filter(accountDto -> accountDto.getUserID() == transactionDto.getSender()).findFirst().orElse(null).getCurrency();
        }

    }


    public List<AccountDto> updateAccountForTransferTransaction(TransactionDto transactionDto) {
        List<AccountDto> accountsByIds = accountService.getAccountsByIds(Arrays.asList(transactionDto.getSender(), transactionDto.getReceiver()));
        AccountDto senderAccount = accountsByIds.stream().filter(accountDto -> accountDto.getUserID() == transactionDto.getSender()).findFirst().orElse(null);
        AccountDto receiverAccount = accountsByIds.stream().filter(accountDto -> accountDto.getUserID() == transactionDto.getReceiver()).findFirst().orElse(null);
        logger.info("Fetching current rates and amount to be transferred from Exchange RAte API");
        CurrencyExchanger exchangeRateAmountForPair = exchangeRateService.getExchangeRateAmountForPair
                (senderAccount.getCurrency(), receiverAccount.getCurrency(), transactionDto.getAmount());
        logger.info("Current Rates fetched:" + exchangeRateAmountForPair);
        for (AccountDto accountDto : accountsByIds) {
            if (transactionDto.getSender() == accountDto.getUserID()) {
                accountDto.setBalance(accountDto.getBalance() - transactionDto.getAmount());
                logger.info("updating sender account balance ");
                accountService.updateAccount(accountDto);
            } else if (transactionDto.getReceiver() == accountDto.getUserID()) {
                accountDto.setBalance(accountDto.getBalance() + exchangeRateAmountForPair.getConversion_result());
                logger.info("updating receiver account balance ");
                accountService.updateAccount(accountDto);
            }
        }

        return accountsByIds;


    }


    @Override
    public List<TransactionDto> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream().map(TransactionMapper::mapToTransactionDto).toList();
    }

    @Override
    public TransactionDto deposit(int accountId, double amount) {
        Transaction savedTransaction = null;
        logger.info("Fetching account details with account ID " + accountId);
        AccountDto accountById = null;
        if (accountService.findAccountById(accountId) != null) {
            accountById = accountService.findAccountById(accountId);
            accountById.setBalance(accountById.getBalance() + amount);
        } else {
            logger.error("account not found with account ID " + accountId);
            throw new AccountNotFoundException("Account not found");
        }

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