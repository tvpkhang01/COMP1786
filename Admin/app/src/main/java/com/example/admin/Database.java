package com.example.admin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Database";
    private static final int DATABASE_VERSION = 1;
    private final String DATABASE_URL = "https://comp-1786-database-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private DatabaseReference firebaseDatabase;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        firebaseDatabase = FirebaseDatabase.getInstance(DATABASE_URL).getReference();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createCourseTable(db);
        createClassTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS `Course`");
        db.execSQL("DROP TABLE IF EXISTS `Class`");
        onCreate(db);
    }

    public void syncDataToFirebase() {
        firebaseDatabase.child("courses").removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                firebaseDatabase.child("classes").removeValue().addOnCompleteListener(classTask -> {
                    if (classTask.isSuccessful()) {
                        ArrayList<Course> courses = getCourses();
                        for (Course course : courses) {
                            String courseId = String.valueOf(course.getId());
                            firebaseDatabase.child("courses").child(courseId).setValue(course)
                                    .addOnSuccessListener(aVoid -> Log.d("Database", "Course synced to Firebase: " + courseId))
                                    .addOnFailureListener(e -> Log.e("Database", "Failed to sync course: " + courseId + " - " + e.getMessage()));
                        }
                        for (Course course : courses) {
                            int courseId = course.getId();
                            ArrayList<Class> classes = getClasses(courseId);
                            for (Class classObj : classes) {
                                String classId = String.valueOf(classObj.getId());
                                firebaseDatabase.child("classes").child(classId).setValue(classObj)
                                        .addOnSuccessListener(aVoid -> Log.d("Database", "Class synced to Firebase: " + classId))
                                        .addOnFailureListener(e -> Log.e("Database", "Failed to sync class: " + classId + " - " + e.getMessage()));
                            }
                        }
                        Log.d("Database", "Firebase sync successful!");
                    } else {
                        Log.e("Database", "Failed to delete classes data from Firebase");
                    }
                });
            } else {
                Log.e("Database", "Failed to delete courses data from Firebase");
            }
        });
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
        this.syncDataToFirebase();
        db.close();
    }

    public ArrayList<Course> getCourses() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Course> courses = new ArrayList<>();
        String select = "SELECT * FROM `Course`";
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
        db.close();
        return courses;
    }

    public Course readCourse(int Id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String select = "SELECT * FROM `Course` WHERE `Id` = ?";
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
        db.close();
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
        this.syncDataToFirebase();
        db.close();
    }

    public void deleteCourse(int courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Class", "CourseId = ?", new String[]{String.valueOf(courseId)});
        db.delete("Course", "Id = ?", new String[]{String.valueOf(courseId)});
        this.syncDataToFirebase();
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
        this.syncDataToFirebase();
        db.close();
    }

    public ArrayList<Class> getClasses(int courseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Class> classes = new ArrayList<>();
        String select = "SELECT * FROM `Class` WHERE CourseId = ?;";
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(courseId)});

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

    public ArrayList<Class> getClassesByTeacher(int courseId, String teacher) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Class> classes = new ArrayList<>();
        String select = "SELECT * FROM `Class` WHERE CourseId = ? AND teacher LIKE ? COLLATE NOCASE;";
        String searchPattern = "%" + teacher + "%";
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(courseId), searchPattern});
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
        this.syncDataToFirebase();
        db.close();
    }

    public void deleteClass(int Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Class", "Id = ?", new String[]{String.valueOf(Id)});
        this.syncDataToFirebase();
        db.close();
    }
}