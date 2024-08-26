package com.funds.transfer.controller;

import com.funds.transfer.model.TransactionDto;
import com.funds.transfer.service.TransactionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funds/v1")
public class TransactionController {
    private static final Logger logger =  LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transactions")
    public ResponseEntity<TransactionDto> createTransaction(@Valid @RequestBody TransactionDto transactionDto) {
        logger.info("calling Create transaction with " + transactionDto);
        return new ResponseEntity<>(transactionService.createTransaction(transactionDto), HttpStatus.CREATED);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        logger.info("calling Get all transactions");
        return new ResponseEntity<>(transactionService.getAllTransactions(), HttpStatus.OK);
    }


}
