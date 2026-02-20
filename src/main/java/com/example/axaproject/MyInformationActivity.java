package com.example.axaproject;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MyInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);

        View returnButton = findViewById(R.id.btn_return);
        returnButton.setOnClickListener(v -> finish());

        EditText fullName = findViewById(R.id.input_full_name);
        EditText email = findViewById(R.id.input_email);
        EditText phone = findViewById(R.id.input_phone);
        EditText address = findViewById(R.id.input_address);
        EditText dob = findViewById(R.id.input_dob);
        EditText married = findViewById(R.id.input_married);
        EditText children = findViewById(R.id.input_children);
        EditText vehicleMake = findViewById(R.id.input_vehicle_make);
        EditText vehicleModel = findViewById(R.id.input_vehicle_model);
        EditText vehicleYear = findViewById(R.id.input_vehicle_year);
        TextView status = findViewById(R.id.personal_status);

        fullName.setText("Dylan Haughney");
        email.setText("Dylan.Haughney@IU-study.com");
        phone.setText("+41 12 123 1234 12");
        address.setText("V2 residences, Dubai");
        dob.setText("16/07/1993");
        married.setText("Yes");
        children.setText("1");
        vehicleMake.setText("Toyota");
        vehicleModel.setText("Rav4");
        vehicleYear.setText("2022");

        findViewById(R.id.btn_save_personal).setOnClickListener(v -> {
            if (fullName.getText() == null || fullName.getText().toString().trim().isEmpty()
                    || email.getText() == null || email.getText().toString().trim().isEmpty()
                    || phone.getText() == null || phone.getText().toString().trim().isEmpty()
                    || address.getText() == null || address.getText().toString().trim().isEmpty()
                    || dob.getText() == null || dob.getText().toString().trim().isEmpty()
                    || married.getText() == null || married.getText().toString().trim().isEmpty()
                    || children.getText() == null || children.getText().toString().trim().isEmpty()
                    || vehicleMake.getText() == null || vehicleMake.getText().toString().trim().isEmpty()
                    || vehicleModel.getText() == null || vehicleModel.getText().toString().trim().isEmpty()
                    || vehicleYear.getText() == null || vehicleYear.getText().toString().trim().isEmpty()) {
                Toast.makeText(this,
                        "Please complete all fields.",
                        Toast.LENGTH_LONG).show();
                return;
            }
            status.setText("Saved. Personal data updated.");
            Toast.makeText(this,
                    "Personal data saved.",
                    Toast.LENGTH_LONG).show();
        });
    }
}
