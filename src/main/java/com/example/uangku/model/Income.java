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
public class Income extends Transaction {

    public Income(Double amount, Category category, LocalDate date, String notes) {
        super(amount, category, date, notes);
    }

    @Override
    public String getType() {
        return "Income";
    }
}