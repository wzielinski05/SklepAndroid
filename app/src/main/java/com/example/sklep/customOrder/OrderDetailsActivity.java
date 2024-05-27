package com.example.sklep.customOrder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

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
    Button optionBtn;
    SharedPreferences sharedPreferences;


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
        sharedPreferences = getApplicationContext().getSharedPreferences("shopData", 0);

        toolbar = findViewById(R.id.topAppBar);
        productImg = findViewById(R.id.productImg);
        computerName = findViewById(R.id.productName);
        computerPrice = findViewById(R.id.computerPrice);
        computerAmount = findViewById(R.id.amount);
        addonsView = findViewById(R.id.addons);
        totalPriceView = findViewById(R.id.totalPrice);
        optionBtn = findViewById(R.id.toolsBtn);
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
        optionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(OrderDetailsActivity.this);
                View view1 = LayoutInflater.from(OrderDetailsActivity.this).inflate(R.layout.tools_bottom_sheet, null);
                bottomSheetDialog.setContentView(view1);
                bottomSheetDialog.show();
                JSONObject userData;
                try {
                    userData = new JSONObject(sharedPreferences.getString("userDataJSON", "{}"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


                LinearLayout shareBtn = view1.findViewById(R.id.shareBtn);
                shareBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        try {
                            sendIntent.putExtra(Intent.EXTRA_EMAIL, userData.getString("email"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.emailStart) + extras.getInt("amount") + "x " + extras.getString("productName"));
                        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.emailStart) + extras.getInt("amount") + "x " + extras.getString("productName") + getString(R.string.email_before) + extras.getString("addons"));
                        sendIntent.setType("text/plain");

                        Intent shareIntent = Intent.createChooser(sendIntent, null);
                        startActivity(shareIntent);

                    }
                });
            }
        });
    }
}