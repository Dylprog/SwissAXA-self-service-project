package com.example.axaproject;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PolicyDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_NUMBER = "extra_number";
    public static final String EXTRA_EXPIRY = "extra_expiry";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_detail);

        View returnButton = findViewById(R.id.btn_return);
        returnButton.setOnClickListener(v -> finish());

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        String number = getIntent().getStringExtra(EXTRA_NUMBER);
        String expiry = getIntent().getStringExtra(EXTRA_EXPIRY);

        TextView detailTitle = findViewById(R.id.detail_title);
        TextView detailNumber = findViewById(R.id.detail_number);
        TextView detailExpiry = findViewById(R.id.detail_expiry);

        detailTitle.setText(title != null ? title : "Policy");
        detailNumber.setText("Policy #: " + (number != null ? number : "N/A"));
        detailExpiry.setText("Expires: " + (expiry != null ? expiry : "N/A"));
    }
}
