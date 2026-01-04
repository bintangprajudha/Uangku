package com.example.uangku.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.uangku.model.Category;
import com.example.uangku.model.Transaction;
import com.example.uangku.model.TransactionType;

public class HistoryManager {

    private TransactionManager transactionManager;
    private int pageSize;

    public HistoryManager(TransactionManager transactionManager, int pageSize) {
        this.transactionManager = transactionManager;
        this.pageSize = pageSize;
    }

    public Object getHistory(int page, Map<String, Object> filters) {
        List<Transaction> filtered = getFilteredHistory(filters);
        int total = filtered.size();
        int start = page * pageSize;
        int end = Math.min(start + pageSize, total);
        List<Transaction> pageData = filtered.subList(start, end);

        Map<String, Object> result = new HashMap<>();
        result.put("transactions", pageData);
        result.put("currentPage", page);
        result.put("totalPages", (int) Math.ceil((double) total / pageSize));
        result.put("totalItems", total);
        result.put("pageSize", pageSize);

        return result;
    }

    public List<Transaction> getFilteredHistory(Map<String, Object> filters) {
        List<Transaction> transactions = transactionManager.getAllTransactions();

        if (filters != null) {
            // Filter by date range
            if (filters.containsKey("startDate") && filters.containsKey("endDate")) {
                LocalDate startDate = (LocalDate) filters.get("startDate");
                LocalDate endDate = (LocalDate) filters.get("endDate");
                transactions = transactionManager.filterByDate(startDate, endDate);
            }

            // Filter by category
            if (filters.containsKey("category")) {
                Category category = (Category) filters.get("category");
                transactions = transactionManager.filterByCategory(category);
            }

            // Filter by type
            if (filters.containsKey("type")) {
                TransactionType type = (TransactionType) filters.get("type");
                transactions = transactionManager.filterByType(type);
            }

            // Search by keyword
            if (filters.containsKey("keyword")) {
                String keyword = (String) filters.get("keyword");
                transactions = transactionManager.search(keyword);
            }
        }

        return transactions;
    }

    public List<Transaction> sortHistory(List<Transaction> transactions, String sortBy, String order) {
        Comparator<Transaction> comparator;

        switch (sortBy.toLowerCase()) {
            case "date":
                comparator = Comparator.comparing(Transaction::getDate);
                break;
            case "amount":
                comparator = Comparator.comparing(Transaction::getAmount);
                break;
            case "type":
                comparator = Comparator.comparing(Transaction::getType);
                break;
            default:
                comparator = Comparator.comparing(Transaction::getDate);
        }

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        return transactions.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public String exportHistory(String format) {
        List<Transaction> transactions = transactionManager.getAllTransactions();

        switch (format.toLowerCase()) {
            case "csv":
                return exportToCSV(transactions);
            case "json":
                return exportToJSON(transactions);
            default:
                return "Unsupported format";
        }
    }

    private String exportToCSV(List<Transaction> transactions) {
        StringBuilder csv = new StringBuilder();
        csv.append("ID,Amount,Category,Date,Notes,Type\n");

        for (Transaction t : transactions) {
            csv.append(t.getId()).append(",")
               .append(t.getAmount()).append(",")
               .append(t.getCategory() != null ? t.getCategory().getName() : "").append(",")
               .append(t.getDate()).append(",")
               .append(t.getNotes() != null ? t.getNotes() : "").append(",")
               .append(t.getType()).append("\n");
        }

        return csv.toString();
    }

    private String exportToJSON(List<Transaction> transactions) {
        StringBuilder json = new StringBuilder();
        json.append("[\n");

        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            json.append("  {\n")
                .append("    \"id\": ").append(t.getId()).append(",\n")
                .append("    \"amount\": ").append(t.getAmount()).append(",\n")
                .append("    \"category\": \"").append(t.getCategory() != null ? t.getCategory().getName() : "").append("\",\n")
                .append("    \"date\": \"").append(t.getDate()).append("\",\n")
                .append("    \"notes\": \"").append(t.getNotes() != null ? t.getNotes() : "").append("\",\n")
                .append("    \"type\": \"").append(t.getType()).append("\"\n")
                .append("  }");

            if (i < transactions.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }

        json.append("]");
        return json.toString();
    }
}
