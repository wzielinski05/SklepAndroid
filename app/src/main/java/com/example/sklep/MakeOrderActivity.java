package com.example.sklep;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sklep.database.DatabaseHelper;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.slider.Slider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MakeOrderActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    ImageView productImg;
    TextView productName;
    SQLiteDatabase db;
    LinearLayout keyboardListLayout;
    LinearLayout mouseListLayout;
    TextView productPrice;
    Slider amountSlider;
    TotalPrice totalPrice;
    TextView totalPriceView;
    String TAG = "AAA";
    Button saveBtn;
    SharedPreferences sharedPreferences;
    int amount = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_make_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sharedPreferences = getApplicationContext().getSharedPreferences("shopData", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Bundle extras = getIntent().getExtras();
        List<Integer> addons = new ArrayList<>();
        totalPrice = new TotalPrice(Float.parseFloat(extras.getString("price")));
        toolbar = findViewById(R.id.topAppBar);
        productImg = findViewById(R.id.productImg);
        productName = findViewById(R.id.productName);
        keyboardListLayout = findViewById(R.id.keyboardLayout);
        mouseListLayout = findViewById(R.id.mouseLayout);
        productPrice = findViewById(R.id.productPrice);
        amountSlider = findViewById(R.id.amountSlider);
        totalPriceView = findViewById(R.id.totalPrice);
        saveBtn = findViewById(R.id.saveBtn);

        toolbar.setTitle(extras.getString("productName"));
        productName.setText(extras.getString("productName"));
        productPrice.setText(extras.getString("price") + " zł");
        totalPriceView.setText(extras.getString("price") + " zł");
        byte[] imageAsBytes = Base64.decode(extras.getString("imgStr"), Base64.NO_PADDING);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        productImg.setImageBitmap(decodedByte);

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext().getApplicationContext());
        db = databaseHelper.getWritableDatabase();
        Cursor cursorKeyboard = db.query("products", null, "type = 'keyboard'", null, null, null, null);
        cursorKeyboard.moveToFirst();
        while (!cursorKeyboard.isAfterLast()) {
            LinearLayout checkboxLayout = new LinearLayout(getApplicationContext());
            checkboxLayout.setOrientation(LinearLayout.HORIZONTAL);
            ImageView keyboardImg = new ImageView(getApplicationContext());
            byte[] imageAsBytesImg = Base64.decode(cursorKeyboard.getString(3), Base64.NO_PADDING);
            Bitmap decodedByteImg = BitmapFactory.decodeByteArray(imageAsBytesImg, 0, imageAsBytesImg.length);
            keyboardImg.setImageBitmap(decodedByteImg);
            String price = cursorKeyboard.getString(2);
            String id = cursorKeyboard.getString(0);
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(cursorKeyboard.getString(1));
            checkBox.setPadding(10, 15, 10, 15);
            TextView priceText = new TextView(this);
            priceText.setText(cursorKeyboard.getString(2) + " zŁ");
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    totalPrice.addToAddon(b ? Float.parseFloat(price) : -(Float.parseFloat(price)));
                    totalPriceView.setText(String.valueOf(totalPrice.getTotalPrice()));
                    if (b) {
                        addons.add(Integer.parseInt(id));
                    } else {
                        addons.remove(Integer.valueOf(id));
                    }
                }
            });
            checkboxLayout.addView(keyboardImg);
            checkboxLayout.addView(checkBox);
            checkboxLayout.addView(priceText);
            keyboardListLayout.addView(checkboxLayout);
            cursorKeyboard.moveToNext();
        }

        Cursor cursorMouse = db.query("products", null, "type = 'mouse'", null, null, null, null);
        cursorMouse.moveToFirst();
        while (!cursorMouse.isAfterLast()) {
            LinearLayout checkboxLayout = new LinearLayout(getApplicationContext());
            checkboxLayout.setOrientation(LinearLayout.HORIZONTAL);
            ImageView mouseImg = new ImageView(getApplicationContext());
            mouseImg.setMaxHeight(10);
            byte[] imageAsBytesImg = Base64.decode(cursorMouse.getString(3), Base64.NO_PADDING);
            Bitmap decodedByteImg = BitmapFactory.decodeByteArray(imageAsBytesImg, 0, imageAsBytesImg.length);
            mouseImg.setImageBitmap(decodedByteImg);
            String id = cursorMouse.getString(0);
            TextView priceText = new TextView(this);
            String price = cursorMouse.getString(2);
            priceText.setText(price + " zŁ");
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(cursorMouse.getString(1));
            checkBox.setPadding(10, 15, 10, 15);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    totalPrice.addToAddon(b ? Float.parseFloat(price) : -(Float.parseFloat(price)));
                    totalPriceView.setText(String.valueOf(totalPrice.getTotalPrice()));
                    if (b) {
                        addons.add(Integer.parseInt(id));
                    } else {
                        addons.remove(Integer.valueOf(id));
                    }

                }
            });
            checkboxLayout.addView(mouseImg);
            checkboxLayout.addView(checkBox);
            checkboxLayout.addView(priceText);
            mouseListLayout.addView(checkboxLayout);
            cursorMouse.moveToNext();
        }
        toolbar.setNavigationOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
        });
        amountSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            int oldValue;

            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                totalPrice.setCorePrice((Float.parseFloat(Objects.requireNonNull(extras.getString("price"))) * slider.getValue()));
                totalPriceView.setText(String.valueOf(totalPrice.getTotalPrice()) + " zł");
                amount = (int) slider.getValue();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONArray orders = new JSONArray(sharedPreferences.getString("orders", "[]"));
                    JSONObject order = new JSONObject();
                    JSONObject computerData = new JSONObject();
                    computerData.put("id", extras.getInt("id"));
                    computerData.put("amount", amount);
                    order.put("computer", computerData);
                    JSONArray addonsObj = new JSONArray();
                    for (int addon : addons) {
                        addonsObj.put(addon);
                    }
                    order.put("addons", addons);
                    JSONObject userData = new JSONObject(sharedPreferences.getString("userDataJSON", "{}"));
                    order.put("userId", userData.getInt("id"));

                    orders.put(order);
                    editor.remove("orders");
                    editor.putString("orders", orders.toString());
                    editor.apply();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("redirect", "cart");
                    startActivity(intent);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    class TotalPrice {
        private float corePrice = 0;
        private float addons = 0;
        DecimalFormat df = new DecimalFormat();

        public TotalPrice(float corePrice) {
            this.corePrice = corePrice;
            df.setMaximumFractionDigits(2);
        }

        public float getCorePrice() {
            return corePrice;
        }

        public void setCorePrice(float corePrice) {
            this.corePrice = corePrice;
        }

        public float getAddons() {
            return addons;
        }

        public void setAddons(float addons) {
            this.addons = addons;
        }

        public void addToAddon(float addons) {
            this.addons += addons;
        }

        public String getTotalPrice() {
            return df.format(corePrice + addons);
        }
    }
}

