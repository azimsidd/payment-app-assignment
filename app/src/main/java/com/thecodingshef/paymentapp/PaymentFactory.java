package com.thecodingshef.paymentapp;

public class PaymentFactory {
    public Payment create(PaymentType type, String amountStr, String provider, String transactionRef) {
        double amount = Double.parseDouble(amountStr.trim());
        return new Payment(type, amount, provider, transactionRef);
    }
}
