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
    public static final String SQL_DELETE_USER = "DROP TABLE " + USER_TABLE_NAME;
    public static final String SQL_DELETE_PRODUCTS = "DROP TABLE products";
    public static final String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE `products` ( id INTEGER PRIMARY KEY AUTOINCREMENT, `name` VARCHAR(255),`price` FLOAT(20),`img` TEXT, `type` VARCHAR(100));";
    public static final String SQl_INSERT_PRODUCTS = "INSERT INTO products (name,price ,img ,type  ) VALUES ('APPLE iMac 24 4k 23.5 Retina M1 8GB RAM 512GB SSD macOS Zielony ', '9999.9', '" + ImageString.MAC.label + "', 'computer' ), ('Acer Nitro 50 i5-12400/16GB/512 GTX1660S', '2999.9', '" + ImageString.ACER.label + "', 'computer' ), ('G4M3R HERO i7-14700F/32GB/1TB/RTX4070S/W11x', '8500.0', '" + ImageString.GAMER.label + "', 'computer' ), ('LOGITECH M185', '59.99', '" + ImageString.LOGITECHMOUSE.label + "', 'mouse' ), ('RAZER Viper V3 Pro Czarny', '799.99', '" + ImageString.RAZER.label + "', 'mouse' ), ('ESPERANZA TK101 USB', '14.0', '" + ImageString.ESPERANZA.label + "', 'keyboard'), ('GLORIOUS GMMK Pro Slate', '1599.0', '" + ImageString.GLOURS.label + "', 'keyboard' )";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_FILE_NAME, null, DB_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);
        db.execSQL(SQl_CREATE);
        db.execSQL(SQl_INSERT_PRODUCTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_USER);
        db.execSQL(SQL_DELETE_PRODUCTS);
        onCreate(db);
    }
}
