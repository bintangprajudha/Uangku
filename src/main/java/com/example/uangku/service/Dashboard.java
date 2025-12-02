package com.example.uangku.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.uangku.model.Transaction;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Dashboard {

    private final TransactionManagerService transactionManager;
    private final StatisticCalculatorService statisticsCalculator;

    /**
     * Get total income for a specific date range
     */
    public double getTotalIncome(LocalDate startDate, LocalDate endDate) {
        return statisticsCalculator.getTotalIncome(startDate, endDate);
    }

    /**
     * Get total expense for a specific date range
     */
    public double getTotalExpense(LocalDate startDate, LocalDate endDate) {
        return statisticsCalculator.getTotalExpense(startDate, endDate);
    }

    /**
     * Get balance (income - expense) for a specific date range
     */
    public double getBalance(LocalDate startDate, LocalDate endDate) {
        return statisticsCalculator.getBalance(startDate, endDate);
    }

    /**
     * Get comprehensive monthly data for a specific year and month
     */
    public Map<String, Object> getMonthlyData(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        double totalIncome = getTotalIncome(startDate, endDate);
        double totalExpense = getTotalExpense(startDate, endDate);
        double balance = getBalance(startDate, endDate);

        List<Transaction> transactions = transactionManager.getTransactionsByDateRange(startDate, endDate);

        Map<String, Object> monthlyData = new HashMap<>();
        monthlyData.put("year", year);
        monthlyData.put("month", month);
        monthlyData.put("monthName", yearMonth.getMonth().name());
        monthlyData.put("totalIncome", totalIncome);
        monthlyData.put("totalExpense", totalExpense);
        monthlyData.put("balance", balance);
        monthlyData.put("transactionCount", transactions.size());
        monthlyData.put("transactions", transactions);
        monthlyData.put("startDate", startDate);
        monthlyData.put("endDate", endDate);

        // Calculate daily averages
        int daysInMonth = yearMonth.lengthOfMonth();
        monthlyData.put("avgDailyIncome", totalIncome / daysInMonth);
        monthlyData.put("avgDailyExpense", totalExpense / daysInMonth);

        return monthlyData;
    }

    /**
     * Get current month summary with additional insights
     */
    public Map<String, Object> getCurrentMonthSummary() {
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();

        Map<String, Object> currentSummary = getMonthlyData(currentYear, currentMonth);

        // Add current month specific data
        int daysPassed = now.getDayOfMonth();
        int daysRemaining = now.lengthOfMonth() - daysPassed;

        double totalIncome = (Double) currentSummary.get("totalIncome");
        double totalExpense = (Double) currentSummary.get("totalExpense");

        // Calculate projected monthly totals based on current trend
        double projectedMonthlyIncome = daysPassed > 0 ? (totalIncome / daysPassed) * now.lengthOfMonth() : 0;
        double projectedMonthlyExpense = daysPassed > 0 ? (totalExpense / daysPassed) * now.lengthOfMonth() : 0;
        double projectedBalance = projectedMonthlyIncome - projectedMonthlyExpense;

        // Add additional insights
        currentSummary.put("currentDate", now);
        currentSummary.put("daysPassed", daysPassed);
        currentSummary.put("daysRemaining", daysRemaining);
        currentSummary.put("projectedMonthlyIncome", projectedMonthlyIncome);
        currentSummary.put("projectedMonthlyExpense", projectedMonthlyExpense);
        currentSummary.put("projectedBalance", projectedBalance);
        currentSummary.put("isCurrentMonth", true);

        // Calculate spending rate
        if (daysPassed > 0) {
            currentSummary.put("dailyIncomeRate", totalIncome / daysPassed);
            currentSummary.put("dailyExpenseRate", totalExpense / daysPassed);
        } else {
            currentSummary.put("dailyIncomeRate", 0.0);
            currentSummary.put("dailyExpenseRate", 0.0);
        }

        return currentSummary;
    }

    /**
     * Get overall totals (all time)
     */
    public double getTotalIncome() {
        return statisticsCalculator.getTotalIncome();
    }

    public double getTotalExpense() {
        return statisticsCalculator.getTotalExpense();
    }

    public double getBalance() {
        return statisticsCalculator.getBalance();
    }
}