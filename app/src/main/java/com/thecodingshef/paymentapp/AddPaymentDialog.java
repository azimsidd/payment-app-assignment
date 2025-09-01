package com.thecodingshef.paymentapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.util.ArrayList;
import java.util.List;

public class AddPaymentDialog extends DialogFragment {

    public interface OnPaymentAddedListener {
        void onPaymentAdded(Payment payment);
    }

    private final List<PaymentType> availableTypes;
    private final OnPaymentAddedListener listener;

    private EditText amountEditText;
    private Spinner paymentTypeSpinner;
    private EditText providerEditText;
    private EditText transactionRefEditText;
    private LinearLayout additionalFieldsContainer;
    private Button cancelButton;
    private Button okButton;

    private PaymentType selectedPaymentType;

    // New helper classes
    private final PaymentValidator validator = new PaymentValidator();
    private final PaymentFactory paymentFactory = new PaymentFactory();

    public AddPaymentDialog(List<PaymentType> availableTypes, OnPaymentAddedListener listener) {
        this.availableTypes = availableTypes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_payment, null);

        initializeViews(view);
        setupSpinner();
        setupClickListeners();

        builder.setView(view);
        return builder.create();
    }

    private void initializeViews(View view) {
        amountEditText = view.findViewById(R.id.amountEditText);
        paymentTypeSpinner = view.findViewById(R.id.paymentTypeSpinner);
        providerEditText = view.findViewById(R.id.providerEditText);
        transactionRefEditText = view.findViewById(R.id.transactionRefEditText);
        additionalFieldsContainer = view.findViewById(R.id.additionalFieldsContainer);
        cancelButton = view.findViewById(R.id.cancelButton);
        okButton = view.findViewById(R.id.okButton);
    }

    private void setupSpinner() {
        List<String> typeNames = new ArrayList<>();
        for (PaymentType type : availableTypes) {
            typeNames.add(type.getDisplayName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                typeNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentTypeSpinner.setAdapter(adapter);

        paymentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPaymentType = availableTypes.get(position);
                updateAdditionalFields();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedPaymentType = null;
            }
        });

        if (!availableTypes.isEmpty()) {
            selectedPaymentType = availableTypes.get(0);
            updateAdditionalFields();
        }
    }

    private void updateAdditionalFields() {
        if (selectedPaymentType == PaymentType.BANK_TRANSFER ||
                selectedPaymentType == PaymentType.CREDIT_CARD) {
            additionalFieldsContainer.setVisibility(View.VISIBLE);
        } else {
            additionalFieldsContainer.setVisibility(View.GONE);
        }
    }

    private void setupClickListeners() {
        cancelButton.setOnClickListener(v -> dismiss());

        okButton.setOnClickListener(v -> {
            String amountStr = amountEditText.getText().toString().trim();

            if (validator.isValidAmount(amountStr, amountEditText)) {
                String provider = null;
                String transactionRef = null;

                if (selectedPaymentType == PaymentType.BANK_TRANSFER ||
                        selectedPaymentType == PaymentType.CREDIT_CARD) {
                    provider = providerEditText.getText().toString().trim();
                    transactionRef = transactionRefEditText.getText().toString().trim();
                }

                Payment payment = paymentFactory.create(selectedPaymentType, amountStr, provider, transactionRef);

                if (listener != null) {
                    listener.onPaymentAdded(payment);
                }
                dismiss();
            }
        });
    }
}
