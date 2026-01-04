package com.example.uangku.service;

import java.util.List;
import java.util.Optional;

import com.example.uangku.model.Transaction;

public interface StorageService {
    Transaction save(Transaction transaction);
    Optional<Transaction> findById(Long id);
    void deleteById(Long id);
    List<Transaction> findAll();
}
