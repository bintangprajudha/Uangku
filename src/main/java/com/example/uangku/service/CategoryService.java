package com.example.uangku.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.uangku.model.Category;
import com.example.uangku.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepo;

    public Category addCategory(Category c) {
        c.setCreatedAt(LocalDateTime.now());
        return categoryRepo.save(c);
    }

    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }
}
