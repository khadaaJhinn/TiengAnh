package com.vantinh.tienganh;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase. firestore.FirebaseFirestore; // Thêm import này
import java.util.HashMap; // Thêm import này
import java.util.Map; // Thêm import này

public class RegisterActivity extends AppCompatActivity {

    private EditText etFullName, etEmail, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvLogin;
    private FirebaseAuth mAuth; // Khai báo
    private FirebaseFirestore db; // Khai báo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance(); // Khởi tạo
        db = FirebaseFirestore.getInstance(); // Khởi tạo


        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnRegister = findViewById(R.id.btn_register);
        tvLogin = findViewById(R.id.tv_login);
    }

    private void setupClickListeners() {
        btnRegister.setOnClickListener(v -> handleRegister());
        
        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void handleRegister() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (validateInput(fullName, email, password, confirmPassword)) {
            // --- BẮT ĐẦU CODE FIREBASE ---
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Đăng ký thành công, lấy User ID
                            String userId = mAuth.getCurrentUser().getUid();

                            // Tạo một đối tượng User để lưu vào Firestore
                            Map<String, Object> user = new HashMap<>();
                            user.put("fullName", fullName);
                            user.put("email", email);
                            user.put("role", "Student"); // Mặc định vai trò là Student
                            user.put("status", "Active"); // Mặc định trạng thái

                            // Lưu thông tin user vào Cloud Firestore
                            db.collection("users").document(userId).set(user)
                                    .addOnSuccessListener(aVoid -> {
                                        // Lưu thành công
                                        Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Lưu thất bại
                                        Toast.makeText(RegisterActivity.this, "Error saving user data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    });

                        } else {
                            // Nếu đăng ký thất bại, hiển thị thông báo lỗi
                            Toast.makeText(RegisterActivity.this, "Authentication failed. " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
            // --- KẾT THÚC CODE FIREBASE ---
        }
    }

    private boolean validateInput(String fullName, String email, String password, String confirmPassword) {
        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Full name is required");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return false;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            return false;
        }

        return true;
    }
}