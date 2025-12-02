package com.example.uangku.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.uangku.model.Expense;
import com.example.uangku.model.Income;
import com.example.uangku.model.Transaction;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionManagerService {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(incomeService.getAllIncomes());
        transactions.addAll(expenseService.getAllExpenses());
        return transactions;
    }

    public List<Transaction> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = getAllTransactions();
        return transactions.stream()
                .filter(t -> !t.getDate().isBefore(startDate) && !t.getDate().isAfter(endDate))
                .toList();
    }

    public List<Income> getIncomesByDateRange(LocalDate startDate, LocalDate endDate) {
        return incomeService.getAllIncomes().stream()
                .filter(income -> !income.getDate().isBefore(startDate) && !income.getDate().isAfter(endDate))
                .toList();
    }

    public List<Expense> getExpensesByDateRange(LocalDate startDate, LocalDate endDate) {
        return expenseService.getAllExpenses().stream()
                .filter(expense -> !expense.getDate().isBefore(startDate) && !expense.getDate().isAfter(endDate))
                .toList();
    }

    public Double getTotalIncomeByDateRange(LocalDate startDate, LocalDate endDate) {
        return getIncomesByDateRange(startDate, endDate).stream()
                .mapToDouble(Income::getAmount)
                .sum();
    }

    public Double getTotalExpenseByDateRange(LocalDate startDate, LocalDate endDate) {
        return getExpensesByDateRange(startDate, endDate).stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }
}