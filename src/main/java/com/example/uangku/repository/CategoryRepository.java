package com.example.uangku.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.uangku.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Ambil kategori berdasarkan type (income / expense)
    List<Category> findByType(String type);

    // Cek apakah category dengan nama & type tertentu sudah ada
    boolean existsByNameAndType(String name, String type);
}
