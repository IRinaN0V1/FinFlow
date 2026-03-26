package com.example.finflow.service;

import com.example.finflow.dto.TransactionDto;
import com.example.finflow.model.Transaction;
import com.example.finflow.model.TransactionType;
import com.example.finflow.model.User;
import com.example.finflow.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;

    @Transactional
    public Transaction createTransaction(TransactionDto dto, Long userId) {
        User user = userService.findById(userId);

        if (dto.getType() == TransactionType.EXPENSE) {
            BigDecimal currentBalance = getBalance(userId);
            if (currentBalance.compareTo(dto.getAmount()) < 0) {
                throw new IllegalArgumentException("Недостаточно средств. Баланс: " + currentBalance);
            }
        }

        Transaction transaction = new Transaction(
                dto.getDescription(),
                dto.getAmount(),
                dto.getType(),
                dto.getCategory()
        );
        transaction.setUser(user);

        return transactionRepository.save(transaction);
    }

    @Transactional(readOnly = true)
    public BigDecimal getBalance(Long userId) {
        BigDecimal totalIncome = transactionRepository.sumByUserIdAndType(userId, TransactionType.INCOME);
        BigDecimal totalExpense = transactionRepository.sumByUserIdAndType(userId, TransactionType.EXPENSE);

        totalIncome = totalIncome != null ? totalIncome : BigDecimal.ZERO;
        totalExpense = totalExpense != null ? totalExpense : BigDecimal.ZERO;

        return totalIncome.subtract(totalExpense);
    }
}