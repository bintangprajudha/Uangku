package com.example.uangku.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.uangku.model.Expense;
import com.example.uangku.model.User;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUser(User user);

    // raska added
    List<Expense> findByUserId(Long userId);

    boolean existsByCategory_Id(Long categoryId);
}
