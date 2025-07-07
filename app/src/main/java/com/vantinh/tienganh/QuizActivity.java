package com.vantinh.tienganh;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class QuizActivity extends AppCompatActivity {

    private TextView tvQuestion, tvQuestionNumber, tvTimer;
    private RadioGroup rgOptions;
    private RadioButton rbOption1, rbOption2, rbOption3, rbOption4;
    private Button btnNext, btnPrevious, btnSubmit;

    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int[] userAnswers;
    private int score = 0;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String quizId = "sampleQuizId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        initViews();
        loadQuestions();
        setupClickListeners();

    }

    private void initViews() {
        tvQuestion = findViewById(R.id.tv_question);
        tvQuestionNumber = findViewById(R.id.tv_question_number);
        tvTimer = findViewById(R.id.tv_timer);
        rgOptions = findViewById(R.id.rg_options);
        rbOption1 = findViewById(R.id.rb_option1);
        rbOption2 = findViewById(R.id.rb_option2);
        rbOption3 = findViewById(R.id.rb_option3);
        rbOption4 = findViewById(R.id.rb_option4);
        btnNext = findViewById(R.id.btn_next);
        btnPrevious = findViewById(R.id.btn_previous);
        btnSubmit = findViewById(R.id.btn_submit);
    }


    private void loadQuestions() {
        questionList = new ArrayList<>();
        // TODO: Lấy quizId thực tế từ Intent khi người dùng chọn một bài quiz cụ thể

        db.collection("quizzes").document(quizId).collection("questions")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Tự động chuyển đổi document thành đối tượng Question
                            Question question = document.toObject(Question.class);
                            questionList.add(question);
                        }

                        // Khởi tạo mảng lưu câu trả lời sau khi tải xong câu hỏi
                        userAnswers = new int[questionList.size()];
                        for (int i = 0; i < userAnswers.length; i++) {
                            userAnswers[i] = -1; // -1 nghĩa là chưa trả lời
                        }

                        // Hiển thị câu hỏi đầu tiên
                        displayQuestion();
                    } else {
                        Toast.makeText(this, "Failed to load questions.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupClickListeners() {
        btnNext.setOnClickListener(v -> {
            saveCurrentAnswer();
            if (currentQuestionIndex < questionList.size() - 1) {
                currentQuestionIndex++;
                displayQuestion();
            }
        });

        btnPrevious.setOnClickListener(v -> {
            saveCurrentAnswer();
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                displayQuestion();
            }
        });

        btnSubmit.setOnClickListener(v -> {
            saveCurrentAnswer();
            calculateScore();
            showResult();
        });
    }

    private void displayQuestion() {
        // Lấy câu hỏi hiện tại từ danh sách
        Question currentQuestion = questionList.get(currentQuestionIndex);

        // Hiển thị nội dung câu hỏi và số thứ tự
        tvQuestion.setText(currentQuestion.getQuestion());
        tvQuestionNumber.setText("Question " + (currentQuestionIndex + 1) + " of " + questionList.size());

        // --- PHẦN CẬP NHẬT CHÍNH ---
        // Lấy danh sách các lựa chọn
        List<String> options = currentQuestion.getOptions();

        // Hiển thị các lựa chọn lên RadioButton một cách an toàn
        // Đảm bảo không bị lỗi nếu câu hỏi có ít hơn 4 đáp án
        rbOption1.setText(options.size() > 0 ? options.get(0) : "");
        rbOption2.setText(options.size() > 1 ? options.get(1) : "");
        rbOption3.setText(options.size() > 2 ? options.get(2) : "");
        rbOption4.setText(options.size() > 3 ? options.get(3) : "");

        // Ẩn các RadioButton không cần thiết
        rbOption1.setVisibility(options.size() > 0 ? android.view.View.VISIBLE : android.view.View.GONE);
        rbOption2.setVisibility(options.size() > 1 ? android.view.View.VISIBLE : android.view.View.GONE);
        rbOption3.setVisibility(options.size() > 2 ? android.view.View.VISIBLE : android.view.View.GONE);
        rbOption4.setVisibility(options.size() > 3 ? android.view.View.VISIBLE : android.view.View.GONE);


        // Xóa lựa chọn của câu hỏi trước
        rgOptions.clearCheck();

        // Khôi phục lại lựa chọn của người dùng nếu họ quay lại câu hỏi này
        if (userAnswers[currentQuestionIndex] != -1) {
            switch (userAnswers[currentQuestionIndex]) {
                case 0: rbOption1.setChecked(true); break;
                case 1: rbOption2.setChecked(true); break;
                case 2: rbOption3.setChecked(true); break;
                case 3: rbOption4.setChecked(true); break;
            }
        }

        // Cập nhật trạng thái của các nút điều hướng
        btnPrevious.setEnabled(currentQuestionIndex > 0);
        btnNext.setEnabled(currentQuestionIndex < questionList.size() - 1);
        btnSubmit.setVisibility(currentQuestionIndex == questionList.size() - 1 ?
                android.view.View.VISIBLE : android.view.View.GONE);
    }

    private void saveCurrentAnswer() {
        int selectedId = rgOptions.getCheckedRadioButtonId();
        if (selectedId != -1) {
            if (selectedId == R.id.rb_option1) userAnswers[currentQuestionIndex] = 0;
            else if (selectedId == R.id.rb_option2) userAnswers[currentQuestionIndex] = 1;
            else if (selectedId == R.id.rb_option3) userAnswers[currentQuestionIndex] = 2;
            else if (selectedId == R.id.rb_option4) userAnswers[currentQuestionIndex] = 3;
        }
    }

    private void calculateScore() {
        score = 0;
        for (int i = 0; i < questionList.size(); i++) {
            if (userAnswers[i] == questionList.get(i).getCorrectAnswer()) {
                score++;
            }
        }
    }

    private void showResult() {
        String resultMessage = "Quiz Completed!\nScore: " + score + "/" + questionList.size();
        Toast.makeText(this, resultMessage, Toast.LENGTH_LONG).show();

        // Lưu kết quả vào Firestore
        saveResultToFirestore();

        // TODO: Điều hướng đến màn hình kết quả chi tiết thay vì chỉ đóng lại
        finish();
    }
    private void saveResultToFirestore() {
        String userId = mAuth.getCurrentUser().getUid();
        if (userId == null) return;

        Map<String, Object> resultData = new HashMap<>();
        resultData.put("userId", userId);
        resultData.put("quizId", quizId);
        resultData.put("score", score);
        resultData.put("totalQuestions", questionList.size());
        resultData.put("completedAt", System.currentTimeMillis());

        db.collection("quiz_results").add(resultData)
                .addOnSuccessListener(documentReference -> {
                    // Lưu thành công
                })
                .addOnFailureListener(e -> {
                    // Lưu thất bại
                });
    }

    // Inner class for Question model
    private static class Question {
        private String question;
        private List<String> options;
        private int correctAnswer;

        public Question() {} // Constructor rỗng cần thiết cho Firestore

        public String getQuestion() { return question; }
        public List<String> getOptions() { return options; }
        public int getCorrectAnswer() { return correctAnswer; }
    }
}