package com.example.uangku.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.uangku.model.Expense;
import com.example.uangku.model.Income;
import com.example.uangku.model.Transaction;
import com.example.uangku.repository.ExpenseRepository;
import com.example.uangku.repository.IncomeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final IncomeRepository incomeRepo;
    private final ExpenseRepository expenseRepo;

    public Transaction addIncome(Income t) {
        t.setCreatedAt(LocalDateTime.now());
        return incomeRepo.save(t);
    }

    public Transaction addExpense(Expense t) {
        t.setCreatedAt(LocalDateTime.now());
        return expenseRepo.save(t);
    }

    public List<Transaction> getAll() {
        List<Transaction> all = new ArrayList<>();
        all.addAll(incomeRepo.findAll());
        all.addAll(expenseRepo.findAll());
        return all;
    }
}
