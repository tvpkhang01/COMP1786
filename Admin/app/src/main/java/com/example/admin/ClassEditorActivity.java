package com.example.admin;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class ClassEditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_editor);

        EditText teacher = findViewById(R.id.teacher);
        EditText date = findViewById(R.id.date);
        EditText comments = findViewById(R.id.comments);
        Button save = findViewById(R.id.save);
        Database database = new Database(this);

        ArrayList<Integer> allowedDays = getIntent().getIntegerArrayListExtra("allowedDays");
        LocalDate liveDate = LocalDate.now();
        DateTimeFormatter shortDate = DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("vi", "VN"));
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, dayOfMonth) -> {
            LocalDate selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
            int dayOfWeekInt = selectedDate.getDayOfWeek().getValue() % 7 + 1;
            boolean isAllowedDay = allowedDays != null && allowedDays.contains(dayOfWeekInt);
            if (isAllowedDay) {
                date.setText(selectedDate.format(shortDate));
            } else {
                Toast.makeText(this, "Please select a valid day according to the course schedule", Toast.LENGTH_SHORT).show();
            }
        }, liveDate.getYear(), liveDate.getMonthValue() - 1, liveDate.getDayOfMonth());

        date.setOnClickListener(v -> datePickerDialog.show());

        if (getIntent().hasExtra("Id")) {
            int Id = getIntent().getIntExtra("Id", -1);
            Log.d("ClassEditorActivity", "Class ID: " + Id);
            Class classItem = database.readClass(Id);
            if (classItem != null) {
                teacher.setText(classItem.getTeacher());
                date.setText(classItem.getDate());
                comments.setText(classItem.getComments());
            }
            save.setOnClickListener(v -> saveClassData(teacher, date, comments, database, classItem));
        } else {
            save.setOnClickListener(v -> saveClassData(teacher, date, comments, database, new Class()));
        }
    }

    private void saveClassData(EditText teacher, EditText date, EditText comments, Database database, Class classItem) {
        if (validateFields(teacher, date)) {
            classItem.setTeacher(teacher.getText().toString());
            classItem.setDate(date.getText().toString());
            classItem.setComments(comments.getText().toString());
            int courseId = getIntent().getIntExtra("courseId", -1);
            Log.d("ClassEditorActivity", "Course Id: " + courseId);
            classItem.setCourseId(courseId);
            Log.d("ClassEditorActivity", "Id: " + classItem.getId());
            if (classItem.getId() == 0) {
                database.addClass(classItem);
                Toast.makeText(this, "Class saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                database.updateClass(classItem);
                Toast.makeText(this, "Class updated successfully", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

    private boolean validateFields(EditText teacher, EditText date) {
        if (teacher.getText().toString().isEmpty()) {
            teacher.setError("Please enter teacher name");
            return false;
        }
        if (date.getText().toString().isEmpty()) {
            date.setError("Please enter date");
            return false;
        }

        return true;
    }
}