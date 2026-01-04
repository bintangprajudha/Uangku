package com.example.uangku.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.uangku.exception.InvalidTransactionException;
import com.example.uangku.model.Expense;
import com.example.uangku.model.User;
import com.example.uangku.repository.ExpenseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public Expense addExpense(Expense expense) {
        // Validasi expense
        validateExpense(expense);

        expense.setCreatedAt(LocalDateTime.now());
        return expenseRepository.save(expense);
    }

    /**
     * Validate expense before saving
     */
    private void validateExpense(Expense expense) {
        if (expense.getAmount() <= 0) {
            throw new InvalidTransactionException("Expense amount must be greater than 0");
        }

        if (expense.getCategory() == null) {
            throw new InvalidTransactionException("Category is required for expense");
        }

        if (expense.getDate() == null) {
            throw new InvalidTransactionException("Date is required for expense");
        }

        if (expense.getUser() == null) {
            throw new InvalidTransactionException("User is required for expense");
        }
    }

    public List<Expense> getAllExpensesByUser(User user) {
        return expenseRepository.findByUser(user);
    }

    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id).orElse(null);
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    public Double getTotalExpenseByUser(User user) {
        return expenseRepository.findByUser(user).stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }
}