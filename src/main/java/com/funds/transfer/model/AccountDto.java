package com.funds.transfer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccountDto {

    private int userID;
    private String userName;
    private String currency;
    private double balance;
    private AccountType accountType;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
