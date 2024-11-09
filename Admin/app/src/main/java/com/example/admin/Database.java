package com.example.admin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    public Database(Context context) {
        super(context, "Database", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createCourseTable(db);
        createClassTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createCourseTable(SQLiteDatabase db) {
        String courseTable = "CREATE TABLE IF NOT EXISTS `Course` (" +
                "`Id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "`DayOfWeek` TEXT, " +
                "`Duration` INTEGER, " +
                "`Time` TEXT, " +
                "`Price` REAL, " +
                "`Capacity` INTEGER, " +
                "`Type` TEXT, " +
                "`Description` TEXT);";
        db.execSQL(courseTable);
    }

    public void addCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("DayOfWeek", course.getDayOfWeek());
        values.put("Duration", course.getDuration());
        values.put("Time", course.getTime());
        values.put("Price", course.getPrice());
        values.put("Capacity", course.getCapacity());
        values.put("Type", course.getType());
        values.put("Description", course.getDescription());

        db.insert("Course", null, values);
        db.close();
    }

    public ArrayList<Course> getCourses() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Course> courses = new ArrayList<>();
        String select = "SELECT * FROM `Course`;";
        Cursor cursor = db.rawQuery(select, null);

        if (cursor.moveToFirst()) {
            do {
                Course course = new Course();
                course.setId(cursor.getInt(0));
                course.setDayOfWeek(cursor.getString(1));
                course.setDuration(cursor.getInt(2));
                course.setTime(cursor.getString(3));
                course.setPrice(cursor.getDouble(4));
                course.setCapacity(cursor.getInt(5));
                course.setType(cursor.getString(6));
                course.setDescription(cursor.getString(7));
                courses.add(course);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return courses;
    }

    public Course readCourse(int Id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String select = "SELECT * FROM `Course` WHERE `Id` = ?;";
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(Id)});
        Course course = new Course();

        if (cursor.moveToFirst()) {
            course.setId(cursor.getInt(0));
            course.setDayOfWeek(cursor.getString(1));
            course.setDuration(cursor.getInt(2));
            course.setTime(cursor.getString(3));
            course.setPrice(cursor.getDouble(4));
            course.setCapacity(cursor.getInt(5));
            course.setType(cursor.getString(6));
            course.setDescription(cursor.getString(7));
        }
        cursor.close();
        return course;
    }

    public void updateCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("DayOfWeek", course.getDayOfWeek());
        values.put("Duration", course.getDuration());
        values.put("Time", course.getTime());
        values.put("Price", course.getPrice());
        values.put("Capacity", course.getCapacity());
        values.put("Type", course.getType());
        values.put("Description", course.getDescription());

        db.update("Course", values, "Id = ?", new String[]{String.valueOf(course.getId())});
        db.close();
    }

    public void deleteCourse(int Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Course", "Id = ?", new String[]{String.valueOf(Id)});
        db.close();
    }

    private void createClassTable(SQLiteDatabase db) {
        String classTable = "CREATE TABLE IF NOT EXISTS `Class` (" +
                "`Id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "`CourseId` INTEGER, " +
                "`Teacher` TEXT, " +
                "`Date` TEXT, " +
                "`Comment` TEXT, " +
                "FOREIGN KEY(`CourseId`) REFERENCES `Course`(`Id`)" +
                ");";
        db.execSQL(classTable);
    }

    public void addClass(Class classObj) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("CourseId", classObj.getCourseId());
        values.put("Teacher", classObj.getTeacher());
        values.put("Date", classObj.getDate());
        values.put("Comment", classObj.getComments());

        db.insert("Class", null, values);
        db.close();
    }

    public ArrayList<Class> getClasses() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Class> classes = new ArrayList<>();
        String select = "SELECT * FROM `Class`;";
        Cursor cursor = db.rawQuery(select, null);

        if (cursor.moveToFirst()) {
            do {
                Class classObj = new Class();
                classObj.setId(cursor.getInt(0));
                classObj.setCourseId(cursor.getInt(1));
                classObj.setTeacher(cursor.getString(2));
                classObj.setDate(cursor.getString(3));
                classObj.setComments(cursor.getString(4));
                classes.add(classObj);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return classes;
    }

    public Class readClass(int Id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String select = "SELECT * FROM `Class` WHERE `Id` = ?;";
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(Id)});
        Class classObj = new Class();

        if (cursor.moveToFirst()) {
            classObj.setId(cursor.getInt(0));
            classObj.setCourseId(cursor.getInt(1));
            classObj.setTeacher(cursor.getString(2));
            classObj.setDate(cursor.getString(3));
            classObj.setComments(cursor.getString(4));
        }
        cursor.close();
        return classObj;
    }

    public void updateClass(Class classObj) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("CourseId", classObj.getCourseId());
        values.put("Teacher", classObj.getTeacher());
        values.put("Date", classObj.getDate());
        values.put("Comment", classObj.getComments());

        db.update("Class", values, "Id = ?", new String[]{String.valueOf(classObj.getId())});
        db.close();
    }

    public void deleteClass(int Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Class", "Id = ?", new String[]{String.valueOf(Id)});
        db.close();
    }
}