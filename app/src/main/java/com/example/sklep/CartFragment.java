package com.example.sklep;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.sklep.customOrder.OrderRowAdapter;
import com.example.sklep.customOrder.OrderRowBean;
import com.example.sklep.customRow.RowAdapter;
import com.example.sklep.customRow.RowBean;
import com.example.sklep.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment {

    List<OrderRowBean> ordersList;
    private ListView ordersListView;
    SQLiteDatabase db;
    SharedPreferences sharedPreferences;
    String TAG = "AAAA";


    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getContext().getSharedPreferences("shopData", 0);

        ordersList = new ArrayList<OrderRowBean>();
        OrderRowAdapter adapter = new OrderRowAdapter(getContext(), R.layout.order_custom_row, ordersList);

        ordersListView = (ListView) view.findViewById(R.id.orderList);

        ordersListView.setAdapter(adapter);

        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity().getApplicationContext());
        db = databaseHelper.getWritableDatabase();

        JSONArray orders = null;
        try {
            orders = new JSONArray(sharedPreferences.getString("orders", "[]"));
            for (int i = 0; i < orders.length(); i++) {
                float totalPrice = 0;
                JSONObject order = new JSONObject(String.valueOf(orders.get(i)));
                JSONObject computerData = new JSONObject(String.valueOf(order.getJSONObject("computer")));
                Cursor computerCursor = db.query("products", null, "id = " + computerData.getInt("id"), null, null, null, null);
                computerCursor.moveToFirst();
                String computerName = computerCursor.getString(1);
                totalPrice += Float.parseFloat(computerCursor.getString(2)) * computerData.getInt("amount");
                String img = computerCursor.getString(3);
                StringBuilder addonsString = new StringBuilder();
                Log.i(TAG, "onViewCreated: " + order.getString("addons"));
                JSONArray addons = new JSONArray(order.getString("addons"));
                for (int j = 0; j < addons.length(); j++) {
                    Cursor addonCursor = db.query("products", null, "id  = " + addons.getInt(j), null, null, null, null);
                    addonCursor.moveToFirst();
                    addonsString.append(addonCursor.getString(1)).append("   ");
                    totalPrice += addonCursor.getFloat(2);
                }
                ordersList.add(new OrderRowBean(computerData.getInt("id"), computerName, String.valueOf(totalPrice), img, addonsString.toString()));
                Log.i(TAG, "onViewCreated: " + order);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }
}