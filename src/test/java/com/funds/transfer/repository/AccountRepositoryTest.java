package com.funds.transfer.repository;

import com.funds.transfer.entity.Account;
import com.funds.transfer.model.AccountType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void saveAccount_returnAccount() {
        Account johnAccount = Account.builder().
                accountName("John").
                accountType(AccountType.CREDIT).
                balance(200).currency("EUR").build();

        Account savedAccount = accountRepository.save(johnAccount);

        Assertions.assertThat(savedAccount).isNotNull();
        Assertions.assertThat(savedAccount.getAccountName()).isEqualTo(johnAccount.getAccountName());

    }

    @Test
    public void findAllAccounts_returnListOfAccounts() {
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

        List<Account> accountList=accountRepository.findAll();

        Assertions.assertThat(accountList).isNotNull();
        Assertions.assertThat(accountList.size()).isEqualTo(2);
        Assertions.assertThat(accountList.get(0).getAccountName()).isEqualTo(johnAccount.getAccountName());
    }


}
