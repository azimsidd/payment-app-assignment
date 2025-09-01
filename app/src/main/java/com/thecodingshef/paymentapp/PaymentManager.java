package com.thecodingshef.paymentapp;

import java.util.ArrayList;
import java.util.List;

public class PaymentManager {
    private List<Payment> payments;

    public PaymentManager() {
        this.payments = new ArrayList<>();
    }

    public void addPayment(Payment payment) {
        // Remove existing payment of same type if any
        removePaymentByType(payment.getType());
        payments.add(payment);
    }

    public void removePayment(Payment payment) {
        payments.remove(payment);
    }

    private void removePaymentByType(PaymentType type) {
        for (int i = payments.size() - 1; i >= 0; i--) {
            if (payments.get(i).getType() == type) {
                payments.remove(i);
                break;
            }
        }
    }

    public List<Payment> getPayments() {
        return new ArrayList<>(payments);
    }

    public double getTotalAmount() {
        double total = 0.0;
        for (Payment payment : payments) {
            total += payment.getAmount();
        }
        return total;
    }

    public List<PaymentType> getAvailablePaymentTypes() {
        PaymentType[] allTypes = PaymentType.values();
        List<PaymentType> availableTypes = new ArrayList<>();

        for (PaymentType type : allTypes) {
            boolean alreadyAdded = false;
            for (Payment payment : payments) {
                if (payment.getType() == type) {
                    alreadyAdded = true;
                    break;
                }
            }
            if (!alreadyAdded) {
                availableTypes.add(type);
            }
        }

        return availableTypes;
    }
}