package com.example.finflow.service;

import com.example.finflow.dto.TransactionDto;
import com.example.finflow.model.Transaction;
import com.example.finflow.model.TransactionType;
import com.example.finflow.model.User;
import com.example.finflow.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TransactionService transactionService;

    private User testUser;
    private TransactionDto incomeDto;
    private TransactionDto expenseDto;

    @BeforeEach
    void setUp() {
        testUser = new User("test@example.com", "Test User", "password");
        testUser.setId(1L);

        incomeDto = new TransactionDto();
        incomeDto.setDescription("Salary");
        incomeDto.setAmount(BigDecimal.valueOf(100000));
        incomeDto.setType(TransactionType.INCOME);
        incomeDto.setCategory("SALARY");

        expenseDto = new TransactionDto();
        expenseDto.setDescription("Food");
        expenseDto.setAmount(BigDecimal.valueOf(5000));
        expenseDto.setType(TransactionType.EXPENSE);
        expenseDto.setCategory("FOOD");
    }

    @Test
    void createTransaction_WithIncome_ShouldSaveTransaction() {
        when(userService.findById(1L)).thenReturn(testUser);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result = transactionService.createTransaction(incomeDto, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo("Salary");
        assertThat(result.getAmount()).isEqualTo(BigDecimal.valueOf(100000));
        assertThat(result.getType()).isEqualTo(TransactionType.INCOME);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void getBalance_WithIncomeAndExpense_ShouldReturnCorrectBalance() {
        when(transactionRepository.sumByUserIdAndType(1L, TransactionType.INCOME))
                .thenReturn(BigDecimal.valueOf(100000));
        when(transactionRepository.sumByUserIdAndType(1L, TransactionType.EXPENSE))
                .thenReturn(BigDecimal.valueOf(5000));

        BigDecimal balance = transactionService.getBalance(1L);

        assertThat(balance).isEqualTo(BigDecimal.valueOf(95000));
    }

    @Test
    void getBalance_WithNoTransactions_ShouldReturnZero() {
        when(transactionRepository.sumByUserIdAndType(1L, TransactionType.INCOME))
                .thenReturn(null);
        when(transactionRepository.sumByUserIdAndType(1L, TransactionType.EXPENSE))
                .thenReturn(null);

        BigDecimal balance = transactionService.getBalance(1L);

        assertThat(balance).isEqualTo(BigDecimal.ZERO);
    }
}