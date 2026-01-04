package com.example.uangku.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;

import com.example.uangku.dto.CategoryRequestDTO;
import com.example.uangku.model.Category;
import com.example.uangku.repository.CategoryRepository;
import com.example.uangku.repository.ExpenseRepository;
import com.example.uangku.repository.IncomeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepo;
    private final ExpenseRepository expenseRepo;
    private final IncomeRepository incomeRepo;

    // addCategory 
    public Category addCategory(CategoryRequestDTO dto) {
        String name = dto.getName() == null ? null : dto.getName().trim();
        String type = dto.getType() == null ? null : dto.getType().trim().toLowerCase();
        if (name == null || name.isEmpty() || type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Category name and type must be provided");
        }
        if (categoryRepo.existsByNameAndType(name, type)) {
            throw new IllegalArgumentException("Category with name '" + name + "' and type '" + type + "' already exists");
        }
        Category c = new Category(
                name,
                type,
                dto.getIcon(),
                dto.getColor()
        );
        return categoryRepo.save(c);
    }

    // getAllCategories 
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    // get all categories sorted by type then name for consistent display
    public List<Category> getAllCategoriesSorted() {
        return categoryRepo.findAll(Sort.by("type").ascending().and(Sort.by("name").ascending()));
    }

    // get categories by type (income / expense)
    public List<Category> getCategoriesByType(String type) {
        return categoryRepo.findByType(type);
    }

    // getCategoryById 
    public Category getCategoryById(Long id) {
        return categoryRepo.findById(id).orElse(null);
    }

    // updateCategory 
    public Category updateCategory(Long id, CategoryRequestDTO dto) {
        Category c = categoryRepo.findById(id).orElse(null);
        if (c == null) return null;
        String name = dto.getName() == null ? c.getName() : dto.getName().trim();
        String type = dto.getType() == null ? c.getType() : dto.getType().trim().toLowerCase();
        c.setName(name);
        c.setType(type);
        c.setIcon(dto.getIcon());
        c.setColor(dto.getColor());
        c.setUpdatedAt(LocalDateTime.now());

        return categoryRepo.save(c);
    }

    // deleteCategory 
    public boolean deleteCategory(Long id) {
        if (!categoryRepo.existsById(id)) return false;
        // jika ada expense atau income yang merujuk ke kategori ini, tolak penghapusan
        if (expenseRepo.existsByCategory_Id(id) || incomeRepo.existsByCategory_Id(id)) {
            throw new IllegalStateException("Cannot delete category: it is referenced by existing transactions");
        }
        categoryRepo.deleteById(id);
        return true;
    }
}
