package com.funds.transfer.mapper;

import com.funds.transfer.entity.Account;
import com.funds.transfer.model.AccountDto;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {


    public static AccountDto mapToAccountDto(Account account) {
        return AccountDto.builder().
                userID(account.getAccountId()).
                userName(account.getAccountName()).
                currency(account.getCurrency()).
                balance(account.getBalance()).
                accountType(account.getAccountType()).
                lastModifiedDate(account.getLastModifiedDate()).
                createdDate(account.getCreatedDate()).
                build();

    }

    public static Account mapToAccount(AccountDto accountDto) {
        return Account.builder().
                accountId(accountDto.getUserID()).
                accountName(accountDto.getUserName()).
                currency(accountDto.getCurrency()).
                balance(accountDto.getBalance()).
                accountType(accountDto.getAccountType()).
                lastModifiedDate(accountDto.getLastModifiedDate()).
                createdDate(accountDto.getCreatedDate()).
                build();

    }
}
