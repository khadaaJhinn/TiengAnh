package com.vantinh.tienganh;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class CourseListActivity extends AppCompatActivity {

    private ListView lvCourses;
    private List<Course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        initViews();
        loadCourses();
        setupListView();
    }

    private void initViews() {
        lvCourses = findViewById(R.id.lv_courses);
    }

    private void loadCourses() {
        // TODO: Load courses from database
        courseList = new ArrayList<>();
        
        // Sample data
        courseList.add(new Course("1", "Basic English Grammar", "Learn fundamental English grammar rules", "Beginner", 30));
        courseList.add(new Course("2", "English Conversation", "Improve your speaking skills", "Intermediate", 25));
        courseList.add(new Course("3", "Business English", "Professional English for workplace", "Advanced", 40));
        courseList.add(new Course("4", "TOEIC Preparation", "Prepare for TOEIC exam", "Intermediate", 50));
        courseList.add(new Course("5", "English Pronunciation", "Master English pronunciation", "Beginner", 20));
    }

    private void setupListView() {
        // TODO: Create custom adapter for course list
        // For now, using simple array adapter with course names
        List<String> courseNames = new ArrayList<>();
        for (Course course : courseList) {
            courseNames.add(course.getName() + " (" + course.getLevel() + ")");
        }

        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, courseNames);
        lvCourses.setAdapter(adapter);

        lvCourses.setOnItemClickListener((parent, view, position, id) -> {
            Course selectedCourse = courseList.get(position);
            handleCourseEnrollment(selectedCourse);
        });
    }

    private void handleCourseEnrollment(Course course) {
        // TODO: Implement course enrollment logic
        Toast.makeText(this, "Enrolled in: " + course.getName(), Toast.LENGTH_SHORT).show();
    }

    // Inner class for Course model
    private static class Course {
        private String id;
        private String name;
        private String description;
        private String level;
        private int duration; // in hours

        public Course(String id, String name, String description, String level, int duration) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.level = level;
            this.duration = duration;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getLevel() { return level; }
        public int getDuration() { return duration; }
    }
}