package com.example.todo_app.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.todo_app.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TODO_DATABASE";
    private static final String TABLE_NAME = "TODO_TABLE";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "TASK";
    private static final String COL_3 = "STATUS";

    // Table names and columns for users
    private static final String TABLE_NAME_USERS = "users";
    private static final String COL_USER_ID = "id";
    private static final String COL_USER_NAME = "name";
    private static final String COL_USER_EMAIL = "email";
    private static final String COL_USER_PASSWORD = "password";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TASK TEXT, STATUS INTEGER)");
        String createUsersTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_NAME + " TEXT, " +
                COL_USER_EMAIL + " TEXT, " +
                COL_USER_PASSWORD + " TEXT)";
        db.execSQL(createUsersTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USERS);
        onCreate(db);
    }

    // User authentication method
    public boolean loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_USERS, null, COL_USER_EMAIL + "=? AND " + COL_USER_PASSWORD + "=?", new String[]{email, password}, null, null, null);
        boolean isLoggedIn = cursor != null && cursor.getCount() > 0;
        if (cursor != null)
            cursor.close();
        db.close();
        return isLoggedIn;
    }

    // Register new user
    public void registerUser(String name, String email, String password, String confirmPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_NAME, name);
        values.put(COL_USER_EMAIL, email);
        values.put(COL_USER_PASSWORD, password);
        values.put(COL_USER_PASSWORD, confirmPassword);
        db.insert(TABLE_NAME_USERS, null, values);
        db.close();
    }

    public void insertTask(ToDoModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, model.getTask());
        values.put(COL_3, 0);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void updateTask(int id, String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, task);
        db.update(TABLE_NAME, values, "ID=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void updateStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_3, status);
        db.update(TABLE_NAME, values, "ID=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "ID=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<ToDoModel> getAllTasks() {
        List<ToDoModel> modelList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                ToDoModel task = new ToDoModel();
                task.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
                task.setTask(cursor.getString(cursor.getColumnIndex(COL_2)));
                task.setStatus(cursor.getInt(cursor.getColumnIndex(COL_3)));
                modelList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return modelList;
    }
}
