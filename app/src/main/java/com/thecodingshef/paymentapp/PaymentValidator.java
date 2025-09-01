package com.thecodingshef.paymentapp;

import android.text.TextUtils;
import android.widget.EditText;

public class PaymentValidator {
    public boolean isValidAmount(String amountStr, EditText amountEditText) {
        if (TextUtils.isEmpty(amountStr)) {
            amountEditText.setError("Amount is required");
            return false;
        }
        try {
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                amountEditText.setError("Amount must be greater than 0");
                return false;
            }
        } catch (NumberFormatException e) {
            amountEditText.setError("Invalid amount");
            return false;
        }
        return true;
    }
}

