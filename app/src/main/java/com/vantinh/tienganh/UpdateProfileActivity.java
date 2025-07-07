package com.vantinh.tienganh;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;



public class    UpdateProfileActivity extends AppCompatActivity {

    private EditText etFullName, etEmail, etPhone, etAddress;
    private Button btnUpdateProfile, btnChangePassword;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        if (mAuth.getCurrentUser() != null) {
            userId = mAuth.getCurrentUser().getUid();
        }

        initViews();
        setupClickListeners();
        loadUserProfile();
    }

    private void initViews() {
        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etAddress = findViewById(R.id.et_address);
        btnUpdateProfile = findViewById(R.id.btn_update_profile);
        btnChangePassword = findViewById(R.id.btn_change_password);
    }

    private void setupClickListeners() {
        btnUpdateProfile.setOnClickListener(v -> handleUpdateProfile());
        btnChangePassword.setOnClickListener(v -> handleChangePassword());
    }

    private void loadUserProfile() {
        // Vô hiệu hóa ô email vì thường không cho phép thay đổi email ở đây
        etEmail.setEnabled(false);

        if (userId != null) {
            DocumentReference docRef = db.collection("users").document(userId);
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    etFullName.setText(documentSnapshot.getString("fullName"));
                    etEmail.setText(documentSnapshot.getString("email"));

                    // Các trường này có thể null, cần kiểm tra
                    if (documentSnapshot.contains("phone")) {
                        etPhone.setText(documentSnapshot.getString("phone"));
                    }
                    if (documentSnapshot.contains("address")) {
                        etAddress.setText(documentSnapshot.getString("address"));
                    }
                } else {
                    Toast.makeText(this, "User profile not found.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Failed to load profile.", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void handleUpdateProfile() {
        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (validateInput(fullName)) {
            if (userId != null) {
                DocumentReference userRef = db.collection("users").document(userId);

                // Tạo một Map để chứa các trường cần cập nhật
                Map<String, Object> updates = new HashMap<>();
                updates.put("fullName", fullName);
                updates.put("phone", phone);
                updates.put("address", address);

                // Dùng .update() để chỉ cập nhật các trường này
                userRef.update(updates)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                            Intent resultIntent = new   Intent();
                            setResult(RESULT_OK, resultIntent); // Gửi tín hiệu thành công
                            finish(); // Đóng Activity sau khi cập nhật thành công
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Error updating profile.", Toast.LENGTH_SHORT).show();
                        });
            }
        }
    }

    private void handleChangePassword() {
        // TODO: Implement change password functionality
        Toast.makeText(this, "Change password feature coming soon", Toast.LENGTH_SHORT).show();
    }

    private boolean validateInput(String fullName) {
        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Full name is required");
            return false;
        }
        return true;
    }
}