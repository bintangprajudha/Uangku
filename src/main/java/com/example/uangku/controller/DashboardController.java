package com.example.uangku.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.uangku.dto.CategoryRequestDTO;

import com.example.uangku.model.Expense;
import com.example.uangku.model.Income;
import com.example.uangku.service.CategoryService;
import com.example.uangku.service.Dashboard;
import com.example.uangku.service.ExpenseService;
import com.example.uangku.service.IncomeService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final Dashboard dashboard;
    private final CategoryService categoryService;
    private final IncomeService incomeService;
    private final ExpenseService expenseService;

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
        } catch (Exception e) {
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
        } catch (Exception e) {
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
    public String addCategory(@RequestParam String name, @RequestParam String type, RedirectAttributes redirectAttributes) {
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
    public String updateCategory(@RequestParam Long id, @RequestParam String name, @RequestParam String type, RedirectAttributes redirectAttributes) {
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
}