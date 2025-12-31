package com.example.uangku.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Income extends Transaction {
    private String source;

    @Override
    public String getType() {
        return "Income";
    }

}