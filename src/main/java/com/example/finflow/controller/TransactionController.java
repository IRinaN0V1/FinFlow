package com.example.finflow.controller;

import com.example.finflow.dto.TransactionDto;
import com.example.finflow.model.Transaction;
import com.example.finflow.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(
            @Valid @RequestBody TransactionDto dto,
            @RequestHeader("X-User-Id") Long userId) {
        Transaction transaction = transactionService.createTransaction(dto, userId);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(transactionService.getBalance(userId));
    }
}