package com.example.uangku.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.uangku.model.Expense;
import com.example.uangku.model.Income;
import com.example.uangku.model.Transaction;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatisticCalculatorService {
    private final TransactionService transactionService;
    private final TransactionManagerService transactionManager;

    public double getTotalIncome() {
        return transactionService.getAll().stream()
                .filter(t -> t instanceof Income)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalExpense() {
        return transactionService.getAll().stream()
                .filter(t -> t instanceof Expense)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getBalance() {
        return getTotalIncome() - getTotalExpense();
    }

    public double getTotalIncome(LocalDate startDate, LocalDate endDate) {
        return transactionManager.getTotalIncomeByDateRange(startDate, endDate);
    }

    public double getTotalExpense(LocalDate startDate, LocalDate endDate) {
        return transactionManager.getTotalExpenseByDateRange(startDate, endDate);
    } 

    public double getBalance(LocalDate startDate, LocalDate endDate) {
        return getTotalIncome(startDate, endDate) - getTotalExpense(startDate, endDate);
    }

    public List<Transaction> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate) {
        return transactionManager.getTransactionsByDateRange(startDate, endDate);
    }
}
