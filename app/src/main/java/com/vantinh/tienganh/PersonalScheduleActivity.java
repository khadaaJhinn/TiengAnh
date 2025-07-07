package com.vantinh.tienganh;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class PersonalScheduleActivity extends AppCompatActivity {

    private ListView lvSchedule;
    private List<ScheduleItem> scheduleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_schedule);

        initViews();
        loadSchedule();
        setupListView();
    }

    private void initViews() {
        lvSchedule = findViewById(R.id.lv_schedule);
    }

    private void loadSchedule() {
        // TODO: Load schedule from database
        scheduleList = new ArrayList<>();
        
        // Sample data
        scheduleList.add(new ScheduleItem("Monday", "09:00 - 10:30", "Basic English Grammar", "Room A101"));
        scheduleList.add(new ScheduleItem("Monday", "14:00 - 15:30", "English Conversation", "Room B202"));
        scheduleList.add(new ScheduleItem("Wednesday", "10:00 - 11:30", "Business English", "Room C303"));
        scheduleList.add(new ScheduleItem("Friday", "15:00 - 16:30", "TOEIC Preparation", "Room A101"));
        scheduleList.add(new ScheduleItem("Saturday", "09:00 - 10:30", "English Pronunciation", "Room B202"));
    }

    private void setupListView() {
        // TODO: Create custom adapter for schedule list
        // For now, using simple array adapter
        List<String> scheduleStrings = new ArrayList<>();
        for (ScheduleItem item : scheduleList) {
            scheduleStrings.add(item.getDay() + " " + item.getTime() + "\n" + 
                              item.getCourseName() + " - " + item.getLocation());
        }

        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(
                this, android.R.layout.simple_list_item_2, android.R.id.text1, scheduleStrings);
        lvSchedule.setAdapter(adapter);
    }

    // Inner class for ScheduleItem model
    private static class ScheduleItem {
        private String day;
        private String time;
        private String courseName;
        private String location;

        public ScheduleItem(String day, String time, String courseName, String location) {
            this.day = day;
            this.time = time;
            this.courseName = courseName;
            this.location = location;
        }

        public String getDay() { return day; }
        public String getTime() { return time; }
        public String getCourseName() { return courseName; }
        public String getLocation() { return location; }
    }
}