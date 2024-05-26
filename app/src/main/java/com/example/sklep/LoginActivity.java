package com.example.sklep;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sklep.database.DatabaseHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    SQLiteDatabase db;
    String TAG = "AAAAAAAAAAAAA";
    Button submitBtn;
    Button registerBtn;
    TextInputEditText email;
    TextInputEditText password;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();
//        db.execSQL("INSERT INTO users (email,password,name) VALUES ('test@test.com',1234,'Dinus')");

        submitBtn = findViewById(R.id.submitBtn);
        registerBtn = findViewById(R.id.registerBtn);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        sharedPreferences = getApplicationContext().getSharedPreferences("shopData", 0);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailText = String.valueOf(email.getText());
                String passwordText = String.valueOf(password.getText());
                String sqlQuery = "select * from users where email= " + emailText + " and password = " + passwordText;
                Cursor cursor = db.rawQuery("select * from users where email = ? and password = ?", new String[]{emailText, passwordText});
                int rows = 0;
                while (cursor.moveToNext()) {
                    rows++;
                }
                if (view != null) {
                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                if (rows > 0) {
                    Snackbar.make(view, getString(R.string.succes_login), Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(getResources().getColor(R.color.success_background))
                            .setTextColor(getResources().getColor(R.color.white))
                            .show();
                    cursor.moveToFirst();
                    JSONObject userDataObj = new JSONObject();
                    try {
                        userDataObj.put("email", cursor.getString(1));
                        userDataObj.put("id", cursor.getInt(0));
                        userDataObj.put("username", cursor.getString(3));

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLogin", true);
                    editor.putString("userDataJSON", userDataObj.toString());

                    editor.apply();
                    editor.commit();
                    new Handler(getMainLooper()).postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                            }, 1000
                    );
                } else {
                    Snackbar.make(view, getString(R.string.error_login), Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(getResources().getColor(R.color.error_background))
                            .setTextColor(getResources().getColor(R.color.white))
                            .show();
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });


    }
}