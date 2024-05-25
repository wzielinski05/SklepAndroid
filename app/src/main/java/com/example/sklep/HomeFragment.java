package com.example.sklep;

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

import com.example.sklep.customRow.RowAdapter;
import com.example.sklep.customRow.RowBean;
import com.example.sklep.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    List<RowBean> productsList;
    private ListView productsListView;
    SQLiteDatabase db;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productsList = new ArrayList<RowBean>();
        RowAdapter adapter = new RowAdapter(getContext(), R.layout.custom_row, productsList);

        productsListView = (ListView) view.findViewById(R.id.productsList);

        productsListView.setAdapter(adapter);
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity().getApplicationContext());
        db = databaseHelper.getWritableDatabase();

        Cursor cursor = db.query("products", null, "type = 'computer'", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            productsList.add(new RowBean(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            cursor.moveToNext();
        }


    }
}