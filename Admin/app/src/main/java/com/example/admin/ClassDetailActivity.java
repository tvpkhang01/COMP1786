package com.example.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class ClassDetailActivity extends AppCompatActivity {

    private EditText classDate, classTeacher, classComments;
    private Button saveClassButton;
    private long courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail);

        // Initialize views
        classDate = findViewById(R.id.classDate);
        classTeacher = findViewById(R.id.classTeacher);
        classComments = findViewById(R.id.classComments);
        saveClassButton = findViewById(R.id.saveClassButton);

        // Get courseId from Intent
        courseId = getIntent().getLongExtra("COURSE_ID", -1);

        saveClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveClassDetails();
            }
        });
    }

    private void saveClassDetails() {
        // Retrieve entered data
        String date = classDate.getText().toString();
        String teacher = classTeacher.getText().toString();
        String comments = classComments.getText().toString();

        // Use courseId to link this class to the specific course in the database
        // Add or update the class session in the database
    }
}