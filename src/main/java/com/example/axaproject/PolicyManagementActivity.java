package com.example.axaproject;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
public class PolicyManagementActivity extends AppCompatActivity {

    private Policy selectedPolicy;
    private int selectedIndex = 0;
    private Spinner policySpinner;
    private Spinner renewalSpinner;
    private View renewalSubmitButton;
    private View renewalContainer;
    private View renewalCard;
    private TextView renewalPrevious;
    private TextView renewalNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_management);

        View returnButton = findViewById(R.id.btn_return);
        returnButton.setOnClickListener(v -> finish());

        EditText policyName = findViewById(R.id.input_policy_name);
        EditText policyNumber = findViewById(R.id.input_policy_number);
        EditText policyExpiry = findViewById(R.id.input_policy_expiry);
        TextView policyStatus = findViewById(R.id.policy_status);
        policySpinner = findViewById(R.id.spinner_policy_select);
        renewalSpinner = findViewById(R.id.spinner_renewal);
        renewalSubmitButton = findViewById(R.id.btn_submit_renewal);
        renewalContainer = findViewById(R.id.renewal_container);
        renewalCard = findViewById(R.id.renewal_card);
        renewalPrevious = findViewById(R.id.renewal_previous);
        renewalNew = findViewById(R.id.renewal_new);

        java.util.List<Policy> policies = PolicyRepository.getPolicies();
        java.util.List<String> policyTitles = new java.util.ArrayList<>();
        for (Policy policy : policies) {
            policyTitles.add(policy.getTitle());
        }
        ArrayAdapter<String> policyAdapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item_white,
                policyTitles
        );
        policyAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_white);
        policySpinner.setAdapter(policyAdapter);
        policySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedIndex = position;
                selectedPolicy = policies.get(position);
                policyName.setText(selectedPolicy.getTitle());
                policyNumber.setText(selectedPolicy.getNumber());
                policyExpiry.setText(selectedPolicy.getExpiryDate());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (!policies.isEmpty()) {
            policySpinner.setSelection(selectedIndex);
        }

        ArrayAdapter<String> renewalAdapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item_white,
                new String[]{"Select option", "Yes", "No"}
        );
        renewalAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_white);
        renewalSpinner.setAdapter(renewalAdapter);
        renewalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    renewalSubmitButton.setVisibility(View.VISIBLE);
                    String oldDate = policyExpiry.getText().toString().trim();
                    if (oldDate.isEmpty() && selectedPolicy != null) {
                        oldDate = selectedPolicy.getExpiryDate();
                    }
                    String normalizedOldDate = formatDateIfPossible(oldDate);
                    String newDate = addOneYear(oldDate);
                    renewalPrevious.setText("Current expiry: " + normalizedOldDate);
                    renewalNew.setText("Renewed expiry: " + newDate);
                    renewalCard.setVisibility(View.GONE);
                } else {
                    renewalSubmitButton.setVisibility(View.GONE);
                    renewalCard.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        renewalSubmitButton.setOnClickListener(v -> {
            renewalCard.setVisibility(View.VISIBLE);
            policyStatus.setText("Renewal submitted. Policy team is processing.");
            Toast.makeText(this,
                    "Renewal submitted.",
                    Toast.LENGTH_LONG).show();
            renewalSubmitButton.setVisibility(View.GONE);
            renewalSpinner.setSelection(0);
            renewalContainer.setVisibility(View.GONE);
        });

        findViewById(R.id.btn_update_policy).setOnClickListener(v -> {
            if (policyName.getText() == null || policyName.getText().toString().trim().isEmpty()
                    || policyNumber.getText() == null
                    || policyNumber.getText().toString().trim().isEmpty()
                    || policyExpiry.getText() == null
                    || policyExpiry.getText().toString().trim().isEmpty()) {
                Toast.makeText(this,
                        "Please complete all policy fields.",
                        Toast.LENGTH_LONG).show();
                return;
            }
            if (selectedPolicy != null) {
                selectedPolicy.setTitle(policyName.getText().toString().trim());
                selectedPolicy.setNumber(policyNumber.getText().toString().trim());
                selectedPolicy.setExpiryDate(policyExpiry.getText().toString().trim());
                policyTitles.set(selectedIndex, selectedPolicy.getTitle());
                policyAdapter.notifyDataSetChanged();
            }
            policyStatus.setText("Update submitted. Policy team is processing.");
            renewalContainer.setVisibility(View.VISIBLE);
        });

        findViewById(R.id.btn_request_upgrade).setOnClickListener(v -> {
            policyStatus.setText("Upgrade request submitted. Policy team is processing.");
            Toast.makeText(this,
                    "Upgrade request submitted.",
                    Toast.LENGTH_LONG).show();
        });
    }

    private String addOneYear(String date) {
        String normalized = normalizeDateInput(date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        format.setLenient(false);
        try {
            java.util.Date parsed = format.parse(normalized);
            if (parsed == null) {
                return "Invalid date format";
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsed);
            calendar.add(Calendar.YEAR, 1);
            return format.format(calendar.getTime());
        } catch (ParseException ignored) {
            return "Invalid date format";
        }
    }

    private String formatDateIfPossible(String date) {
        String normalized = normalizeDateInput(date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        format.setLenient(false);
        try {
            java.util.Date parsed = format.parse(normalized);
            return parsed == null ? date : format.format(parsed);
        } catch (ParseException ignored) {
            return date;
        }
    }

    private String normalizeDateInput(String date) {
        if (date == null) {
            return "";
        }
        String trimmed = date.trim();
        if (trimmed.contains("-") && !trimmed.contains("/")) {
            return trimmed.replace("-", "/");
        }
        return trimmed;
    }
}
