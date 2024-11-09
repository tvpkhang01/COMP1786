package com.example.admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;


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

        List<String> selectedDaysList = new ArrayList<>();


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

            RadioButton selectedType = findViewById(type.getCheckedRadioButtonId());
            String typeString = selectedType.getText().toString();

            Course course = new Course();
            course.setName(name.getText().toString());
            course.setDay(String.join(", ", selectedDaysList));
            course.setTime(time.getText().toString().trim());
            course.setCapacity(Integer.parseInt(capacity.getText().toString().trim()));
            course.setDuration(Integer.parseInt(duration.getText().toString().trim()));
            course.setPrice(Double.parseDouble(price.getText().toString().trim()));
            course.setType(typeString);
            course.setDescription(description.getText().toString().trim());
            database.addCourse(course);
            Toast.makeText(this, "Course saved successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}