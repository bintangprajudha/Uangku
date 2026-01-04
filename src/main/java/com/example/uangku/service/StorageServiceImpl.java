package com.example.uangku.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.uangku.model.Expense;
import com.example.uangku.model.Income;
import com.example.uangku.model.Transaction;
import com.example.uangku.repository.ExpenseRepository;
import com.example.uangku.repository.IncomeRepository;

@Service
public class StorageServiceImpl implements StorageService {

    private final IncomeRepository incomeRepo;
    private final ExpenseRepository expenseRepo;

    public StorageServiceImpl(IncomeRepository incomeRepo, ExpenseRepository expenseRepo) {
        this.incomeRepo = incomeRepo;
        this.expenseRepo = expenseRepo;
    }

    @Override
    public Transaction save(Transaction transaction) {
        if (transaction instanceof Income) {
            return incomeRepo.save((Income) transaction);
        } else if (transaction instanceof Expense) {
            return expenseRepo.save((Expense) transaction);
        }
        throw new IllegalArgumentException("Unsupported transaction type");
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        Optional<Income> income = incomeRepo.findById(id);
        if (income.isPresent()) {
            return Optional.of(income.get());
        }
        Optional<Expense> expense = expenseRepo.findById(id);
        if (expense.isPresent()) {
            return Optional.of(expense.get());
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {
        if (incomeRepo.existsById(id)) {
            incomeRepo.deleteById(id);
        } else if (expenseRepo.existsById(id)) {
            expenseRepo.deleteById(id);
        }
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> all = new ArrayList<>();
        all.addAll(incomeRepo.findAll());
        all.addAll(expenseRepo.findAll());
        return all;
    }
}
