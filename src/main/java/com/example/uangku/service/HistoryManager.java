package com.example.uangku.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.uangku.model.Transaction;
import com.example.uangku.model.User;

@Service
public class HistoryManager {

    private TransactionManagerService transactionManagerService;
    private int pageSize;

    public HistoryManager(TransactionManagerService transactionManagerService, @Value("${history.page.size:10}") int pageSize) {
        this.transactionManagerService = transactionManagerService;
        this.pageSize = pageSize;
    }

 

    public Object getHistory(int page, Map<String, Object> filters) {
        try {
            List<Transaction> allTransactions = getFilteredHistory(filters);

            int totalItems = allTransactions.size();
            int totalPages = (int) Math.ceil((double) totalItems / pageSize);
            int startIndex = (page - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, totalItems);

            List<Transaction> pageTransactions = allTransactions.subList(startIndex, endIndex);

            Map<String, Object> result = new HashMap<>();
            result.put("transactions", pageTransactions);
            result.put("currentPage", page);
            result.put("totalPages", totalPages);
            result.put("totalItems", totalItems);
            result.put("pageSize", pageSize);

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Error retrieving history: " + e.getMessage());
        }
    }

  
    public List<Transaction> getFilteredHistory(Map<String, Object> filters) {
        try {
            User user = (User) filters.get("user");
            if (user == null) {
                throw new IllegalArgumentException("User is required for filtering");
            }

            List<Transaction> transactions = transactionManagerService.getAllTransactionsByUser(user);


            String type = (String) filters.get("type");
            if (type != null) {
                transactions = transactionManagerService.filterByType(transactions, type);
            }

            Long categoryId = (Long) filters.get("categoryId");
            if (categoryId != null) {
                transactions = transactionManagerService.filterByCategory(transactions, categoryId);
            }

            LocalDate startDate = (LocalDate) filters.get("startDate");
            LocalDate endDate = (LocalDate) filters.get("endDate");
            if (startDate != null && endDate != null) {
                transactions = transactionManagerService.filterByDateRange(transactions, startDate, endDate);
            }

            return transactions;

        } catch (Exception e) {
            throw new RuntimeException("Error filtering history: " + e.getMessage());
        }
    }

 
    public List<Transaction> sortHistory(List<Transaction> transactions, String sortBy, String order) {
        try {
            Comparator<Transaction> comparator;

            switch (sortBy.toLowerCase()) {
                case "date":
                    comparator = Comparator.comparing(Transaction::getDate);
                    break;
                case "amount":
                    comparator = Comparator.comparing(Transaction::getAmount);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid sortBy field: " + sortBy);
            }

            if ("desc".equalsIgnoreCase(order)) {
                comparator = comparator.reversed();
            }

            return transactions.stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Error sorting history: " + e.getMessage());
        }
    }

    public String exportHistory(String format) {
        try {

            List<Transaction> transactions = transactionManagerService.getAllTransactions();

            switch (format.toLowerCase()) {
                case "json":
                    return exportToJson(transactions);
                case "csv":
                    return exportToCsv(transactions);
                default:
                    throw new IllegalArgumentException("Unsupported format: " + format);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error exporting history: " + e.getMessage());
        }
    }

    private String exportToJson(List<Transaction> transactions) {
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            json.append("  {\n");
            json.append("    \"id\": ").append(t.getId()).append(",\n");
            json.append("    \"type\": \"").append(t.getType()).append("\",\n");
            json.append("    \"amount\": ").append(t.getAmount()).append(",\n");
            json.append("    \"date\": \"").append(t.getDate()).append("\",\n");
            json.append("    \"notes\": \"").append(t.getNotes() != null ? t.getNotes() : "").append("\"\n");
            json.append("  }");
            if (i < transactions.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("]");
        return json.toString();
    }

    private String exportToCsv(List<Transaction> transactions) {
        StringBuilder csv = new StringBuilder();
        csv.append("ID,Type,Amount,Date,Notes\n");
        for (Transaction t : transactions) {
            csv.append(t.getId()).append(",");
            csv.append(t.getType()).append(",");
            csv.append(t.getAmount()).append(",");
            csv.append(t.getDate()).append(",");
            csv.append("\"").append(t.getNotes() != null ? t.getNotes().replace("\"", "\"\"") : "").append("\"\n");
        }
        return csv.toString();
    }
}
