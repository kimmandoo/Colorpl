package com.colorpl.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BootItem {
    private String name;
    private String id;
    private int qty;
    private double price;

    // Getters and Setters

    public BootItem setName(String name) {
        this.name = name;
        return this;
    }

    public BootItem setId(String id) {
        this.id = id;
        return this;
    }

    public BootItem setQty(int qty) {
        this.qty = qty;
        return this;
    }

    public BootItem setPrice(double price) {
        this.price = price;
        return this;
    }
}