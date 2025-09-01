package com.thecodingshef.paymentapp;

public class Payment {
    private PaymentType type;
    private double amount;
    private String provider;
    private String transactionReference;

    public Payment() {

    }

    public Payment(PaymentType type, double amount, String provider, String transactionReference) {
        this.type = type;
        this.amount = amount;
        this.provider = provider;
        this.transactionReference = transactionReference;
    }

    // Getters and setters
    public PaymentType getType() { return type; }
    public void setType(PaymentType type) { this.type = type; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public String getTransactionReference() { return transactionReference; }
    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

}