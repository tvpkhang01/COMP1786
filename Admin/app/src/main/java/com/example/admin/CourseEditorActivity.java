package com.example.admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import androidx.appcompat.app.AppCompatActivity;


public class CourseEditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_editor);
        EditText name = findViewById(R.id.name);
        EditText dayOfWeek = findViewById(R.id.dayOfWeek);
        EditText time = findViewById(R.id.time);
        EditText capacity = findViewById(R.id.capacity);
        EditText duration = findViewById(R.id.duration);
        EditText price = findViewById(R.id.price);
        RadioGroup type = findViewById(R.id.type);
        RadioButton flowYoga = findViewById(R.id.flowYoga);
        RadioButton aerialYoga = findViewById(R.id.aerialYoga);
        RadioButton familyYoga = findViewById(R.id.familyYoga);
        Button save = findViewById(R.id.save);
        Database database = new Database(this);



        save.setOnClickListener(v -> {
            if (name.getText().toString().equals("")) {
                name.setError("Please enter name");
                return;
            }

            Course course = new Course();
            course.setName(name.getText().toString());
            database.addCourse(course);
            finish();
        });
    }
}