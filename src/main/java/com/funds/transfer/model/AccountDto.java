package com.funds.transfer.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccountDto {

    private int userID;
    @NotBlank(message = "Account name cannot be Blank")
    private String userName;
    @NotBlank(message = "CURRENNCY cannot be Blank")
    @Size(min = 3, max = 3, message = "Currency must be a 3-letter code")
    private String currency;
    @PositiveOrZero(message = "Balance must be equal or greater than 0")
    private double balance;
    private AccountType accountType;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
