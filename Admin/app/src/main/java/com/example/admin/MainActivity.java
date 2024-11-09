package com.example.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            @SuppressLint("ViewHolder")
            View view = getLayoutInflater().inflate(R.layout.course_item, null);
            view.findViewById(R.id.course_layout);
            TextView name = view.findViewById(R.id.name);
            Button edit = view.findViewById(R.id.edit);
            Button delete = view.findViewById(R.id.delete);
            Course course = courses.get(position);

            name.setText("Course " + course.getId());
            edit.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, CourseEditorActivity.class);
                intent.putExtra("Id", course.getId());
                startActivity(intent);
            });
            delete.setOnClickListener(v -> {
                database.deleteCourse(course.getId());
            });
            return view;
        }
    }
}