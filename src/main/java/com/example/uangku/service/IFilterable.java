package com.example.uangku.service;

import java.time.LocalDate;
import java.util.List;

import com.example.uangku.model.Transaction;

public interface IFilterable {
    List<Transaction> filterByType(List<Transaction> transactions, String type);
    List<Transaction> filterByCategory(List<Transaction> transactions, Long categoryId);
    List<Transaction> filterByDateRange(List<Transaction> transactions, LocalDate startDate, LocalDate endDate);
}
