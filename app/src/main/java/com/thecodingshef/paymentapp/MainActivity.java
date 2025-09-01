package com.thecodingshef.paymentapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.Chip;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AddPaymentDialog.OnPaymentAddedListener {

    private TextView totalAmountText;
    private LinearLayout paymentsContainer;
    private TextView addPaymentLink;
    private Button saveButton;

    private PaymentManager paymentManager;
    private FileManager fileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        View root = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            int top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            v.setPadding(v.getPaddingLeft(), top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });


        initializeViews();
        initializeManagers();
        loadSavedPayments();
        updateUI();
        setupClickListeners();
    }

    private void initializeViews() {
        totalAmountText = findViewById(R.id.totalAmountText);
        paymentsContainer = findViewById(R.id.paymentsContainer);
        addPaymentLink = findViewById(R.id.addPaymentLink);
        saveButton = findViewById(R.id.saveButton);
    }

    private void initializeManagers() {
        paymentManager = new PaymentManager();
        fileManager = new FileManager(this);
    }

    private void loadSavedPayments() {
        List<Payment> savedPayments = fileManager.loadPayments();
        for (Payment payment : savedPayments) {
            paymentManager.addPayment(payment);
        }
    }

    private void setupClickListeners() {
        addPaymentLink.setOnClickListener(v -> showAddPaymentDialog());

        saveButton.setOnClickListener(v -> savePayments());
    }

    private void showAddPaymentDialog() {
        List<PaymentType> availableTypes = paymentManager.getAvailablePaymentTypes();
        if (availableTypes.isEmpty()) {
            return;
        }

        AddPaymentDialog dialog = new AddPaymentDialog(availableTypes, this);
        dialog.show(getSupportFragmentManager(), "AddPaymentDialog");
    }

    @Override
    public void onPaymentAdded(Payment payment) {
        paymentManager.addPayment(payment);
        updateUI();
    }

    private void updateUI() {
        updateTotalAmount();
        updatePaymentChips();
    }

    private void updateTotalAmount() {
        double total = paymentManager.getTotalAmount();
        totalAmountText.setText(String.format(Locale.getDefault(), "Total Amount = ₹ %.0f", total));
    }

    private void updatePaymentChips() {
        paymentsContainer.removeAllViews();

        List<Payment> payments = paymentManager.getPayments();
        for (final Payment payment : payments) {
            Chip chipView = createPaymentChip(payment);
            paymentsContainer.addView(chipView);
        }
    }

    private Chip createPaymentChip(final Payment payment) {
        Chip chip = (Chip) LayoutInflater.from(this).inflate(R.layout.payment_item_chip, paymentsContainer, false);

        String chipText = String.format(Locale.getDefault(), "%s: ₹ %.0f",
                payment.getType().getDisplayName(), payment.getAmount());
        chip.setText(chipText);
        chip.setCloseIconVisible(true);

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentManager.removePayment(payment);
                updateUI();
            }
        });

        return chip;
    }


    private void savePayments() {
        fileManager.savePayments(paymentManager.getPayments());
    }
}





