package com.example.uangku.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.uangku.exception.InvalidTransactionException;
import com.example.uangku.model.Income;
import com.example.uangku.model.User;
import com.example.uangku.repository.IncomeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncomeService {
    private final IncomeRepository incomeRepository;

    public Income addIncome(Income income) {
        // Validasi income
        validateIncome(income);

        income.setCreatedAt(LocalDateTime.now());
        return incomeRepository.save(income);
    }

    /**
     * Validate income before saving
     */
    private void validateIncome(Income income) {
        if (income.getAmount() <= 0) {
            throw new InvalidTransactionException("Income amount must be greater than 0");
        }

        if (income.getCategory() == null) {
            throw new InvalidTransactionException("Category is required for income");
        }

        if (income.getDate() == null) {
            throw new InvalidTransactionException("Date is required for income");
        }

        if (income.getUser() == null) {
            throw new InvalidTransactionException("User is required for income");
        }
    }

    public List<Income> getAllIncomesByUser(User user) {
        return incomeRepository.findByUser(user);
    }

    public Income getIncomeById(Long id) {
        return incomeRepository.findById(id).orElse(null);
    }

    public void deleteIncome(Long id) {
        incomeRepository.deleteById(id);
    }

    public Double getTotalIncomeByUser(User user) {
        return incomeRepository.findByUser(user).stream()
                .mapToDouble(Income::getAmount)
                .sum();
    }
}