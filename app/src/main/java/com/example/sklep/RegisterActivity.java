package com.example.sklep;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText username;
    TextInputEditText email;
    TextInputEditText password;
    SQLiteDatabase db;
    String TAG = "AAA";
    Button submitBtn;
    Button loginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();
//        db.execSQL("INSERT INTO users (email,password,name) VALUES ('test@test.com',1234,'Dinus')");

        submitBtn = findViewById(R.id.submitBtn);
        loginBtn = findViewById(R.id.loginBtn);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameText = String.valueOf(username.getText());
                String emailText = String.valueOf(email.getText());
                String passwordText = String.valueOf(password.getText());

                ContentValues values = new ContentValues();
                values.put("email", emailText);
                values.put("name", usernameText);
                values.put("password", passwordText);

                long result = db.insert("users", null, values);

                if (view != null) {
                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                if (result < 0) {
                    Snackbar.make(view, getString(R.string.error_login), Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(getResources().getColor(R.color.error_background))
                            .setTextColor(getResources().getColor(R.color.white))
                            .show();
                } else {
                    Snackbar.make(view, getString(R.string.succes_register), Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(getResources().getColor(R.color.success_background))
                            .setTextColor(getResources().getColor(R.color.white))
                            .show();

                    new Handler(getMainLooper()).postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                }
                            }, 1000
                    );
                }


            }
        });


    }
}