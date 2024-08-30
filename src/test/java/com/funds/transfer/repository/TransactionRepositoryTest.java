package com.funds.transfer.repository;


import com.funds.transfer.entity.Account;
import com.funds.transfer.entity.Transaction;
import com.funds.transfer.model.AccountType;
import com.funds.transfer.model.TypeOfTransaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    public void setUp() {
        Account johnAccount = Account.builder().
                accountName("John").
                accountType(AccountType.CREDIT).
                balance(200).currency("EUR").build();

        Account kaneAccount = Account.builder().
                accountName("Kane").
                accountType(AccountType.DEBIT).
                balance(300).currency("USD").build();

        accountRepository.save(johnAccount);
        accountRepository.save(kaneAccount);
    }

    @Test
    public void saveTransaction_returnTransaction() {

        Transaction transaction = Transaction.builder().
                typeOfTransaction(TypeOfTransaction.TRANSFER).senderAccountId(1).receiverAccountId(2).build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        Assertions.assertNotNull(savedTransaction);
        Assertions.assertTrue(savedTransaction.getReceiverAccountId() == 2);
        Assertions.assertFalse(savedTransaction.getSenderAccountId() == 2);


    }

    @Test
    public void findAllTransactions_returnListOfTransactions() {
        Transaction firstTransaction = Transaction.builder().
                typeOfTransaction(TypeOfTransaction.TRANSFER).senderAccountId(1).receiverAccountId(2).amount(100).build();

        Transaction secondTransaction = Transaction.builder().
                typeOfTransaction(TypeOfTransaction.TRANSFER).senderAccountId(2).receiverAccountId(1).amount(1000).build();
        transactionRepository.save(firstTransaction);
        transactionRepository.save(secondTransaction);

        Assertions.assertTrue(transactionRepository.findAll().size() == 2);

    }
}
