package com.funds.transfer.mapper;

import com.funds.transfer.entity.Transaction;
import com.funds.transfer.model.TransactionDto;
import com.funds.transfer.model.TxStatus;
import jakarta.persistence.Column;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TransactionMapper {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime transactionDate;
    private TxStatus status;

    public static Transaction mapToTransaction(TransactionDto transactionDto) {
        return Transaction.builder().
                transactionId(transactionDto.getTransactionNumber()).
                senderAccountId(transactionDto.getSender()).
                receiverAccountId(transactionDto.getReceiver()).
                typeOfTransaction(transactionDto.getTypeOfTransaction()).
                amount(transactionDto.getAmount()).
                toCurrency(transactionDto.getReceiverCurrency()).
                fromCurrency(transactionDto.getSenderCurrency()).
                transactionDate(transactionDto.getTransactionDate()).
                status(transactionDto.getTransactionStatus()).
                build();
    }

    public static TransactionDto mapToTransactionDto(Transaction transaction) {
        return TransactionDto.builder().
                transactionNumber(transaction.getTransactionId()).
                sender(transaction.getSenderAccountId()).
                receiver(transaction.getReceiverAccountId()).
                typeOfTransaction(transaction.getTypeOfTransaction()).
                amount(transaction.getAmount()).
                receiverCurrency(transaction.getToCurrency()).
                senderCurrency(transaction.getFromCurrency()).
                transactionDate(transaction.getTransactionDate()).
                transactionStatus(transaction.getStatus()).
                build();
    }
}
