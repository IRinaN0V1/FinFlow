package com.example.finflow.dto;

import com.example.finflow.model.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransactionDto {

    @NotBlank(message = "Описание обязательно")
    private String description;

    @NotNull(message = "Сумма обязательна")
    @Positive(message = "Сумма должна быть положительной")
    private BigDecimal amount;

    @NotNull(message = "Тип транзакции обязателен")
    private TransactionType type;

    @NotBlank(message = "Категория обязательна")
    private String category;
}