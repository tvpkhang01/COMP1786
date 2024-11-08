package com.example.admin;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class CourseDetailActivity extends AppCompatActivity {

    private TextView courseName, courseDay; // Add more TextViews for other course details
    private Button addClassButton;
    private RecyclerView classRecyclerView;
    private long courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        // Initialize views
        courseName = findViewById(R.id.courseName);
        courseDay = findViewById(R.id.courseDay);
        addClassButton = findViewById(R.id.addClassButton);
        classRecyclerView = findViewById(R.id.classRecyclerView);

        // Get courseId from Intent
        courseId = getIntent().getLongExtra("COURSE_ID", -1);
        loadCourseDetails(courseId);

        addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open ClassDetailActivity for adding a new class
                Intent intent = new Intent(CourseDetailActivity.this, ClassDetailActivity.class);
                intent.putExtra("COURSE_ID", courseId);
                startActivity(intent);
            }
        });
    }

    private void loadCourseDetails(long courseId) {
        // Load course details from the database using the courseId
        // For example:
        // Course course = dbHelper.getCourse(courseId);
        // courseName.setText(course.getCourseName());
        // courseDay.setText(course.getDay());
        // Load other course details
    }
}