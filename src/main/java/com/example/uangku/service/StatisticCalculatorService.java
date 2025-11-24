package com.example.uangku.service;

import org.springframework.stereotype.Service;

import com.example.uangku.model.Expense;
import com.example.uangku.model.Income;
import com.example.uangku.model.Transaction;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatisticCalculatorService {
    private final TransactionService transactionService;

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
}
