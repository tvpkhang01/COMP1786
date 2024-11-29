package com.example.admin;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CourseEditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_editor);

        EditText dayOfWeek = findViewById(R.id.dayOfWeek);
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

        ArrayList<String> selectedDaysList = new ArrayList<>();
        String[] daysArray = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        boolean[] selectedDays = new boolean[daysArray.length];

        dayOfWeek.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Days of the Week");
            builder.setMultiChoiceItems(daysArray, selectedDays, (dialog, which, isChecked) -> {
                if (isChecked) {
                    selectedDaysList.add(daysArray[which]);
                } else {
                    selectedDaysList.remove(daysArray[which]);
                }
            });

            builder.setPositiveButton("OK", (dialog, which) -> {
                dayOfWeek.setText(String.join(", ", selectedDaysList));
                dialog.dismiss();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builder.create().show();
        });

        time.setOnClickListener(v -> {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            TimePickerDialog startTimePicker = new TimePickerDialog(this, (view, startHour, startMinute) -> {
                LocalTime startTime = LocalTime.of(startHour, startMinute);

                TimePickerDialog endTimePicker = new TimePickerDialog(this, (view1, endHour, endMinute) -> {
                    LocalTime endTime = LocalTime.of(endHour, endMinute);

                    String formattedTime = startTime.format(timeFormatter) + "-" + endTime.format(timeFormatter);
                    time.setText(formattedTime);

                }, startTime.getHour(), startTime.getMinute(), true);
                endTimePicker.setTitle("Select End Time");
                endTimePicker.show();

            }, 0, 0, true);
            startTimePicker.setTitle("Select Start Time");
            startTimePicker.show();
        });

        if (getIntent().hasExtra("Id")) {
            int Id = getIntent().getIntExtra("Id", -1);
            Log.d("CourseEditorActivity", "Course ID: " + Id);
            Course course = database.readCourse(Id);
            Log.d("CourseEditorActivity", "Time: " + course.getTime());
            Log.d("CourseEditorActivity", "Duration: " + course.getDuration());
            Log.d("CourseEditorActivity", "Capacity: " + course.getCapacity());

            if (course != null) {
                dayOfWeek.setText(course.getDayOfWeek());
                time.setText(course.getTime());
                duration.setText(String.valueOf(course.getDuration()));
                price.setText(String.valueOf(course.getPrice()));
                capacity.setText(String.valueOf(course.getCapacity()));

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

            save.setOnClickListener(v -> saveCourseData(dayOfWeek, time, capacity, duration, price, type, description, database, course, selectedDaysList));
        } else {
            save.setOnClickListener(v -> saveCourseData(dayOfWeek, time, capacity, duration, price, type, description, database, new Course(), selectedDaysList));
        }
    }

    private void saveCourseData(EditText dayofWeek, EditText time, EditText capacity, EditText duration, EditText price, RadioGroup type, EditText description, Database database, Course course, ArrayList<String> selectedDaysList) {
        if (validateFields(dayofWeek, time, capacity, duration, price, type)) {
            if (!selectedDaysList.isEmpty()) {
                course.setDayOfWeek(String.join(", ", selectedDaysList));
            }
            course.setTime(time.getText().toString());
            course.setCapacity(Integer.parseInt(capacity.getText().toString()));
            course.setDuration(Integer.parseInt(duration.getText().toString()));
            course.setPrice(Double.parseDouble(price.getText().toString()));
            RadioButton radioButton = findViewById(type.getCheckedRadioButtonId());
            course.setType(radioButton.getText().toString());
            course.setDescription(description.getText().toString());
            Log.d("CourseEditorActivity", "Id: " + course.getId());
            if (course.getId() == 0) {
                database.addCourse(course);
                Toast.makeText(this, "Course saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                database.updateCourse(course);
                Toast.makeText(this, "Course updated successfully", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

    private boolean validateFields(EditText dayofWeek, EditText time, EditText capacity, EditText duration, EditText price, RadioGroup type) {
        if (dayofWeek.getText().toString().isEmpty()) {
            dayofWeek.setError("Please enter at least one day");
        }
        if (time.getText().toString().isEmpty()) {
            time.setError("Please enter time");
            return false;
        }
        if (capacity.getText().toString().isEmpty()) {
            capacity.setError("Please enter capacity");
            return false;
        }
        if (duration.getText().toString().isEmpty()) {
            duration.setError("Please enter duration");
            return false;
        }
        if (price.getText().toString().isEmpty()) {
            price.setError("Please enter price");
            return false;
        }
        if (type.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select type of class", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
