package com.example.uangku.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.uangku.model.Category;
import com.example.uangku.model.Transaction;
import com.example.uangku.model.TransactionType;

public class TransactionManager {

    private List<Transaction> transactions;
    private StorageService storage;

    public TransactionManager(StorageService storage) {
        this.storage = storage;
        this.transactions = new ArrayList<>(storage.findAll());
    }

    public Transaction addTransaction(Transaction transaction) {
        Transaction saved = storage.save(transaction);
        transactions.add(saved);
        return saved;
    }

    public Transaction updateTransaction(String id, Transaction data) {
        Long transactionId = Long.parseLong(id);
        Optional<Transaction> existingOpt = storage.findById(transactionId);
        if (existingOpt.isPresent()) {
            Transaction existing = existingOpt.get();
            // Update fields
            existing.setAmount(data.getAmount());
            existing.setCategory(data.getCategory());
            existing.setDate(data.getDate());
            existing.setNotes(data.getNotes());
            Transaction updated = storage.save(existing);
            // Update in list
            transactions.removeIf(t -> t.getId().equals(transactionId));
            transactions.add(updated);
            return updated;
        }
        return null;
    }

    public boolean deleteTransaction(String id) {
        Long transactionId = Long.parseLong(id);
        boolean deleted = false;
        try {
            storage.deleteById(transactionId);
            transactions.removeIf(t -> t.getId().equals(transactionId));
            deleted = true;
        } catch (Exception e) {
            // Handle exception if needed
        }
        return deleted;
    }

    public Transaction getTransactionById(String id) {
        Long transactionId = Long.parseLong(id);
        return storage.findById(transactionId).orElse(null);
    }

    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    public List<Transaction> filterByDate(LocalDate startDate, LocalDate endDate) {
        return transactions.stream()
                .filter(t -> !t.getDate().isBefore(startDate) && !t.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    public List<Transaction> filterByCategory(Category category) {
        return transactions.stream()
                .filter(t -> t.getCategory() != null && t.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    public List<Transaction> filterByType(TransactionType type) {
        String typeStr = type.name();
        return transactions.stream()
                .filter(t -> t.getType().equalsIgnoreCase(typeStr))
                .collect(Collectors.toList());
    }

    public List<Transaction> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllTransactions();
        }
        String lowerKeyword = keyword.toLowerCase();
        return transactions.stream()
                .filter(t -> (t.getNotes() != null && t.getNotes().toLowerCase().contains(lowerKeyword)) ||
                             (t.getCategory() != null && t.getCategory().getName() != null && t.getCategory().getName().toLowerCase().contains(lowerKeyword)))
                .collect(Collectors.toList());
    }
}
