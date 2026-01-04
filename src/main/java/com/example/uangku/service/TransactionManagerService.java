package com.example.uangku.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.uangku.model.Expense;
import com.example.uangku.model.Income;
import com.example.uangku.model.Transaction;
import com.example.uangku.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionManagerService implements IFilterable {

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

    @Override
    public List<Transaction> filterByType(List<Transaction> transactions, String type) {
        if (type == null || type.isEmpty() || type.equals("All")) {
            return transactions;
        }

        return transactions.stream()
                .filter(t -> t.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> filterByCategory(List<Transaction> transactions, Long categoryId) {
        if (categoryId == null || categoryId <= 0) {
            return transactions;
        }

        return transactions.stream()
                .filter(t -> t.getCategory() != null && t.getCategory().getId().equals(categoryId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> filterByDateRange(List<Transaction> transactions, 
            LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return transactions;
        }

        return transactions.stream()
                .filter(t -> !t.getDate().isBefore(startDate) && !t.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }
}