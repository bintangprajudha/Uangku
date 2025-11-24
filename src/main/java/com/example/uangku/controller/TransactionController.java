package com.example.uangku.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.uangku.model.Expense;
import com.example.uangku.model.Income;
import com.example.uangku.model.Transaction; 
import com.example.uangku.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/income")
    public Transaction addIncome(@RequestBody Income t) {
        return transactionService.addIncome(t);
    }

    @PostMapping("/expense")
    public Transaction addExpense(@RequestBody Expense t) {
        return transactionService.addExpense(t);
    }

    @GetMapping
    public List<Transaction> all() {
        return transactionService.getAll();
    }
}
