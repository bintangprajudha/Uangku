package com.example.uangku.service;

import com.example.uangku.dto.CategoryStatsDTO;
import com.example.uangku.dto.QuickStatsDTO;
import com.example.uangku.model.Expense;
import com.example.uangku.model.Income;
import com.example.uangku.model.User;
import com.example.uangku.repository.ExpenseRepository;
import com.example.uangku.repository.IncomeRepository;
import com.example.uangku.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuickStatsService {
    
    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public QuickStatsDTO getQuickStats(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        
        Double todayExpense = calculateTodayExpense(user, today);
        Double dailyAverage = calculateDailyAverage(user, firstDayOfMonth, today);
        CategoryStatsDTO topCategory = getTopCategory(user, firstDayOfMonth, lastDayOfMonth);
        
        int currentDay = today.getDayOfMonth();
        int totalDays = today.lengthOfMonth();
        int monthProgress = (int) Math.round((currentDay * 100.0) / totalDays);
        
        Double monthIncome = calculateMonthIncome(user, firstDayOfMonth, lastDayOfMonth);
        Double monthExpense = calculateMonthExpense(user, firstDayOfMonth, lastDayOfMonth);
        Double savings = monthIncome - monthExpense;
        
        int savingsRate = calculateSavingsRate(monthIncome, monthExpense);
        String savingsRateStatus = determineSavingsRateStatus(savingsRate);
        
        boolean isTodayAboveAverage = todayExpense > dailyAverage;
        
        QuickStatsDTO stats = new QuickStatsDTO();
        stats.setTodayExpense(todayExpense);
        stats.setDailyAverage(dailyAverage);
        stats.setTopCategory(topCategory != null ? topCategory.getCategoryName() : "N/A");
        stats.setTopCategoryAmount(topCategory != null ? topCategory.getTotalAmount() : 0.0);
        stats.setMonthProgress(monthProgress);
        stats.setCurrentDay(currentDay);
        stats.setTotalDaysInMonth(totalDays);
        stats.setSavingsRate(savingsRate);
        stats.setTotalIncome(monthIncome);
        stats.setTotalExpense(monthExpense);
        stats.setTotalSavings(savings);
        stats.setIsTodayAboveAverage(isTodayAboveAverage);
        stats.setSavingsRateStatus(savingsRateStatus);
        
        return stats;
    }
    
    private Double calculateTodayExpense(User user, LocalDate today) {
        List<Expense> todayExpenses = expenseRepository.findByUser(user).stream()
            .filter(e -> e.getDate() != null && e.getDate().equals(today))
            .collect(Collectors.toList());
        
        return todayExpenses.stream()
            .map(Expense::getAmount)
            .reduce(0.0, Double::sum);
    }

    private Double calculateDailyAverage(User user, LocalDate startDate, LocalDate endDate) {
        List<Expense> expenses = expenseRepository.findByUser(user).stream()
            .filter(e -> e.getDate() != null && 
                        !e.getDate().isBefore(startDate) && 
                        !e.getDate().isAfter(endDate))
            .collect(Collectors.toList());
        
        Double totalExpense = expenses.stream()
            .map(Expense::getAmount)
            .reduce(0.0, Double::sum);
        
        int daysElapsed = endDate.getDayOfMonth();
        
        if (daysElapsed == 0) {
            return 0.0;
        }
        
        return totalExpense / daysElapsed;
    }

    private CategoryStatsDTO getTopCategory(User user, LocalDate startDate, LocalDate endDate) {
        List<Expense> expenses = expenseRepository.findByUser(user).stream()
            .filter(e -> e.getDate() != null && 
                        !e.getDate().isBefore(startDate) && 
                        !e.getDate().isAfter(endDate))
            .collect(Collectors.toList());
        
        if (expenses.isEmpty()) {
            return null;
        }
        
        Map<String, Double> categoryTotals = new HashMap<>();
        Map<String, Integer> categoryCounts = new HashMap<>();
        
        for (Expense expense : expenses) {
            String categoryName = expense.getCategory().getName();
            categoryTotals.merge(categoryName, expense.getAmount(), Double::sum);
            categoryCounts.merge(categoryName, 1, Integer::sum);
        }
        
        String topCategoryName = categoryTotals.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("N/A");
        
        Double topAmount = categoryTotals.get(topCategoryName);
        Integer transactionCount = categoryCounts.get(topCategoryName);
        
        Double totalExpense = categoryTotals.values().stream()
            .reduce(0.0, Double::sum);
        
        double percentage = totalExpense > 0 ? (topAmount / totalExpense) * 100 : 0.0;
        
        return new CategoryStatsDTO(topCategoryName, topAmount, transactionCount, percentage);
    }
    
    private Double calculateMonthIncome(User user, LocalDate startDate, LocalDate endDate) {
        List<Income> incomes = incomeRepository.findByUser(user).stream()
            .filter(i -> i.getDate() != null && 
                        !i.getDate().isBefore(startDate) && 
                        !i.getDate().isAfter(endDate))
            .collect(Collectors.toList());
        
        return incomes.stream()
            .map(Income::getAmount)
            .reduce(0.0, Double::sum);
    }
    
    private Double calculateMonthExpense(User user, LocalDate startDate, LocalDate endDate) {
        List<Expense> expenses = expenseRepository.findByUser(user).stream()
            .filter(e -> e.getDate() != null && 
                        !e.getDate().isBefore(startDate) && 
                        !e.getDate().isAfter(endDate))
            .collect(Collectors.toList());
        
        return expenses.stream()
            .map(Expense::getAmount)
            .reduce(0.0, Double::sum);
    }
    
    private int calculateSavingsRate(Double income, Double expense) {
        if (income == 0) {
            return 0;
        }
        
        Double savings = income - expense;
        Double rate = (savings / income) * 100;
        
        return Math.max(0, rate.intValue());
    }

    private String determineSavingsRateStatus(int savingsRate) {
        if (savingsRate >= 30) {
            return "EXCELLENT";
        } else if (savingsRate >= 20) {
            return "GOOD";
        } else if (savingsRate >= 10) {
            return "WARNING";
        } else {
            return "POOR";
        }
    }
    
    @Transactional(readOnly = true)
    public List<CategoryStatsDTO> getCategoryBreakdown(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        
        List<Expense> expenses = expenseRepository.findByUser(user).stream()
            .filter(e -> e.getDate() != null && 
                        !e.getDate().isBefore(firstDayOfMonth) && 
                        !e.getDate().isAfter(lastDayOfMonth))
            .collect(Collectors.toList());
        
        Map<String, Double> categoryTotals = new HashMap<>();
        Map<String, Integer> categoryCounts = new HashMap<>();
        
        for (Expense expense : expenses) {
            String categoryName = expense.getCategory().getName();
            categoryTotals.merge(categoryName, expense.getAmount(), Double::sum);
            categoryCounts.merge(categoryName, 1, Integer::sum);
        }
        
        Double totalExpense = categoryTotals.values().stream()
            .reduce(0.0, Double::sum);
        
        return categoryTotals.entrySet().stream()
            .map(entry -> {
                String categoryName = entry.getKey();
                Double amount = entry.getValue();
                Integer count = categoryCounts.get(categoryName);
                
                double percentage = totalExpense > 0 ? (amount / totalExpense) * 100 : 0.0;
                
                return new CategoryStatsDTO(categoryName, amount, count, percentage);
            })
            .sorted((a, b) -> b.getTotalAmount().compareTo(a.getTotalAmount()))
            .collect(Collectors.toList());
    }
}