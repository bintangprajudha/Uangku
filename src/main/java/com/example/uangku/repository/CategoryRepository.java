package com.example.uangku.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.uangku.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByType(String type);

    boolean existsByNameAndType(String name, String type);
}
