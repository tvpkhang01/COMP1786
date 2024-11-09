package com.example.admin;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class CourseEditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_editor);
        EditText name = findViewById(R.id.name);
        EditText time = findViewById(R.id.time);
        EditText capacity = findViewById(R.id.capacity);
        EditText duration = findViewById(R.id.duration);
        EditText price = findViewById(R.id.price);
        RadioGroup type = findViewById(R.id.type);
        RadioButton flowYoga = findViewById(R.id.flowYoga);
        RadioButton aerialYoga = findViewById(R.id.aerialYoga);
        RadioButton familyYoga = findViewById(R.id.familyYoga);
        EditText description = findViewById(R.id.description);
        Button save = findViewById(R.id.save);
        Database database = new Database(this);


        if (getIntent().hasExtra("Id")) {

            int Id = getIntent().getIntExtra("Id", -1);
            Log.d("CourseEditorActivity", "Course ID: " + Id);
            Course course = database.readCourse(Id);
            Log.d("CourseEditorActivity", "Time: " + course.getTime());
            Log.d("CourseEditorActivity", "Duration: " + course.getDuration());
            if (course != null) {
                if (course.getTime() != null) {
                    time.setText(course.getTime());
                } else {
                    time.setText("");
                }
                duration.setText(course.getDuration());
                price.setText(String.valueOf(course.getPrice()));
                capacity.setText(course.getCapacity());
                switch (course.getType()) {
                    case "Flow Yoga":
                        flowYoga.setChecked(true);
                        break;
                    case "Aerial Yoga":
                        aerialYoga.setChecked(true);
                        break;
                    case "Family Yoga":
                        familyYoga.setChecked(true);
                        break;
                }
                description.setText(course.getDescription());
            }
            save.setOnClickListener(v -> {
                if (name.getText().toString().equals("")) {
                    name.setError("Please enter name");
                    return;
                }
                if (time.getText().toString().isEmpty()) {
                    time.setError("Please enter time");
                    return;
                }
                if (capacity.getText().toString().isEmpty()) {
                    capacity.setError("Please enter capacity");
                    return;
                }
                if (duration.getText().toString().isEmpty()) {
                    duration.setError("Please enter duration");
                    return;
                }
                if (price.getText().toString().isEmpty()) {
                    price.setError("Please enter price");
                    return;
                }
                if (type.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(this, "Please select type of class", Toast.LENGTH_SHORT).show();
                    return;
                }

//                course.setDay(String.join(", ", selectedDaysList));
                course.setTime(time.getText().toString());
                course.setCapacity(Integer.parseInt(capacity.getText().toString()));
                course.setDuration(Integer.parseInt(duration.getText().toString()));
                course.setPrice(Double.parseDouble(price.getText().toString()));
                RadioButton radioButton = findViewById(type.getCheckedRadioButtonId());
                course.setType(radioButton.getText().toString());
                course.setDescription(description.getText().toString());
                database.updateCourse(course);
                Toast.makeText(this, "Course saved successfully", Toast.LENGTH_SHORT).show();
                finish();
            });
        } else {
            save.setOnClickListener(v -> {
                if (name.getText().toString().equals("")) {
                    name.setError("Please enter name");
                    return;
                }
                if (time.getText().toString().trim().isEmpty()) {
                    time.setError("Please enter time");
                    return;
                }
                if (capacity.getText().toString().trim().isEmpty()) {
                    capacity.setError("Please enter capacity");
                    return;
                }
                if (duration.getText().toString().trim().isEmpty()) {
                    duration.setError("Please enter duration");
                    return;
                }
                if (price.getText().toString().trim().isEmpty()) {
                    price.setError("Please enter price");
                    return;
                }
                if (type.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(this, "Please select type of class", Toast.LENGTH_SHORT).show();
                    return;
                }

                Course course = new Course();

//                course.setDay(String.join(", ", selectedDaysList));
                course.setTime(time.getText().toString());
                course.setCapacity(Integer.parseInt(capacity.getText().toString()));
                course.setDuration(Integer.parseInt(duration.getText().toString()));
                course.setPrice(Double.parseDouble(price.getText().toString()));
                RadioButton radioButton = findViewById(type.getCheckedRadioButtonId());
                course.setType(radioButton.getText().toString());
                course.setDescription(description.getText().toString());
                database.addCourse(course);
                Toast.makeText(this, "Course saved successfully", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }
}