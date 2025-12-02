package com.example.uangku.config;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.uangku.model.Category;
import com.example.uangku.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepo;

    @Override
    public void run(String... args) throws Exception {
        // Check if categories already exist
        if (categoryRepo.count() > 0) {
            System.out.println("Categories already initialized. Skipping default data.");
            return;
        }

        System.out.println("Initializing default categories...");

        List<Category> defaultCategories = new ArrayList<>();

        // Income Categories
        defaultCategories.add(createCategory("Salary", "income"));
        defaultCategories.add(createCategory("Business", "income"));
        defaultCategories.add(createCategory("Investment", "income"));
        defaultCategories.add(createCategory("Freelance", "income"));
        defaultCategories.add(createCategory("Bonus", "income"));
        defaultCategories.add(createCategory("Gift", "income"));
        defaultCategories.add(createCategory("Other Income", "income"));

        // Expense Categories
        defaultCategories.add(createCategory("Food & Dining", "expense"));
        defaultCategories.add(createCategory("Transport", "expense"));
        defaultCategories.add(createCategory("Utilities", "expense"));
        defaultCategories.add(createCategory("Entertainment", "expense"));
        defaultCategories.add(createCategory("Health", "expense"));
        defaultCategories.add(createCategory("Education", "expense"));
        defaultCategories.add(createCategory("Shopping", "expense"));
        defaultCategories.add(createCategory("Rent", "expense"));
        defaultCategories.add(createCategory("Insurance", "expense"));
        defaultCategories.add(createCategory("Phone & Internet", "expense"));
        defaultCategories.add(createCategory("Other Expense", "expense"));

        categoryRepo.saveAll(defaultCategories);

        System.out.println("Default categories initialized: " + defaultCategories.size() + " categories created.");
    }

    private Category createCategory(String name, String type) {
        Category category = new Category();
        category.setName(name);
        category.setType(type);
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        return category;
    }
}
