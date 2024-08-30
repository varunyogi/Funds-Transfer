package com.funds.transfer.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

    private int transactionNumber;
    private int sender;
    private int receiver;
    private TypeOfTransaction typeOfTransaction;
    @PositiveOrZero(message = "Amount must be equal or greater than 0")
    private double amount;
    private String receiverCurrency;
    private String senderCurrency;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime transactionDate;
    private TxStatus transactionStatus;
}
