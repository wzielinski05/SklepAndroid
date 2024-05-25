package com.example.sklep.customRow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sklep.MakeOrderActivity;
import com.example.sklep.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class RowAdapter extends ArrayAdapter<RowBean> {
    Context context;
    int layoutResourceId;
    List<RowBean> data;

    public RowAdapter(Context context, int layoutResourceId, List<RowBean> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        RowBeanHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new RowBeanHolder();
            holder.imgIcon = (ImageView) row.findViewById(R.id.imgIcon);
            holder.txtPrice = (TextView) row.findViewById(R.id.price);
            holder.txtProductName = (TextView) row.findViewById(R.id.productName);
            holder.card = (MaterialCardView) row.findViewById(R.id.card);

            row.setTag(holder);
        } else {
            holder = (RowBeanHolder) row.getTag();
        }

        RowBean object = data.get(position);

        byte[] imageAsBytes = Base64.decode(object.img, Base64.NO_PADDING);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        holder.imgIcon.setImageBitmap(decodedByte);
        holder.txtProductName.setText(object.productName);
        holder.txtPrice.setText(object.price + " zł");


        row.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), MakeOrderActivity.class);
                i.putExtra("id", object.id);
                i.putExtra("productName", object.productName);
                i.putExtra("price", object.price);
                i.putExtra("imgStr", object.img);
                getContext().startActivity(i);
            }
        });


        return row;
    }

    static class RowBeanHolder {
        ImageView imgIcon;
        TextView txtProductName;
        TextView txtPrice;
        MaterialCardView card;
    }
}
