package com.example.uangku.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.uangku.dto.SearchCriteriaDTO;
import com.example.uangku.model.Transaction;
import com.example.uangku.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchEngine {
    private final TransactionManagerService transactionManager;

    public Transaction[] search(User user, String keyword) {
        // Get all transactions
        List<Transaction> transactions = transactionManager.getAllTransactionsByUser(user);
        
        // If no keyword or empty, return all
        if (keyword == null || keyword.trim().isEmpty()) {
            return transactions.toArray(new Transaction[0]);
        }

        // Search in notes and category name
        String search = keyword.toLowerCase().trim();
        
        List<Transaction> results = transactions.stream()
                .filter(t -> {
                    // Check notes
                    boolean matchNotes = t.getNotes() != null && 
                                        t.getNotes().toLowerCase().contains(search);
                    
                    // Check category name
                    boolean matchCategory = t.getCategory() != null && 
                                           t.getCategory().getName() != null &&
                                           t.getCategory().getName().toLowerCase().contains(search);
                    
                    return matchNotes || matchCategory;
                })
                .toList();
        
        return results.toArray(new Transaction[0]);
    }

    public Transaction[] advancedSearch(SearchCriteriaDTO criteria) {
        if (criteria == null || criteria.getUser() == null) {
            return new Transaction[0];
        }

        // Start with all transactions
        List<Transaction> transactions = new ArrayList<>(
            transactionManager.getAllTransactionsByUser(criteria.getUser())
        );

        // Apply date range filter
        if (criteria.getStartDate() != null && criteria.getEndDate() != null) {
            List<Transaction> filtered = transactionManager.filterByDateRange(
                transactions, criteria.getStartDate(), criteria.getEndDate());
            transactions = new ArrayList<>(filtered);
        }

        // Apply type filter
        if (criteria.getType() != null && !criteria.getType().isEmpty() && !criteria.getType().equalsIgnoreCase("all")) {
            List<Transaction> filtered = transactionManager.filterByType(transactions, criteria.getType());
            transactions = new ArrayList<>(filtered);
        }

        // Apply category filter
        if (criteria.getCategoryId() != null && criteria.getCategoryId() > 0) {
            List<Transaction> filtered = transactionManager.filterByCategory(transactions, criteria.getCategoryId());
            transactions = new ArrayList<>(filtered);
        }

        // Apply keyword search (notes and category)
        if (criteria.getKeyword() != null && !criteria.getKeyword().trim().isEmpty()) {
            String search = criteria.getKeyword().toLowerCase().trim();
            List<Transaction> filtered = transactions.stream()
                .filter(t -> {
                    boolean matchNotes = t.getNotes() != null && 
                                        t.getNotes().toLowerCase().contains(search);
                    
                    boolean matchCategory = t.getCategory() != null && 
                                           t.getCategory().getName() != null &&
                                           t.getCategory().getName().toLowerCase().contains(search);
                    
                    return matchNotes || matchCategory;
                })
                .toList();
            transactions = new ArrayList<>(filtered); 
        }

        // Apply amount range filter
        if (criteria.getMinAmount() != null || criteria.getMaxAmount() != null) {
            List<Transaction> filtered = searchByAmountList(transactions, criteria.getMinAmount(), criteria.getMaxAmount());
            transactions = new ArrayList<>(filtered);
        }

        // Sort by date descending (newest first)
        if (!transactions.isEmpty()) {
            transactions.sort(Comparator.comparing(Transaction::getDate).reversed());
        }

        return transactions.toArray(new Transaction[0]);
    }

    public Transaction[] searchByAmount(User user, Double min, Double max) {
        List<Transaction> transactions = transactionManager.getAllTransactionsByUser(user);
        List<Transaction> results = searchByAmountList(transactions, min, max);
        return results.toArray(new Transaction[0]);
    }

    private List<Transaction> searchByAmountList(List<Transaction> transactions, Double min, Double max) {
        return transactions.stream()
                .filter(t -> {
                    double amount = t.getAmount();
                    boolean minCheck = min == null || amount >= min;
                    boolean maxCheck = max == null || amount <= max;
                    return minCheck && maxCheck;
                })
                .toList();
    }

    public Transaction[] searchByNotes(User user, String keyword) {
        List<Transaction> transactions = transactionManager.getAllTransactionsByUser(user);
        
        if (keyword == null || keyword.trim().isEmpty()) {
            return transactions.toArray(new Transaction[0]);
        }

        String search = keyword.toLowerCase().trim();
        
        List<Transaction> results = transactions.stream()
                .filter(t -> t.getNotes() != null && 
                            t.getNotes().toLowerCase().contains(search))
                .toList();
        
        return results.toArray(new Transaction[0]);
    }

    public Transaction[] sortByDateDescending(Transaction[] transactions) {
        if (transactions == null || transactions.length == 0) {
            return new Transaction[0];
        }
        
        List<Transaction> list = new ArrayList<>(List.of(transactions));
        list.sort(Comparator.comparing(Transaction::getDate).reversed());
        return list.toArray(new Transaction[0]);
    }

    public Transaction[] sortByAmountDescending(Transaction[] transactions) {
        if (transactions == null || transactions.length == 0) {
            return new Transaction[0];
        }
        
        List<Transaction> list = new ArrayList<>(List.of(transactions));
        list.sort(Comparator.comparing(Transaction::getAmount).reversed());
        return list.toArray(new Transaction[0]);
    }
}
