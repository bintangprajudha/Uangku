package com.example.uangku.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Expense extends Transaction {

    public Expense(Double amount, Category category, LocalDate date, String notes) {
        super(amount, category, date, notes);
    }

    @Override
    public String getType() {
        return "Expense";
    }
}
