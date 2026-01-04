package com.example.uangku.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.uangku.dto.CategoryRequestDTO;
import com.example.uangku.dto.CategoryStatsDTO;
import com.example.uangku.dto.QuickStatsDTO;
import com.example.uangku.exception.InvalidTransactionException;
import com.example.uangku.model.Expense;
import com.example.uangku.model.Income;
import com.example.uangku.service.CategoryService;
import com.example.uangku.service.Dashboard;
import com.example.uangku.service.ExpenseService;
import com.example.uangku.service.IncomeService;
import com.example.uangku.service.QuickStatsService;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final Dashboard dashboard;
    private final CategoryService categoryService;
    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    // raska added
    private final QuickStatsService quickStatsService;
    // raska added end

    @GetMapping("/")
    public String dashboard(Model model, HttpSession session) {
        com.example.uangku.model.User user = (com.example.uangku.model.User) session.getAttribute("user");
        if (user == null)
            return "redirect:/auth/login";
        model.addAttribute("totalIncome", incomeService.getTotalIncomeByUser(user));
        model.addAttribute("totalExpense", expenseService.getTotalExpenseByUser(user));
        model.addAttribute("balance",
                incomeService.getTotalIncomeByUser(user) - expenseService.getTotalExpenseByUser(user));
        Map<String, Object> currentMonthSummary = dashboard.getCurrentMonthSummary(user);
        model.addAttribute("currentMonthSummary", currentMonthSummary);
        model.addAttribute("categories", categoryService.getAllCategoriesSorted());
        // raska added
        QuickStatsDTO quickStats = quickStatsService.getQuickStats(user.getId());
        model.addAttribute("quickStats", quickStats);

        List<CategoryStatsDTO> categoryBreakdown = quickStatsService.getCategoryBreakdown(user.getId());
        model.addAttribute("categoryBreakdown", categoryBreakdown);
        // raska added end

        // Get recent transactions (limit to 5 most recent)
        List<Map<String, Object>> recentTransactions = new ArrayList<>();
        List<com.example.uangku.model.Income> incomes = incomeService.getAllIncomesByUser(user);
        List<com.example.uangku.model.Expense> expenses = expenseService.getAllExpensesByUser(user);

        // Convert incomes to map
        for (com.example.uangku.model.Income income : incomes) {
            Map<String, Object> transactionMap = new java.util.HashMap<>();
            transactionMap.put("id", income.getId());
            transactionMap.put("amount", income.getAmount());
            transactionMap.put("category", income.getCategory());
            transactionMap.put("date", income.getDate());
            transactionMap.put("notes", income.getNotes());
            transactionMap.put("type", "Income");
            transactionMap.put("createdAt", income.getCreatedAt());
            recentTransactions.add(transactionMap);
        }

        // Convert expenses to map
        for (com.example.uangku.model.Expense expense : expenses) {
            Map<String, Object> transactionMap = new java.util.HashMap<>();
            transactionMap.put("id", expense.getId());
            transactionMap.put("amount", expense.getAmount());
            transactionMap.put("category", expense.getCategory());
            transactionMap.put("date", expense.getDate());
            transactionMap.put("notes", expense.getNotes());
            transactionMap.put("type", "Expense");
            transactionMap.put("createdAt", expense.getCreatedAt());
            recentTransactions.add(transactionMap);
        }

        // Sort by createdAt descending (most recent first), handling null dates
        recentTransactions.sort((t1, t2) -> {
            LocalDateTime createdAt1 = (LocalDateTime) t1.get("createdAt");
            LocalDateTime createdAt2 = (LocalDateTime) t2.get("createdAt");
            if (createdAt1 == null && createdAt2 == null)
                return 0;
            if (createdAt1 == null)
                return 1;
            if (createdAt2 == null)
                return -1;
            return createdAt2.compareTo(createdAt1);
        });

        // Limit to 5 most recent
        if (recentTransactions.size() > 5) {
            recentTransactions = recentTransactions.subList(0, 5);
        }
        model.addAttribute("recentTransactions", recentTransactions);

        return "dashboard";
    }

    @PostMapping("/income/add")
    public String addIncome(@RequestParam Double amount,
            @RequestParam Long categoryId,
            @RequestParam LocalDate date,
            @RequestParam(required = false) String notes,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            com.example.uangku.model.User user = (com.example.uangku.model.User) session.getAttribute("user");
            if (user == null)
                return "redirect:/auth/login";
            Income income = new Income();
            income.setAmount(amount);
            income.setCategory(categoryService.getCategoryById(categoryId));
            income.setDate(date);
            income.setNotes(notes);
            income.setCreatedAt(LocalDateTime.now());
            income.setUser(user);
            incomeService.addIncome(income);
            redirectAttributes.addFlashAttribute("success", "Income added successfully!");
        } catch (InvalidTransactionException e) {
            // Handle validation errors
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            // Handle unexpected errors
            redirectAttributes.addFlashAttribute("error", "Failed to add income: " + e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/expense/add")
    public String addExpense(@RequestParam Double amount,
            @RequestParam Long categoryId,
            @RequestParam LocalDate date,
            @RequestParam(required = false) String notes,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            com.example.uangku.model.User user = (com.example.uangku.model.User) session.getAttribute("user");
            if (user == null)
                return "redirect:/auth/login";
            Expense expense = new Expense();
            expense.setAmount(amount);
            expense.setCategory(categoryService.getCategoryById(categoryId));
            expense.setDate(date);
            expense.setNotes(notes);
            expense.setCreatedAt(LocalDateTime.now());
            expense.setUser(user);
            expenseService.addExpense(expense);
            redirectAttributes.addFlashAttribute("success", "Expense added successfully!");
        } catch (InvalidTransactionException e) {
            // Handle validation errors
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            // Handle unexpected errors
            redirectAttributes.addFlashAttribute("error", "Failed to add expense: " + e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/categories")
    public String categories(Model model, HttpSession session) {
        com.example.uangku.model.User user = (com.example.uangku.model.User) session.getAttribute("user");
        if (user == null)
            return "redirect:/auth/login";
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categories"; // Buat file categories.html di templates
    }

    @PostMapping("/categories/add")
    public String addCategory(@RequestParam String name, @RequestParam String type,
            RedirectAttributes redirectAttributes) {
        try {
            CategoryRequestDTO dto = new CategoryRequestDTO();
            dto.setName(name);
            dto.setType(type);

            categoryService.addCategory(dto);

            redirectAttributes.addFlashAttribute("success", "Category added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add category: " + e.getMessage());
        }
        return "redirect:/categories";
    }

    @PostMapping("/category/update")
    public String updateCategory(@RequestParam Long id, @RequestParam String name, @RequestParam String type,
            RedirectAttributes redirectAttributes) {
        try {
            CategoryRequestDTO dto = new CategoryRequestDTO();
            dto.setName(name);
            dto.setType(type);

            categoryService.updateCategory(id, dto);

            redirectAttributes.addFlashAttribute("success", "Category updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update category: " + e.getMessage());
        }
        return "redirect:/categories";
    }

    @PostMapping("/category/delete")
    public String deleteCategory(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("success", "Category deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete category: " + e.getMessage());
        }
        return "redirect:/categories";
    }

    @GetMapping("/transactions")
    public String transactions(Model model, HttpSession session) {
        com.example.uangku.model.User user = (com.example.uangku.model.User) session.getAttribute("user");
        if (user == null)
            return "redirect:/auth/login";
        List<Map<String, Object>> transactions = new ArrayList<>();
        List<com.example.uangku.model.Income> incomes = incomeService.getAllIncomesByUser(user);
        List<com.example.uangku.model.Expense> expenses = expenseService.getAllExpensesByUser(user);

        // Convert incomes to map
        for (com.example.uangku.model.Income income : incomes) {
            Map<String, Object> transactionMap = new java.util.HashMap<>();
            transactionMap.put("id", income.getId());
            transactionMap.put("amount", income.getAmount());
            transactionMap.put("category", income.getCategory());
            transactionMap.put("date", income.getDate());
            transactionMap.put("notes", income.getNotes());
            transactionMap.put("type", "Income");
            transactions.add(transactionMap);
        }

        // Convert expenses to map
        for (com.example.uangku.model.Expense expense : expenses) {
            Map<String, Object> transactionMap = new java.util.HashMap<>();
            transactionMap.put("id", expense.getId());
            transactionMap.put("amount", expense.getAmount());
            transactionMap.put("category", expense.getCategory());
            transactionMap.put("date", expense.getDate());
            transactionMap.put("notes", expense.getNotes());
            transactionMap.put("type", "Expense");
            transactions.add(transactionMap);
        }

        // Sort by date descending, handling null dates
        transactions.sort((t1, t2) -> {
            LocalDate date1 = (LocalDate) t1.get("date");
            LocalDate date2 = (LocalDate) t2.get("date");
            if (date1 == null && date2 == null)
                return 0;
            if (date1 == null)
                return 1;
            if (date2 == null)
                return -1;
            return date2.compareTo(date1);
        });
        model.addAttribute("transactions", transactions);
        model.addAttribute("categories", categoryService.getAllCategoriesSorted());
        return "transactions";
    }

    @PostMapping("/transaction/delete/{id}")
    public String deleteTransaction(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            com.example.uangku.model.User user = (com.example.uangku.model.User) session.getAttribute("user");
            if (user == null)
                return "redirect:/auth/login";

            // Try to delete as Income first
            Income income = incomeService.getIncomeById(id);
            if (income != null && income.getUser().getId().equals(user.getId())) {
                incomeService.deleteIncome(id);
                redirectAttributes.addFlashAttribute("success", "Income deleted successfully!");
                return "redirect:/transactions";
            }

            // Try to delete as Expense
            Expense expense = expenseService.getExpenseById(id);
            if (expense != null && expense.getUser().getId().equals(user.getId())) {
                expenseService.deleteExpense(id);
                redirectAttributes.addFlashAttribute("success", "Expense deleted successfully!");
                return "redirect:/transactions";
            }

            redirectAttributes.addFlashAttribute("error", "Transaction not found or access denied.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete transaction: " + e.getMessage());
        }
        return "redirect:/transactions";
    }


}
