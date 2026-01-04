package com.example.uangku.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Data
@NoArgsConstructor
public abstract class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @ManyToOne
    private Category category;

    @ManyToOne
    private User user;

    private LocalDate date;
    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor
    public Transaction(Double amount, Category category, LocalDate date, String notes) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.notes = notes;
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public abstract String getType();

    public double getAmount() {
        return amount != null ? amount : 0.0;
    }
}
