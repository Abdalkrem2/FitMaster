package com.web.fitmaster.dto;

import com.fitmaster.model.enums.PaymentMethod;
import com.fitmaster.model.enums.RevenueType;
import java.time.LocalDate;

public class RevenueDTO {
    private Long userId;
    private double amount;
    private LocalDate date;
    private RevenueType revenueType;
    private PaymentMethod paymentMethod;
    private String description;

    // getters + setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public RevenueType getRevenueType() { return revenueType; }
    public void setRevenueType(RevenueType revenueType) { this.revenueType = revenueType; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

