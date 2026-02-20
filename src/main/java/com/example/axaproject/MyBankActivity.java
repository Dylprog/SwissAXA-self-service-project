package com.example.axaproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MyBankActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bank);

        View returnButton = findViewById(R.id.btn_return);
        returnButton.setOnClickListener(v -> finish());

        findViewById(R.id.btn_open_ubs)
                .setOnClickListener(v -> openBankApp("UBS Mobile"));
        findViewById(R.id.btn_open_credit_suisse)
                .setOnClickListener(v -> openBankApp("Credit Suisse"));
        findViewById(R.id.btn_open_raiffeisen)
                .setOnClickListener(v -> openBankApp("Raiffeisen"));
        findViewById(R.id.btn_open_other_bank)
                .setOnClickListener(v -> openBankApp("Other bank"));
    }

    private void openBankApp(String bankName) {
        Toast.makeText(this,
                "Download app first",
                Toast.LENGTH_LONG).show();
    }
}
