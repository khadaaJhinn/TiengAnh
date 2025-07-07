package com.vantinh.tienganh;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FeedbackActivity extends AppCompatActivity {

    private Spinner spFeedbackType;
    private RatingBar rbRating;
    private EditText etSubject, etMessage;
    private Button btnSubmitFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        initViews();
        setupSpinner();
        setupClickListeners();
    }

    private void initViews() {
        spFeedbackType = findViewById(R.id.sp_feedback_type);
        rbRating = findViewById(R.id.rb_rating);
        etSubject = findViewById(R.id.et_subject);
        etMessage = findViewById(R.id.et_message);
        btnSubmitFeedback = findViewById(R.id.btn_submit_feedback);
    }

    private void setupSpinner() {
        // Setup feedback type spinner
        String[] feedbackTypes = {
            "General Feedback",
            "Course Content",
            "Technical Issue",
            "Feature Request",
            "Bug Report",
            "Instructor Feedback"
        };

        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, feedbackTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFeedbackType.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnSubmitFeedback.setOnClickListener(v -> handleSubmitFeedback());
    }

    private void handleSubmitFeedback() {
        String feedbackType = spFeedbackType.getSelectedItem().toString();
        float rating = rbRating.getRating();
        String subject = etSubject.getText().toString().trim();
        String message = etMessage.getText().toString().trim();

        if (validateInput(subject, message)) {
            // TODO: Implement feedback submission logic
            submitFeedback(feedbackType, rating, subject, message);
        }
    }

    private boolean validateInput(String subject, String message) {
        if (TextUtils.isEmpty(subject)) {
            etSubject.setError("Subject is required");
            return false;
        }

        if (TextUtils.isEmpty(message)) {
            etMessage.setError("Message is required");
            return false;
        }

        if (message.length() < 10) {
            etMessage.setError("Message must be at least 10 characters long");
            return false;
        }

        return true;
    }

    private void submitFeedback(String feedbackType, float rating, String subject, String message) {
        // TODO: Send feedback to server/database
        
        // Create feedback object
        Feedback feedback = new Feedback(feedbackType, rating, subject, message);
        
        // Simulate successful submission
        Toast.makeText(this, "Feedback submitted successfully!", Toast.LENGTH_SHORT).show();
        
        // Clear form
        clearForm();
    }

    private void clearForm() {
        spFeedbackType.setSelection(0);
        rbRating.setRating(0);
        etSubject.setText("");
        etMessage.setText("");
    }

    // Inner class for Feedback model
    private static class Feedback {
        private String type;
        private float rating;
        private String subject;
        private String message;
        private long timestamp;

        public Feedback(String type, float rating, String subject, String message) {
            this.type = type;
            this.rating = rating;
            this.subject = subject;
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }

        public String getType() { return type; }
        public float getRating() { return rating; }
        public String getSubject() { return subject; }
        public String getMessage() { return message; }
        public long getTimestamp() { return timestamp; }
    }
}