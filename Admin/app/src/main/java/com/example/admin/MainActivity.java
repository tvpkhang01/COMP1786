package com.example.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Database database;
    private ListView courses_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button add = findViewById(R.id.add);
        courses_list = findViewById(R.id.courses_list);
        database = new Database(this);

        add.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CourseEditorActivity.class);
            startActivity(intent);
        });
        refreshList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        ArrayList<Course> courses = database.getCourses();
        courses_list.setAdapter(new ListAdapter(courses));
    }

    public class ListAdapter extends BaseAdapter {
        private final ArrayList<Course> courses;

        public ListAdapter(ArrayList<Course> courses) {
            this.courses = courses;
        }

        @Override
        public int getCount() {
            return courses.size();
        }

        @Override
        public Object getItem(int position) {
            return courses.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
            }

            TextView name = convertView.findViewById(R.id.name);
            Button edit = convertView.findViewById(R.id.edit);
            Button delete = convertView.findViewById(R.id.delete);
            Course course = courses.get(position);

            name.setText("Course: " + course.getId());

            name.setOnClickListener(v-> {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            });

            edit.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, CourseEditorActivity.class);
                intent.putExtra("Id", course.getId());
                startActivity(intent);
            });

            delete.setOnClickListener(v -> showDeleteConfirmation(course));

            return convertView;
        }

        private void showDeleteConfirmation(Course course) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Delete Course")
                    .setMessage("Are you sure you want to delete this course?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Toast.makeText(MainActivity.this, "Course deleted successfully", Toast.LENGTH_SHORT).show();
                        database.deleteCourse(course.getId());
                        refreshList();
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }
}
