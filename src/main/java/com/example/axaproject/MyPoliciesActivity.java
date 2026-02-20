package com.example.axaproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyPoliciesActivity extends AppCompatActivity {

    private List<Policy> policies;
    private PolicyAdapter adapter;
    private ActivityResultLauncher<String[]> uploadLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_policies);

        View returnButton = findViewById(R.id.btn_return);
        returnButton.setOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.policies_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        policies = PolicyRepository.getPolicies();
        adapter = new PolicyAdapter(policies, this::openPolicyDetails, this::showRecommendations);
        recyclerView.setAdapter(adapter);

        uploadLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                this::handleUploadResult
        );

        findViewById(R.id.btn_upload_external_policy).setOnClickListener(
                v -> uploadLauncher.launch(new String[]{"*/*"}));
        findViewById(R.id.btn_compare_ai).setOnClickListener(
                v -> Toast.makeText(this, "AI comparison coming soon.", Toast.LENGTH_LONG).show());
        findViewById(R.id.btn_campaigns).setOnClickListener(
                v -> Toast.makeText(this, "Campaigns coming soon.", Toast.LENGTH_LONG).show());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void handleUploadResult(Uri uri) {
        if (uri == null) {
            Toast.makeText(this, "No file selected.", Toast.LENGTH_LONG).show();
            return;
        }
        promptExternalPolicyName();
    }

    private void promptExternalPolicyName() {
        EditText input = new EditText(this);
        input.setHint("Policy name");

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Name external policy")
                .setView(input)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(v -> {
                    String name = input.getText() != null
                            ? input.getText().toString().trim()
                            : "";
                    if (name.isEmpty()) {
                        Toast.makeText(this, "Please enter a policy name.", Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                    String title = "External - " + name;
                    Policy externalPolicy = new Policy(
                            title,
                            "EXT-" + (policies.size() + 1),
                            "Unknown",
                            true
                    );
                    policies.add(0, externalPolicy);
                    adapter.notifyItemInserted(0);
                    Toast.makeText(this, "External policy added.", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }));

        dialog.show();
    }

    private void openPolicyDetails(Policy policy) {
        Intent intent = new Intent(this, PolicyDetailActivity.class);
        intent.putExtra(PolicyDetailActivity.EXTRA_TITLE, policy.getTitle());
        intent.putExtra(PolicyDetailActivity.EXTRA_NUMBER, policy.getNumber());
        intent.putExtra(PolicyDetailActivity.EXTRA_EXPIRY, policy.getExpiryDate());
        startActivity(intent);
    }

    private void showRecommendations(Policy policy) {
        String message = "Based on the uploaded policy, we recommend:\n"
                + "• SwissAXA Comprehensive Cover\n"
                + "• SwissAXA Value Plus\n"
                + "• SwissAXA Multi-Policy Bundle";

        new AlertDialog.Builder(this)
                .setTitle("Recommendations")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
