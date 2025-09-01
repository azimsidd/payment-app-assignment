package com.thecodingshef.paymentapp;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String FILE_NAME = "LastPayment.txt";
    private final Context context;
    private final Gson gson;

    public FileManager(Context context) {
        this.context = context;
        this.gson = new Gson();
    }

    public void savePayments(List<Payment> payments) {
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            FileWriter writer = new FileWriter(file);
            gson.toJson(payments, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Payment> loadPayments() {
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            if (file.exists()) {
                FileReader reader = new FileReader(file);
                Type listType = new TypeToken<List<Payment>>(){}.getType();
                List<Payment> payments = gson.fromJson(reader, listType);
                reader.close();
                return payments != null ? payments : new ArrayList<Payment>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}