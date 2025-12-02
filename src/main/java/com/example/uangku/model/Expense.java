package com.example.uangku.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Expense extends Transaction {
    private String paymentMethod; // Optional: Cash, Credit Card, etc.
}
