package com.example.uangku.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<com.example.uangku.model.Expense, Long> {

}
