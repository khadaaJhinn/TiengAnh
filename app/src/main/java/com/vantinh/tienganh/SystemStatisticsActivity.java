package com.vantinh.tienganh;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SystemStatisticsActivity extends AppCompatActivity {

    private TextView tvTotalUsers, tvActiveUsers, tvTotalCourses, tvTotalQuizzes;
    private TextView tvCompletedCourses, tvAverageScore, tvTotalStudyHours;
    private ListView lvPopularCourses, lvRecentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_statistics);

        initViews();
        loadStatistics();
        displayStatistics();
        setupListViews();
    }

    private void initViews() {
        tvTotalUsers = findViewById(R.id.tv_total_users);
        tvActiveUsers = findViewById(R.id.tv_active_users);
        tvTotalCourses = findViewById(R.id.tv_total_courses);
        tvTotalQuizzes = findViewById(R.id.tv_total_quizzes);
        tvCompletedCourses = findViewById(R.id.tv_completed_courses);
        tvAverageScore = findViewById(R.id.tv_average_score);
        tvTotalStudyHours = findViewById(R.id.tv_total_study_hours);
        lvPopularCourses = findViewById(R.id.lv_popular_courses);
        lvRecentActivity = findViewById(R.id.lv_recent_activity);
    }

    private void loadStatistics() {
        // TODO: Load statistics from database
        // For now, using sample data
    }

    private void displayStatistics() {
        // Sample statistics data
        tvTotalUsers.setText("Total Users: 1,250");
        tvActiveUsers.setText("Active Users: 890");
        tvTotalCourses.setText("Total Courses: 25");
        tvTotalQuizzes.setText("Total Quizzes: 150");
        tvCompletedCourses.setText("Completed Courses: 3,420");
        tvAverageScore.setText("Average Score: 78.5%");
        tvTotalStudyHours.setText("Total Study Hours: 12,450");
    }

    private void setupListViews() {
        setupPopularCoursesList();
        setupRecentActivityList();
    }

    private void setupPopularCoursesList() {
        List<PopularCourse> popularCourses = new ArrayList<>();
        popularCourses.add(new PopularCourse("Basic English Grammar", 450, 85.2f));
        popularCourses.add(new PopularCourse("English Conversation", 380, 82.7f));
        popularCourses.add(new PopularCourse("TOEIC Preparation", 320, 79.3f));
        popularCourses.add(new PopularCourse("Business English", 280, 81.8f));
        popularCourses.add(new PopularCourse("English Pronunciation", 250, 88.1f));

        List<String> courseStrings = new ArrayList<>();
        for (PopularCourse course : popularCourses) {
            courseStrings.add(course.getName() + "\n" +
                            "Enrollments: " + course.getEnrollments() + 
                            " | Avg Score: " + course.getAverageScore() + "%");
        }

        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(
                this, android.R.layout.simple_list_item_2, android.R.id.text1, courseStrings);
        lvPopularCourses.setAdapter(adapter);
    }

    private void setupRecentActivityList() {
        List<RecentActivity> recentActivities = new ArrayList<>();
        recentActivities.add(new RecentActivity("User Registration", "John Doe registered", "2 minutes ago"));
        recentActivities.add(new RecentActivity("Course Completion", "Jane Smith completed Basic Grammar", "15 minutes ago"));
        recentActivities.add(new RecentActivity("Quiz Submission", "Bob Johnson submitted TOEIC Quiz #5", "30 minutes ago"));
        recentActivities.add(new RecentActivity("New Course", "Teacher Alice created Business English Module 3", "1 hour ago"));
        recentActivities.add(new RecentActivity("Feedback", "Charlie Wilson submitted feedback", "2 hours ago"));

        List<String> activityStrings = new ArrayList<>();
        for (RecentActivity activity : recentActivities) {
            activityStrings.add(activity.getType() + "\n" +
                              activity.getDescription() + " - " + activity.getTimestamp());
        }

        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(
                this, android.R.layout.simple_list_item_2, android.R.id.text1, activityStrings);
        lvRecentActivity.setAdapter(adapter);
    }

    // Inner classes for statistics models
    private static class PopularCourse {
        private String name;
        private int enrollments;
        private float averageScore;

        public PopularCourse(String name, int enrollments, float averageScore) {
            this.name = name;
            this.enrollments = enrollments;
            this.averageScore = averageScore;
        }

        public String getName() { return name; }
        public int getEnrollments() { return enrollments; }
        public float getAverageScore() { return averageScore; }
    }

    private static class RecentActivity {
        private String type;
        private String description;
        private String timestamp;

        public RecentActivity(String type, String description, String timestamp) {
            this.type = type;
            this.description = description;
            this.timestamp = timestamp;
        }

        public String getType() { return type; }
        public String getDescription() { return description; }
        public String getTimestamp() { return timestamp; }
    }
}