package com.example.uangku.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Report {
    private String title;
    private Object period;
    private double totalIncome;
    private double totalExpense;
    private double balance;
    private List<Transaction> transactions;
    private Map<String, Double> categoryBreakdown;
    private LocalDateTime generatedAt;

    public Report(String title, Object period) {
        this.title = title;
        this.period = period;
        this.generatedAt = LocalDateTime.now();
    }

    public void setData(List<Transaction> transactions, double totalIncome, double totalExpense, double balance, Map<String, Double> categoryBreakdown) {
        this.transactions = transactions;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.balance = balance;
        this.categoryBreakdown = categoryBreakdown;
    }

    public String getTitle() {
        return title;
    }

    public Object getPeriod() {
        return period;
    }

    public Map<String, Object> getSummary() {
        Map<String, Object> summary = new java.util.HashMap<>();
        summary.put("totalIncome", totalIncome);
        summary.put("totalExpense", totalExpense);
        summary.put("balance", balance);
        summary.put("categoryBreakdown", categoryBreakdown);
        return summary;
    }

    public Map<String, Object> toJSON() {
        Map<String, Object> json = new java.util.HashMap<>();
        json.put("title", title);
        json.put("period", period);
        json.put("totalIncome", totalIncome);
        json.put("totalExpense", totalExpense);
        json.put("balance", balance);
        json.put("transactions", transactions);
        json.put("categoryBreakdown", categoryBreakdown);
        json.put("generatedAt", generatedAt);
        return json;
    }
}
