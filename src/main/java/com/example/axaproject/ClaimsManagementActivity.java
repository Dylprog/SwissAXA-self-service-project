package com.example.axaproject;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class ClaimsManagementActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityResultLauncher<String[]> uploadLauncher;
    private TextView uploadStatus;
    private TextView locationStatus;
    private TextView reviewStatus;
    private View claimOptionsContainer;
    private Spinner claimTypeSpinner;
    private Spinner damageTypeSpinner;
    private EditText damageDateInput;
    private FrameLayout mapContainer;
    private GoogleMap googleMap;
    private LatLng selectedLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claims_management);

        View returnButton = findViewById(R.id.btn_return);
        returnButton.setOnClickListener(v -> finish());

        uploadStatus = findViewById(R.id.upload_status);
        locationStatus = findViewById(R.id.location_status);
        reviewStatus = findViewById(R.id.review_status);
        claimOptionsContainer = findViewById(R.id.claim_options_container);
        claimTypeSpinner = findViewById(R.id.spinner_claim_type);
        damageTypeSpinner = findViewById(R.id.spinner_damage_type);
        damageDateInput = findViewById(R.id.input_damage_date);
        mapContainer = findViewById(R.id.map_container);

        ArrayAdapter<String> claimTypeAdapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item_white,
                new String[]{"Select type of claim", "Auto", "Home", "Travel", "Health", "Other"}
        );
        claimTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_white);
        claimTypeSpinner.setAdapter(claimTypeAdapter);

        ArrayAdapter<String> damageTypeAdapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item_white,
                new String[]{"Select type of damages", "Theft", "Water damage", "Fire",
                        "Accident", "Other"}
        );
        damageTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_white);
        damageTypeSpinner.setAdapter(damageTypeAdapter);

        damageDateInput.setOnClickListener(v -> showDatePicker());

        uploadLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                this::handleUploadResult
        );

        findViewById(R.id.btn_file_claim)
                .setOnClickListener(v -> claimOptionsContainer.setVisibility(View.VISIBLE));

        findViewById(R.id.btn_upload_evidence)
                .setOnClickListener(v -> uploadLauncher.launch(new String[]{"image/*", "video/*"}));

        findViewById(R.id.btn_capture_location)
                .setOnClickListener(v -> {
                    showMap();
                });

        findViewById(R.id.btn_submit_claim)
                .setOnClickListener(v -> {
                    String claimType = String.valueOf(claimTypeSpinner.getSelectedItem());
                    String damageType = String.valueOf(damageTypeSpinner.getSelectedItem());
                    String damageDate = "";
                    if (damageDateInput.getText() != null) {
                        damageDate = damageDateInput.getText().toString().trim();
                    }

                    if (claimTypeSpinner.getSelectedItemPosition() == 0
                            || damageTypeSpinner.getSelectedItemPosition() == 0
                            || damageDate.isEmpty()) {
                        Toast.makeText(this,
                                "Please complete all required fields.",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    reviewStatus.setText("Submitted.\nClaim type: " + claimType
                            + "\nDamage: " + damageType
                            + "\nDate: " + damageDate);
                    Toast.makeText(
                            this,
                            "Claim submitted. Our team will review it.",
                            Toast.LENGTH_LONG
                    ).show();
                    claimOptionsContainer.setVisibility(View.GONE);
                });
    }

    private void handleUploadResult(Uri uri) {
        if (uri == null) {
            Toast.makeText(this, "No file selected.", Toast.LENGTH_LONG).show();
            return;
        }
        uploadStatus.setText("Evidence uploaded");
        Toast.makeText(this, "Evidence uploaded.", Toast.LENGTH_LONG).show();
    }

    private void showDatePicker() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int year = calendar.get(java.util.Calendar.YEAR);
        int month = calendar.get(java.util.Calendar.MONTH);
        int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, (view, y, m, d) -> {
            String formatted = String.format("%02d/%02d/%04d", d, m + 1, y);
            damageDateInput.setText(formatted);
        }, year, month, day);
        dialog.show();
    }

    private void showMap() {
        mapContainer.setVisibility(View.VISIBLE);
        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_container);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map_container, fragment)
                    .commitNow();
        }
        fragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        LatLng zurich = new LatLng(47.3769, 8.5417);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zurich, 12f));
        googleMap.setOnMapClickListener(latLng -> {
            selectedLatLng = latLng;
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(latLng));
            locationStatus.setText("Location captured: "
                    + String.format("%.5f, %.5f", latLng.latitude, latLng.longitude));
            Toast.makeText(this, "Location captured.", Toast.LENGTH_LONG).show();
        });
    }
}
