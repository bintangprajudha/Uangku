package com.example.uangku.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.uangku.model.Expense;
import com.example.uangku.model.Income;
import com.example.uangku.model.Transaction;
import com.example.uangku.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatisticCalculatorService {
    
    private final TransactionManagerService transactionManager;

    public double getTotalIncome(User user) {
        return transactionManager.getAllTransactionsByUser(user).stream()
                .filter(t -> t instanceof Income)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalExpense(User user) {
        return transactionManager.getAllTransactionsByUser(user).stream()
                .filter(t -> t instanceof Expense)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getBalance(User user) {
        return getTotalIncome(user) - getTotalExpense(user);
    }

    public double getTotalIncome(User user, LocalDate startDate, LocalDate endDate) {
        return transactionManager.getTotalIncomeByDateRange(user, startDate, endDate);
    }

    public double getTotalExpense(User user, LocalDate startDate, LocalDate endDate) {
        return transactionManager.getTotalExpenseByDateRange(user, startDate, endDate);
    }

    public double getBalance(User user, LocalDate startDate, LocalDate endDate) {
        return getTotalIncome(user, startDate, endDate) - getTotalExpense(user, startDate, endDate);
    }

    public List<Transaction> getTransactionsByDateRange(User user, LocalDate startDate, LocalDate endDate) {
        return transactionManager.getTransactionsByDateRange(user, startDate, endDate);
    }
}
