package com.example.uangku.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.uangku.exception.InvalidTransactionException;
import com.example.uangku.model.Category;
import com.example.uangku.model.Expense;
import com.example.uangku.model.Income;
import com.example.uangku.model.Transaction;
import com.example.uangku.model.TransactionType;
import com.example.uangku.model.User;
import com.example.uangku.service.StorageService;

@Service
public class TransactionManagerService implements IFilterable {

    private List<Transaction> transactions;
    private StorageService storage;
    private IncomeService incomeService;
    private ExpenseService expenseService;

    @Autowired
    public TransactionManagerService(StorageService storage, IncomeService incomeService, ExpenseService expenseService) {
        this.storage = storage;
        this.incomeService = incomeService;
        this.expenseService = expenseService;
        this.transactions = new ArrayList<>();
    }

    // Static method for validation (Static dan Collection)
    public static boolean isValidTransaction(Transaction t) {
        return t != null && t.getAmount() > 0 && t.getDate() != null;
    }

    public List<Transaction> getAllTransactionsByUser(User user) {
        if (user == null) {
            throw new InvalidTransactionException("User cannot be null");
        }
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
    public List<Transaction> filterByDateRange(List<Transaction> transactions, LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return transactions;
        }

        return transactions.stream()
                .filter(t -> !t.getDate().isBefore(startDate) && !t.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    // Overloaded method for polymorphism (Polymorphism)
    public List<Transaction> filterByDateRange(List<Transaction> transactions, LocalDate startDate) {
        return filterByDateRange(transactions, startDate, LocalDate.now());
    }

    public Transaction addTransaction(Transaction transaction) {
        if (!isValidTransaction(transaction)) {
            throw new InvalidTransactionException("Invalid transaction data");
        }
        return storage.save(transaction);
    }

    public Transaction updateTransaction(String id, Transaction data) {
        Long transactionId = Long.parseLong(id);
        Transaction existing = storage.findById(transactionId).orElseThrow(() -> new InvalidTransactionException("Transaction not found"));
        existing.setAmount(data.getAmount());
        existing.setCategory(data.getCategory());
        existing.setDate(data.getDate());
        existing.setNotes(data.getNotes());
        return storage.save(existing);
    }

    public boolean deleteTransaction(String id) {
        Long transactionId = Long.parseLong(id);
        if (storage.findById(transactionId).isPresent()) {
            storage.deleteById(transactionId);
            return true;
        }
        return false;
    }

    public Transaction getTransactionById(String id) {
        Long transactionId = Long.parseLong(id);
        return storage.findById(transactionId).orElseThrow(() -> new InvalidTransactionException("Transaction not found"));
    }

    public List<Transaction> getAllTransactions() {
        return storage.findAll();
    }

    public List<Transaction> filterByDate(LocalDate startDate, LocalDate endDate) {
        return storage.findAll().stream()
                .filter(t -> !t.getDate().isBefore(startDate) && !t.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    public List<Transaction> filterByCategory(Category category) {
        return storage.findAll().stream()
                .filter(t -> t.getCategory() != null && t.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    public List<Transaction> filterByType(TransactionType type) {
        return storage.findAll().stream()
                .filter(t -> t.getType().equals(type.name()))
                .collect(Collectors.toList());
    }

    public List<Transaction> search(String keyword) {
        return storage.findAll().stream()
                .filter(t -> t.getNotes() != null && t.getNotes().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

}
