package com.example.admin;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    public Database(Context context) {
        super(context, "Database", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createTable() {
String courseTable = "CREATE TABLE IF NOT EXISTS `COURSE` ( `Id` INTEGER PRIMARY KEY AUTOINCREMENT, `Course` TEXT, `Description` TEXT, `Priority` TEXT,"
    }
}
