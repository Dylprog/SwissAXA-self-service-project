package com.example.axaproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MyServicesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_services);

        View returnButton = findViewById(R.id.btn_return);
        returnButton.setOnClickListener(v -> finish());

        findViewById(R.id.btn_claims_management).setOnClickListener(
                v -> startActivity(new Intent(this, ClaimsManagementActivity.class)));
        findViewById(R.id.btn_policy_management).setOnClickListener(
                v -> startActivity(new Intent(this, PolicyManagementActivity.class)));
        findViewById(R.id.btn_appointment_booking).setOnClickListener(
                v -> startActivity(new Intent(this, AppointmentBookingActivity.class)));
    }
}
