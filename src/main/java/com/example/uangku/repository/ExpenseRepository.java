package com.example.uangku.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.uangku.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

}
