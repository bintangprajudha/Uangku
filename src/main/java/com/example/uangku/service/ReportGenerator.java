package com.example.uangku.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.uangku.model.Report;
import com.example.uangku.model.Transaction;
import com.example.uangku.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportGenerator {

    private final TransactionManagerService transactionManager;
    private final StatisticCalculatorService statisticsCalculator;

    public Report generateMonthlyReport(User user, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<Transaction> transactions = transactionManager.getTransactionsByDateRange(user, startDate, endDate);
        double totalIncome = statisticsCalculator.getTotalIncome(user, startDate, endDate);
        double totalExpense = statisticsCalculator.getTotalExpense(user, startDate, endDate);
        double balance = statisticsCalculator.getBalance(user, startDate, endDate);

        Map<String, Double> categoryBreakdown = new HashMap<>();
        // Calculate category breakdown (simplified)
        transactions.forEach(t -> {
            String categoryName = t.getCategory() != null ? t.getCategory().getName() : "Uncategorized";
            categoryBreakdown.put(categoryName, categoryBreakdown.getOrDefault(categoryName, 0.0) + t.getAmount());
        });

        Report report = new Report("Monthly Report - " + yearMonth.getMonth().name() + " " + year, yearMonth);
        report.setData(transactions, totalIncome, totalExpense, balance, categoryBreakdown);

        return report;
    }

    public Report generateCategoryReport(User user, String category) {
        List<Transaction> allTransactions = transactionManager.getAllTransactionsByUser(user);
        List<Transaction> categoryTransactions = allTransactions.stream()
                .filter(t -> t.getCategory() != null && t.getCategory().getName().equals(category))
                .toList();

        double totalIncome = categoryTransactions.stream()
                .filter(t -> t instanceof com.example.uangku.model.Income)
                .mapToDouble(Transaction::getAmount).sum();
        double totalExpense = categoryTransactions.stream()
                .filter(t -> t instanceof com.example.uangku.model.Expense)
                .mapToDouble(Transaction::getAmount).sum();
        double balance = totalIncome - totalExpense;

        Map<String, Double> categoryBreakdown = new HashMap<>();
        categoryBreakdown.put(category, totalExpense); // Simplified for category report

        Report report = new Report("Category Report - " + category, category);
        report.setData(categoryTransactions, totalIncome, totalExpense, balance, categoryBreakdown);

        return report;
    }

    public Report generateCustomReport(User user, LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = transactionManager.getTransactionsByDateRange(user, startDate, endDate);
        double totalIncome = statisticsCalculator.getTotalIncome(user, startDate, endDate);
        double totalExpense = statisticsCalculator.getTotalExpense(user, startDate, endDate);
        double balance = statisticsCalculator.getBalance(user, startDate, endDate);

        Map<String, Double> categoryBreakdown = new HashMap<>();
        transactions.forEach(t -> {
            String categoryName = t.getCategory() != null ? t.getCategory().getName() : "Uncategorized";
            categoryBreakdown.put(categoryName, categoryBreakdown.getOrDefault(categoryName, 0.0) + t.getAmount());
        });

        Report report = new Report("Custom Report - " + startDate + " to " + endDate, Map.of("startDate", startDate, "endDate", endDate));
        report.setData(transactions, totalIncome, totalExpense, balance, categoryBreakdown);

        return report;
    }
}
