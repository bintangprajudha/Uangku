package com.example.uangku.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<Map<String, Object>> getMonthlySummaries(User user) {
        List<Map<String, Object>> summaries = new ArrayList<>();
        LocalDate now = LocalDate.now();

        for (int i = 11; i >= 0; i--) {
            YearMonth yearMonth = YearMonth.from(now.minusMonths(i));
            LocalDate startOfMonth = yearMonth.atDay(1);
            LocalDate endOfMonth = yearMonth.atEndOfMonth();

            double income = getTotalIncome(user, startOfMonth, endOfMonth);
            double expense = getTotalExpense(user, startOfMonth, endOfMonth);

            Map<String, Object> summary = new HashMap<>();
            summary.put("month", yearMonth.getMonth().name() + " " + yearMonth.getYear());
            summary.put("income", income);
            summary.put("expense", expense);
            summaries.add(summary);
        }

        return summaries;
    }
}
