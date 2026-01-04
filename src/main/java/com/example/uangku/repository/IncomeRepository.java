package com.example.uangku.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.uangku.model.Income;
import com.example.uangku.model.User;

public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findByUser(User user);

    // raska added
    List<Income> findByUserId(Long userId);

    boolean existsByCategory_Id(Long categoryId);
}
