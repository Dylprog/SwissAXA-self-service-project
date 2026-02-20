package com.example.axaproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomepageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homepage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button tabMyPolicies = findViewById(R.id.tab_myPolicies);
        Button tabMyDocument = findViewById(R.id.tab_myDocument);
        Button tabMyBank = findViewById(R.id.tab_myBank);
        Button tabMyServices = findViewById(R.id.tab_myServices);
        Button tabMyInformation = findViewById(R.id.tab_myInformation);

        tabMyPolicies.setOnClickListener(v ->
                startActivity(new Intent(this, MyPoliciesActivity.class)));
        tabMyDocument.setOnClickListener(v ->
                startActivity(new Intent(this, MyDocumentActivity.class)));
        tabMyBank.setOnClickListener(v ->
                startActivity(new Intent(this, MyBankActivity.class)));
        tabMyServices.setOnClickListener(v ->
                startActivity(new Intent(this, MyServicesActivity.class)));
        tabMyInformation.setOnClickListener(v ->
                startActivity(new Intent(this, MyInformationActivity.class)));
    }
}