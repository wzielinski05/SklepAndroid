package com.example.sklep.customOrder;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sklep.MainActivity;
import com.example.sklep.R;
import com.example.sklep.database.DatabaseHelper;
import com.google.android.material.appbar.MaterialToolbar;

public class OrderDetailsActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    ImageView productImg;
    TextView computerName;
    TextView computerPrice;
    TextView computerAmount;
    TextView addonsView;

    TextView totalPriceView;
    String TAG = "AAAA";
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Bundle extras = getIntent().getExtras();

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();
        toolbar = findViewById(R.id.topAppBar);
        productImg = findViewById(R.id.productImg);
        computerName = findViewById(R.id.productName);
        computerPrice = findViewById(R.id.computerPrice);
        computerAmount = findViewById(R.id.amount);
        addonsView = findViewById(R.id.addons);
        totalPriceView = findViewById(R.id.totalPrice);

        Cursor cursor = db.query("products", null, "id = " + extras.getInt("id"), null, null, null, null);

        cursor.moveToFirst();


        toolbar.setTitle(extras.getString("productName"));
        computerName.setText(extras.getString("productName"));
        computerPrice.setText(cursor.getString(2) + " zł");
        totalPriceView.setText(extras.getString("price") + " zł");
        computerAmount.setText(String.valueOf(extras.getInt("amount")));
        byte[] imageAsBytes = Base64.decode(extras.getString("imgStr"), Base64.NO_PADDING);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        productImg.setImageBitmap(decodedByte);
        addonsView.setText(" " + extras.getString("addons").replace("  ", "\n"));


        Log.i(TAG, "onCreate: " + extras.getString("addons"));


        toolbar.setNavigationOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
        });
    }
}