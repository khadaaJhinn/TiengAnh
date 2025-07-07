package com.vantinh.tienganh;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class StudyProgressActivity extends AppCompatActivity {

    private TextView tvOverallProgress, tvCompletedCourses, tvTotalHours;
    private ProgressBar pbOverallProgress;
    private ListView lvCourseProgress;
    private List<CourseProgress> progressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_progress);

        initViews();
        loadProgressData();
        displayOverallProgress();
        setupProgressList();
    }

    private void initViews() {
        tvOverallProgress = findViewById(R.id.tv_overall_progress);
        tvCompletedCourses = findViewById(R.id.tv_completed_courses);
        tvTotalHours = findViewById(R.id.tv_total_hours);
        pbOverallProgress = findViewById(R.id.pb_overall_progress);
        lvCourseProgress = findViewById(R.id.lv_course_progress);
    }

    private void loadProgressData() {
        // TODO: Load progress data from database
        progressList = new ArrayList<>();
        
        // Sample data
        progressList.add(new CourseProgress("Basic English Grammar", 85, 25, 30, "In Progress"));
        progressList.add(new CourseProgress("English Conversation", 100, 25, 25, "Completed"));
        progressList.add(new CourseProgress("Business English", 60, 24, 40, "In Progress"));
        progressList.add(new CourseProgress("TOEIC Preparation", 30, 15, 50, "In Progress"));
        progressList.add(new CourseProgress("English Pronunciation", 100, 20, 20, "Completed"));
    }

    private void displayOverallProgress() {
        int totalCourses = progressList.size();
        int completedCourses = 0;
        int totalHoursStudied = 0;
        int totalProgressSum = 0;

        for (CourseProgress progress : progressList) {
            if (progress.getStatus().equals("Completed")) {
                completedCourses++;
            }
            totalHoursStudied += progress.getHoursStudied();
            totalProgressSum += progress.getProgressPercentage();
        }

        int overallProgress = totalCourses > 0 ? totalProgressSum / totalCourses : 0;

        tvOverallProgress.setText("Overall Progress: " + overallProgress + "%");
        tvCompletedCourses.setText("Completed Courses: " + completedCourses + "/" + totalCourses);
        tvTotalHours.setText("Total Study Hours: " + totalHoursStudied);
        pbOverallProgress.setProgress(overallProgress);
    }

    private void setupProgressList() {
        // TODO: Create custom adapter for progress list
        // For now, using simple array adapter
        List<String> progressStrings = new ArrayList<>();
        for (CourseProgress progress : progressList) {
            progressStrings.add(progress.getCourseName() + "\n" +
                              "Progress: " + progress.getProgressPercentage() + "% | " +
                              "Hours: " + progress.getHoursStudied() + "/" + progress.getTotalHours() + " | " +
                              "Status: " + progress.getStatus());
        }

        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(
                this, android.R.layout.simple_list_item_2, android.R.id.text1, progressStrings);
        lvCourseProgress.setAdapter(adapter);
    }

    // Inner class for CourseProgress model
    private static class CourseProgress {
        private String courseName;
        private int progressPercentage;
        private int hoursStudied;
        private int totalHours;
        private String status;

        public CourseProgress(String courseName, int progressPercentage, int hoursStudied, 
                            int totalHours, String status) {
            this.courseName = courseName;
            this.progressPercentage = progressPercentage;
            this.hoursStudied = hoursStudied;
            this.totalHours = totalHours;
            this.status = status;
        }

        public String getCourseName() { return courseName; }
        public int getProgressPercentage() { return progressPercentage; }
        public int getHoursStudied() { return hoursStudied; }
        public int getTotalHours() { return totalHours; }
        public String getStatus() { return status; }
    }
}