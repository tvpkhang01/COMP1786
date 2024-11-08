package com.example.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import java.util.Objects;

public class CourseEditorActivity extends AppCompatActivity {

    private EditText editTextCourseName;
    private TextView editTextDayOfWeek;
    private EditText editTextCapacity;

    private final StringBuilder selectedDaysText = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_editor);
    }
}