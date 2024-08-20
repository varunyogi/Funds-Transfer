package com.funds.transfer.entity;

import com.funds.transfer.model.TxStatus;
import com.funds.transfer.model.TypeOfTransaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionId;
    private int senderAccountId;
    private int receiverAccountId;
    private TypeOfTransaction typeOfTransaction;
    private double amount;
    private String toCurrency;
    private String fromCurrency;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime transactionDate;
    private TxStatus status;


}
