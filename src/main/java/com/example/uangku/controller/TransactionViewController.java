package com.example.uangku.controller;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.uangku.model.Transaction;
import com.example.uangku.model.User;
import com.example.uangku.service.CategoryService;
import com.example.uangku.service.TransactionManagerService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TransactionViewController {
    
    private final TransactionManagerService transactionManagerService;
    private final CategoryService categoryService;

    @GetMapping("/transactions/filter")
    public String showTransactions(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/auth/login";
        
        // Get filtered transactions
        List<Transaction> transactions;
        if (search != null || type != null || categoryId != null || startDate != null || endDate != null) {
            transactions = transactionManagerService.searchAndFilterTransactions(
                    user, search, type, categoryId, startDate, endDate);
        } else {
            transactions = transactionManagerService.getAllTransactionsByUser(user);
            transactions.sort(Comparator.comparing(Transaction::getDate).reversed());
        }
        
        model.addAttribute("transactions", transactions);
        model.addAttribute("categories", categoryService.getAllCategoriesSorted());
        model.addAttribute("search", search);
        model.addAttribute("type", type);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        
        return "transactions";
    }
}