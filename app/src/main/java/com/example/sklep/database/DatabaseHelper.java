package com.example.sklep.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String USER_TABLE_NAME = "users";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_NAME = "name";


    public static final String DB_FILE_NAME = "shop.db";
    public static final int DB_VERSION = 1;

    public static final String SQl_CREATE = "CREATE TABLE " + USER_TABLE_NAME + "( id INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_EMAIL + " VARCHAR(255) UNIQUE, " + COLUMN_PASSWORD + " VARCHAR (255), " + COLUMN_NAME + " VARCHAR(255) );";
    public static final String SQL_DELETE = "DROP TABLE " + USER_TABLE_NAME;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_FILE_NAME, null, DB_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQl_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE);
        onCreate(db);
    }
}
