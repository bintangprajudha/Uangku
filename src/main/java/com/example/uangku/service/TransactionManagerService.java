package com.example.uangku.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.uangku.model.Expense;
import com.example.uangku.model.Income;
import com.example.uangku.model.Transaction;
import com.example.uangku.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionManagerService {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    public List<Transaction> getAllTransactionsByUser(User user) {
        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(incomeService.getAllIncomesByUser(user));
        transactions.addAll(expenseService.getAllExpensesByUser(user));
        return transactions;
    }

    public List<Transaction> getTransactionsByDateRange(User user, LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = getAllTransactionsByUser(user);
        return transactions.stream()
                .filter(t -> !t.getDate().isBefore(startDate) && !t.getDate().isAfter(endDate))
                .toList();
    }

    public List<Income> getIncomesByDateRange(User user, LocalDate startDate, LocalDate endDate) {
        return incomeService.getAllIncomesByUser(user).stream()
                .filter(income -> !income.getDate().isBefore(startDate) && !income.getDate().isAfter(endDate))
                .toList();
    }

    public List<Expense> getExpensesByDateRange(User user, LocalDate startDate, LocalDate endDate) {
        return expenseService.getAllExpensesByUser(user).stream()
                .filter(expense -> !expense.getDate().isBefore(startDate) && !expense.getDate().isAfter(endDate))
                .toList();
    }

    public Double getTotalIncomeByDateRange(User user, LocalDate startDate, LocalDate endDate) {
        return getIncomesByDateRange(user, startDate, endDate).stream()
                .mapToDouble(Income::getAmount)
                .sum();
    }

    public Double getTotalExpenseByDateRange(User user, LocalDate startDate, LocalDate endDate) {
        return getExpensesByDateRange(user, startDate, endDate).stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }
}