package com.example.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    private Database database;
    private ListView classes_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button add = findViewById(R.id.addClass);
        classes_list = findViewById(R.id.classes_list);
        database = new Database(this);

        add.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity2.this, ClassEditorActivity.class);
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
        ArrayList<Class> classes = database.getClasses();
        classes_list.setAdapter(new MainActivity2.ListAdapter(classes));
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

            return convertView;
        }
    }
}