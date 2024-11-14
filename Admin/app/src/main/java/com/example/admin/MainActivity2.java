package com.example.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity2 extends AppCompatActivity {

    private Database database;
    private ListView classes_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button add = findViewById(R.id.add);
        classes_list = findViewById(R.id.classes_list);
        database = new Database(this);
        int courseId = getIntent().getIntExtra("courseId", -1);
        EditText search_text = findViewById(R.id.search_text);
        Button search_button = findViewById(R.id.search_button);
        search_button.setOnClickListener(v -> {
            String teacher = search_text.getText().toString();
            Log.d("Teacher", teacher);
            Log.d("Course Id", String.valueOf(courseId));
            if (teacher.isEmpty()) {
                refreshList(courseId);
            } else {
                refreshList(courseId, teacher);
            }
        });
        Course course = database.readCourse(courseId);
        String dayOfWeek = course.getDayOfWeek();
        ArrayList<Integer> allowedDays = getAllowedDays(dayOfWeek);
        add.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity2.this, ClassEditorActivity.class);
            intent.putExtra("courseId", courseId);
            intent.putIntegerArrayListExtra("allowedDays", allowedDays);
            startActivity(intent);
        });
        refreshList(courseId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int courseId = getIntent().getIntExtra("courseId", -1);
        refreshList(courseId);
    }

    private void refreshList(int courseId) {
        if (courseId != -1) {
            ArrayList<Class> classes = database.getClasses(courseId);
            classes_list.setAdapter(new MainActivity2.ListAdapter(classes));
        }
    }

    private void refreshList(int courseId, String teacher) {
        if (courseId != -1) {
            ArrayList<Class> classes = database.getClassesByTeacher(courseId, teacher);
            classes_list.setAdapter(new MainActivity2.ListAdapter(classes));
        }
    }

    private ArrayList<Integer> getAllowedDays(String dayOfWeek) {
        ArrayList<Integer> allowedDays = new ArrayList<>();
        String[] days = dayOfWeek.split(", ");
        for (String day : days) {
            switch (day.trim()) {
                case "Monday":
                    allowedDays.add(Calendar.MONDAY);
                    break;
                case "Tuesday":
                    allowedDays.add(Calendar.TUESDAY);
                    break;
                case "Wednesday":
                    allowedDays.add(Calendar.WEDNESDAY);
                    break;
                case "Thursday":
                    allowedDays.add(Calendar.THURSDAY);
                    break;
                case "Friday":
                    allowedDays.add(Calendar.FRIDAY);
                    break;
                case "Saturday":
                    allowedDays.add(Calendar.SATURDAY);
                    break;
                case "Sunday":
                    allowedDays.add(Calendar.SUNDAY);
                    break;
            }

        }
        return allowedDays;
    }

    public class ListAdapter extends BaseAdapter {
        private final ArrayList<Class> classes;

        public ListAdapter(ArrayList<Class> classes) {
            this.classes = classes;
        }

        @Override
        public int getCount() {
            return classes.size();
        }

        @Override
        public Object getItem(int position) {
            return classes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item, parent, false);
            }
            TextView name = convertView.findViewById(R.id.name);
            Button edit = convertView.findViewById(R.id.edit);
            Button delete = convertView.findViewById(R.id.delete);
            Class classItem = classes.get(position);
            name.setText("Class " + classItem.getId());
            edit.setOnClickListener(v -> {
                int courseId = getIntent().getIntExtra("courseId", -1);
                Course course = database.readCourse(courseId);
                String dayOfWeek = course.getDayOfWeek();
                ArrayList<Integer> allowedDays = getAllowedDays(dayOfWeek);
                Intent intent = new Intent(MainActivity2.this, ClassEditorActivity.class);
                intent.putExtra("Id", classItem.getId());
                intent.putExtra("courseId", courseId);
                intent.putIntegerArrayListExtra("allowedDays", allowedDays);
                startActivity(intent);
            });
            delete.setOnClickListener(v -> {
                new AlertDialog.Builder(MainActivity2.this)
                        .setTitle("Delete Course")
                        .setMessage("Are you sure you want to delete this class?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            Toast.makeText(MainActivity2.this, "Course deleted successfully", Toast.LENGTH_SHORT).show();
                            database.deleteClass(classItem.getId());
                            int courseId = getIntent().getIntExtra("courseId", -1);
                            refreshList(courseId);
                        })
                        .setNegativeButton("No", null)
                        .show();
            });
            return convertView;
        }


    }
}