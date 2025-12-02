package com.example.uangku.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.uangku.model.Income;
import com.example.uangku.repository.IncomeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncomeService {
    private final IncomeRepository incomeRepository;

    public Income addIncome(Income income) {
        income.setCreatedAt(LocalDateTime.now());
        return incomeRepository.save(income);
    }

    public List<Income> getAllIncomes() {
        return incomeRepository.findAll();
    }

    public Income getIncomeById(Long id) {
        return incomeRepository.findById(id).orElse(null);
    }

    public void deleteIncome(Long id) {
        incomeRepository.deleteById(id);
    }

    public Double getTotalIncome() {
        return incomeRepository.findAll().stream()
                .mapToDouble(Income::getAmount)
                .sum();
    }
}