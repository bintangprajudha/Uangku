package com.example.uangku.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.uangku.dto.SearchCriteriaDTO;
import com.example.uangku.model.Transaction;
import com.example.uangku.model.User;
import com.example.uangku.service.CategoryService;
import com.example.uangku.service.SearchEngine;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TransactionViewController {
    
    private final SearchEngine searchEngine;
    private final CategoryService categoryService;

    @GetMapping("/transactions/filter")
    public String showTransactions(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount,
            Model model,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/auth/login";

        Transaction[] transactionsArray;
        
        boolean hasFilter = (search != null && !search.trim().isEmpty()) || 
                           (type != null && !type.isEmpty()) || 
                           (categoryId != null && categoryId > 0) || 
                           startDate != null || 
                           endDate != null || 
                           minAmount != null || 
                           maxAmount != null;
        
        if (hasFilter) {
            // Use advanced search with criteria
            SearchCriteriaDTO criteria = SearchCriteriaDTO.builder()
                    .user(user)
                    .keyword(search)
                    .type(type)
                    .categoryId(categoryId)
                    .startDate(startDate)
                    .endDate(endDate)
                    .minAmount(minAmount)
                    .maxAmount(maxAmount)
                    .build();
            
            transactionsArray = searchEngine.advancedSearch(criteria);
        } else {
            // No filter - get all transactions sorted by date
            transactionsArray = searchEngine.search(user, "");
            transactionsArray = searchEngine.sortByDateDescending(transactionsArray);
        }
        
        // Convert array to list for Thymeleaf
        List<Transaction> transactions = Arrays.asList(transactionsArray);
        
        // Add attributes to model
        model.addAttribute("transactions", transactions);
        model.addAttribute("categories", categoryService.getAllCategoriesSorted());
        model.addAttribute("search", search);
        model.addAttribute("type", type);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("minAmount", minAmount);
        model.addAttribute("maxAmount", maxAmount);
        
        return "transactions";
    }
}