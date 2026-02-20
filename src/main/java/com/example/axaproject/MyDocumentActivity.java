package com.example.axaproject;

import android.content.ContentResolver;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.shape.ShapeAppearanceModel;

import androidx.core.content.ContextCompat;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyDocumentActivity extends AppCompatActivity {

    private static final long MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024;
    private final List<DocumentItem> documents = new ArrayList<>();
    private DocumentAdapter adapter;
    private ActivityResultLauncher<String[]> uploadLauncher;
    private MaterialButton claimsButton;
    private MaterialButton policiesButton;
    private MaterialButton uploadButton;
    private String selectedType = "Claims";
    private ColorStateList uploadTint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_document);

        View returnButton = findViewById(R.id.btn_return);
        returnButton.setOnClickListener(v -> finish());

        uploadButton = findViewById(R.id.btn_upload_document);
        uploadTint = uploadButton.getBackgroundTintList();

        claimsButton = findViewById(R.id.btn_type_claims);
        policiesButton = findViewById(R.id.btn_type_policies);
        updateSegmentShapes();
        updateTypeSelection("Claims");

        claimsButton.setOnClickListener(v -> updateTypeSelection("Claims"));
        policiesButton.setOnClickListener(v -> updateTypeSelection("Policies"));

        RecyclerView recyclerView = findViewById(R.id.documents_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DocumentAdapter(documents);
        recyclerView.setAdapter(adapter);

        uploadLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                this::handleUploadResult
        );

        findViewById(R.id.btn_upload_document)
                .setOnClickListener(v -> uploadLauncher.launch(new String[]{"*/*"}));
    }

    private void handleUploadResult(Uri uri) {
        if (uri == null) {
            Toast.makeText(this, "No file selected.", Toast.LENGTH_LONG).show();
            return;
        }

        String fileName = getFileName(uri);
        long fileSize = getFileSize(uri);
        String mimeType = getContentResolver().getType(uri);

        if (!isValidDocument(mimeType, fileSize)) {
            Toast.makeText(this, "Upload error: invalid file size or format.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        String docType = selectedType;
        DocumentItem item = new DocumentItem(
                fileName != null ? fileName : "Document",
                docType,
                "Stored securely"
        );
        documents.add(0, item);
        adapter.notifyItemInserted(0);
        Toast.makeText(this, "Document uploaded successfully.", Toast.LENGTH_LONG).show();
    }

    private boolean isValidDocument(String mimeType, long fileSize) {
        if (fileSize <= 0 || fileSize > MAX_FILE_SIZE_BYTES) {
            return false;
        }
        if (mimeType == null) {
            return false;
        }
        return mimeType.equals("application/pdf")
                || mimeType.equals("image/jpeg")
                || mimeType.equals("image/png");
    }

    private String getFileName(Uri uri) {
        ContentResolver resolver = getContentResolver();
        try (Cursor cursor = resolver.query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (nameIndex >= 0) {
                    return cursor.getString(nameIndex);
                }
            }
        }
        return null;
    }

    private long getFileSize(Uri uri) {
        ContentResolver resolver = getContentResolver();
        try (Cursor cursor = resolver.query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                if (sizeIndex >= 0) {
                    return cursor.getLong(sizeIndex);
                }
            }
        }
        return -1;
    }

    private void updateTypeSelection(String type) {
        selectedType = type;
        boolean isClaims = "Claims".equals(type);
        styleSegment(claimsButton, isClaims);
        styleSegment(policiesButton, !isClaims);
    }

    private void updateSegmentShapes() {
        applySegmentShape(claimsButton, 0, true);
        applySegmentShape(policiesButton, 0, false);
    }

    private void applySegmentShape(MaterialButton button, int radius, boolean isLeft) {
        ShapeAppearanceModel.Builder builder = button.getShapeAppearanceModel().toBuilder();
        if (isLeft) {
            builder.setTopLeftCornerSize(radius);
            builder.setBottomLeftCornerSize(radius);
            builder.setTopRightCornerSize(0);
            builder.setBottomRightCornerSize(0);
        } else {
            builder.setTopRightCornerSize(radius);
            builder.setBottomRightCornerSize(radius);
            builder.setTopLeftCornerSize(0);
            builder.setBottomLeftCornerSize(0);
        }
        button.setShapeAppearanceModel(builder.build());
    }

    private void styleSegment(MaterialButton button, boolean selected) {
        if (selected) {
            button.setTextColor(ContextCompat.getColor(this, R.color.black));
            button.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.white)));
            button.setStrokeWidth(dpToPx(3));
            button.setStrokeColor(ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.axa_red)));
        } else {
            button.setTextColor(ContextCompat.getColor(this, R.color.white));
            if (uploadTint != null) {
                button.setBackgroundTintList(uploadTint);
            }
            button.setStrokeWidth(0);
            button.setStrokeColor(null);
        }
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}
