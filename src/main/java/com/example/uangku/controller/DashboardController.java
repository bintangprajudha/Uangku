package com.example.uangku.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.uangku.model.Expense;
import com.example.uangku.model.Income;
import com.example.uangku.service.CategoryService;
import com.example.uangku.service.ExpenseService;
import com.example.uangku.service.IncomeService;
import com.example.uangku.service.StatisticCalculatorService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final StatisticCalculatorService statisticService;
    private final CategoryService categoryService;
    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("totalIncome", statisticService.getTotalIncome());
        model.addAttribute("totalExpense", statisticService.getTotalExpense());
        model.addAttribute("balance", statisticService.getBalance());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "dashboard";
    }

    @PostMapping("/income/add")
    public String addIncome(@RequestParam Double amount,
            @RequestParam Long categoryId,
            @RequestParam LocalDate date,
            @RequestParam(required = false) String notes,
            RedirectAttributes redirectAttributes) {
        try {
            Income income = new Income();
            income.setAmount(amount);
            income.setCategory(categoryService.getCategoryById(categoryId));
            income.setDate(date);
            income.setNotes(notes);
            income.setCreatedAt(LocalDateTime.now());

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
            RedirectAttributes redirectAttributes) {
        try {
            Expense expense = new Expense();
            expense.setAmount(amount);
            expense.setCategory(categoryService.getCategoryById(categoryId));
            expense.setDate(date);
            expense.setNotes(notes);
            expense.setCreatedAt(LocalDateTime.now());

            expenseService.addExpense(expense);
            redirectAttributes.addFlashAttribute("success", "Expense added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add expense: " + e.getMessage());
        }
        return "redirect:/";
    }
}