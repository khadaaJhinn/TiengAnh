package com.vantinh.tienganh;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ContentCreationActivity extends AppCompatActivity {

    private RadioGroup rgContentType;
    private RadioButton rbLecture, rbQuiz;
    
    // Lecture fields
    private EditText etLectureTitle, etLectureDescription, etLectureContent;
    private Spinner spLectureCourse, spLectureLevel;
    
    // Quiz fields
    private EditText etQuizTitle, etQuizDescription, etQuestion, etOption1, etOption2, etOption3, etOption4;
    private Spinner spQuizCourse, spQuizLevel, spCorrectAnswer;
    
    private Button btnSaveContent, btnPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_creation);

        initViews();
        setupSpinners();
        setupClickListeners();
    }

    private void initViews() {
        rgContentType = findViewById(R.id.rg_content_type);
        rbLecture = findViewById(R.id.rb_lecture);
        rbQuiz = findViewById(R.id.rb_quiz);
        
        // Lecture fields
        etLectureTitle = findViewById(R.id.et_lecture_title);
        etLectureDescription = findViewById(R.id.et_lecture_description);
        etLectureContent = findViewById(R.id.et_lecture_content);
        spLectureCourse = findViewById(R.id.sp_lecture_course);
        spLectureLevel = findViewById(R.id.sp_lecture_level);
        
        // Quiz fields
        etQuizTitle = findViewById(R.id.et_quiz_title);
        etQuizDescription = findViewById(R.id.et_quiz_description);
        etQuestion = findViewById(R.id.et_question);
        etOption1 = findViewById(R.id.et_option1);
        etOption2 = findViewById(R.id.et_option2);
        etOption3 = findViewById(R.id.et_option3);
        etOption4 = findViewById(R.id.et_option4);
        spQuizCourse = findViewById(R.id.sp_quiz_course);
        spQuizLevel = findViewById(R.id.sp_quiz_level);
        spCorrectAnswer = findViewById(R.id.sp_correct_answer);
        
        btnSaveContent = findViewById(R.id.btn_save_content);
        btnPreview = findViewById(R.id.btn_preview);
    }

    private void setupSpinners() {
        // Course options
        String[] courses = {
            "Basic English Grammar",
            "English Conversation",
            "Business English",
            "TOEIC Preparation",
            "English Pronunciation"
        };

        // Level options
        String[] levels = {"Beginner", "Intermediate", "Advanced"};

        // Answer options
        String[] answers = {"Option 1", "Option 2", "Option 3", "Option 4"};

        // Setup lecture spinners
        setupSpinner(spLectureCourse, courses);
        setupSpinner(spLectureLevel, levels);

        // Setup quiz spinners
        setupSpinner(spQuizCourse, courses);
        setupSpinner(spQuizLevel, levels);
        setupSpinner(spCorrectAnswer, answers);
    }

    private void setupSpinner(Spinner spinner, String[] items) {
        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setupClickListeners() {
        rgContentType.setOnCheckedChangeListener((group, checkedId) -> {
            toggleContentFields(checkedId == R.id.rb_lecture);
        });

        btnSaveContent.setOnClickListener(v -> handleSaveContent());
        btnPreview.setOnClickListener(v -> handlePreviewContent());

        // Set default selection
        rbLecture.setChecked(true);
        toggleContentFields(true);
    }

    private void toggleContentFields(boolean isLecture) {
        // Show/hide lecture fields
        int lectureVisibility = isLecture ? android.view.View.VISIBLE : android.view.View.GONE;
        etLectureTitle.setVisibility(lectureVisibility);
        etLectureDescription.setVisibility(lectureVisibility);
        etLectureContent.setVisibility(lectureVisibility);
        spLectureCourse.setVisibility(lectureVisibility);
        spLectureLevel.setVisibility(lectureVisibility);

        // Show/hide quiz fields
        int quizVisibility = isLecture ? android.view.View.GONE : android.view.View.VISIBLE;
        etQuizTitle.setVisibility(quizVisibility);
        etQuizDescription.setVisibility(quizVisibility);
        etQuestion.setVisibility(quizVisibility);
        etOption1.setVisibility(quizVisibility);
        etOption2.setVisibility(quizVisibility);
        etOption3.setVisibility(quizVisibility);
        etOption4.setVisibility(quizVisibility);
        spQuizCourse.setVisibility(quizVisibility);
        spQuizLevel.setVisibility(quizVisibility);
        spCorrectAnswer.setVisibility(quizVisibility);
    }

    private void handleSaveContent() {
        if (rbLecture.isChecked()) {
            saveLecture();
        } else {
            saveQuiz();
        }
    }

    private void saveLecture() {
        String title = etLectureTitle.getText().toString().trim();
        String description = etLectureDescription.getText().toString().trim();
        String content = etLectureContent.getText().toString().trim();
        String course = spLectureCourse.getSelectedItem().toString();
        String level = spLectureLevel.getSelectedItem().toString();

        if (validateLectureInput(title, description, content)) {
            // TODO: Save lecture to database
            Lecture lecture = new Lecture(title, description, content, course, level);
            Toast.makeText(this, "Lecture saved successfully!", Toast.LENGTH_SHORT).show();
            clearLectureFields();
        }
    }

    private void saveQuiz() {
        String title = etQuizTitle.getText().toString().trim();
        String description = etQuizDescription.getText().toString().trim();
        String question = etQuestion.getText().toString().trim();
        String option1 = etOption1.getText().toString().trim();
        String option2 = etOption2.getText().toString().trim();
        String option3 = etOption3.getText().toString().trim();
        String option4 = etOption4.getText().toString().trim();
        String course = spQuizCourse.getSelectedItem().toString();
        String level = spQuizLevel.getSelectedItem().toString();
        int correctAnswer = spCorrectAnswer.getSelectedItemPosition();

        if (validateQuizInput(title, description, question, option1, option2, option3, option4)) {
            // TODO: Save quiz to database
            Quiz quiz = new Quiz(title, description, question, 
                    new String[]{option1, option2, option3, option4}, 
                    correctAnswer, course, level);
            Toast.makeText(this, "Quiz saved successfully!", Toast.LENGTH_SHORT).show();
            clearQuizFields();
        }
    }

    private boolean validateLectureInput(String title, String description, String content) {
        if (TextUtils.isEmpty(title)) {
            etLectureTitle.setError("Title is required");
            return false;
        }
        if (TextUtils.isEmpty(description)) {
            etLectureDescription.setError("Description is required");
            return false;
        }
        if (TextUtils.isEmpty(content)) {
            etLectureContent.setError("Content is required");
            return false;
        }
        return true;
    }

    private boolean validateQuizInput(String title, String description, String question, 
                                    String option1, String option2, String option3, String option4) {
        if (TextUtils.isEmpty(title)) {
            etQuizTitle.setError("Title is required");
            return false;
        }
        if (TextUtils.isEmpty(description)) {
            etQuizDescription.setError("Description is required");
            return false;
        }
        if (TextUtils.isEmpty(question)) {
            etQuestion.setError("Question is required");
            return false;
        }
        if (TextUtils.isEmpty(option1)) {
            etOption1.setError("Option 1 is required");
            return false;
        }
        if (TextUtils.isEmpty(option2)) {
            etOption2.setError("Option 2 is required");
            return false;
        }
        if (TextUtils.isEmpty(option3)) {
            etOption3.setError("Option 3 is required");
            return false;
        }
        if (TextUtils.isEmpty(option4)) {
            etOption4.setError("Option 4 is required");
            return false;
        }
        return true;
    }

    private void handlePreviewContent() {
        if (rbLecture.isChecked()) {
            previewLecture();
        } else {
            previewQuiz();
        }
    }

    private void previewLecture() {
        // TODO: Implement lecture preview
        Toast.makeText(this, "Lecture preview coming soon", Toast.LENGTH_SHORT).show();
    }

    private void previewQuiz() {
        // TODO: Implement quiz preview
        Toast.makeText(this, "Quiz preview coming soon", Toast.LENGTH_SHORT).show();
    }

    private void clearLectureFields() {
        etLectureTitle.setText("");
        etLectureDescription.setText("");
        etLectureContent.setText("");
        spLectureCourse.setSelection(0);
        spLectureLevel.setSelection(0);
    }

    private void clearQuizFields() {
        etQuizTitle.setText("");
        etQuizDescription.setText("");
        etQuestion.setText("");
        etOption1.setText("");
        etOption2.setText("");
        etOption3.setText("");
        etOption4.setText("");
        spQuizCourse.setSelection(0);
        spQuizLevel.setSelection(0);
        spCorrectAnswer.setSelection(0);
    }

    // Inner classes for content models
    private static class Lecture {
        private String title, description, content, course, level;

        public Lecture(String title, String description, String content, String course, String level) {
            this.title = title;
            this.description = description;
            this.content = content;
            this.course = course;
            this.level = level;
        }
    }

    private static class Quiz {
        private String title, description, question, course, level;
        private String[] options;
        private int correctAnswer;

        public Quiz(String title, String description, String question, String[] options, 
                   int correctAnswer, String course, String level) {
            this.title = title;
            this.description = description;
            this.question = question;
            this.options = options;
            this.correctAnswer = correctAnswer;
            this.course = course;
            this.level = level;
        }
    }
}