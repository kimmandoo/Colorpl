package com.colorpl.payment.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentRequest {

    private String orderName;
    private String orderId;
    private double totalPrice;
    private BootUser user;
    private List<BootItem> items;

    // Getters and Setters

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BootUser getUser() {
        return user;
    }

    public void setUser(BootUser user) {
        this.user = user;
    }

    public List<BootItem> getItems() {
        return items;
    }

    public void setItems(List<BootItem> items) {
        this.items = items;
    }
}
