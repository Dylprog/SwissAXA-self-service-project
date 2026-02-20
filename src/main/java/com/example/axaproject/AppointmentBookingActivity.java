package com.example.axaproject;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AppointmentBookingActivity extends AppCompatActivity {

    private TextView selectedDateTime;
    private TextView bookingStatus;
    private Spinner serviceSpinner;
    private Spinner timeSlotSpinner;
    private View timeSlotContainer;
    private String pickedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_booking);

        View returnButton = findViewById(R.id.btn_return);
        returnButton.setOnClickListener(v -> finish());

        selectedDateTime = findViewById(R.id.selected_datetime);
        bookingStatus = findViewById(R.id.booking_status);
        serviceSpinner = findViewById(R.id.spinner_service_agent);
        timeSlotSpinner = findViewById(R.id.spinner_time_slot);
        timeSlotContainer = findViewById(R.id.time_slot_container);

        ArrayAdapter<String> serviceAdapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item_white,
                new String[]{"Select service/agent", "Customer Service", "Claims Agent",
                        "Policy Advisor", "Account Manager"}
        );
        serviceAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_white);
        serviceSpinner.setAdapter(serviceAdapter);

        updateTimeSlots("Select service/agent");
        timeSlotContainer.setVisibility(View.GONE);

        serviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = String.valueOf(serviceSpinner.getSelectedItem());
                updateTimeSlots(selection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        findViewById(R.id.btn_select_datetime).setOnClickListener(v -> pickDate());

        findViewById(R.id.btn_confirm_booking).setOnClickListener(v -> confirmBooking());
    }

    private void pickDate() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dateDialog = new DatePickerDialog(this, (view, y, m, d) -> {
            pickedDate = String.format("%04d/%02d/%02d", y, m + 1, d);
            selectedDateTime.setText("Selected date: " + pickedDate);
            String selection = String.valueOf(serviceSpinner.getSelectedItem());
            updateTimeSlots(selection);
            timeSlotContainer.setVisibility(View.VISIBLE);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dateDialog.show();
    }

    private void confirmBooking() {
        if (serviceSpinner.getSelectedItemPosition() == 0
                || timeSlotSpinner.getSelectedItemPosition() == 0
                || pickedDate.isEmpty()) {
            Toast.makeText(this,
                    "Please select service, time slot, and date.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        String service = String.valueOf(serviceSpinner.getSelectedItem());
        String timeSlot = String.valueOf(timeSlotSpinner.getSelectedItem());
        bookingStatus.setText("Booking confirmed.\nService: " + service
                + "\nSlot: " + timeSlot
                + "\nWhen: " + pickedDate
                + "\nAgent/service desk notified.");
        Toast.makeText(this,
                "Booking confirmed. Confirmation sent.",
                Toast.LENGTH_LONG).show();
    }

    private void updateTimeSlots(String selection) {
        String[] slots;
        if ("Customer Service".equals(selection)) {
            slots = new String[]{"Select time slot", "09:15", "10:00", "11:45", "14:30"};
        } else if ("Claims Agent".equals(selection)) {
            slots = new String[]{"Select time slot", "08:45", "12:15", "15:30", "16:15"};
        } else if ("Policy Advisor".equals(selection)) {
            slots = new String[]{"Select time slot", "09:00", "11:30", "13:45", "16:00"};
        } else if ("Account Manager".equals(selection)) {
            slots = new String[]{"Select time slot", "10:15", "12:45", "15:00", "17:30"};
        } else {
            slots = new String[]{"Select time slot"};
        }
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item_white,
                slots
        );
        timeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_white);
        timeSlotSpinner.setAdapter(timeAdapter);
    }
}
