package com.example.sklep.customOrder;

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

import com.example.sklep.R;
import com.example.sklep.customRow.RowBean;

import java.util.List;

public class OrderRowAdapter extends ArrayAdapter<OrderRowBean> {
    Context context;
    int layoutResourceId;
    List<OrderRowBean> data;

    public OrderRowAdapter(Context context, int layoutResourceId, List<OrderRowBean> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        OrderRowBeanHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new OrderRowBeanHolder();
            holder.imgIcon = (ImageView) row.findViewById(R.id.orderImgIcon);
            holder.txtProductName = (TextView) row.findViewById(R.id.orderProductName);
            holder.txtPrice = (TextView) row.findViewById(R.id.orderPrice);
            holder.txtAddons = (TextView) row.findViewById(R.id.orderAddons);
            row.setTag(holder);
        } else {
            holder = (OrderRowBeanHolder) row.getTag();
        }
        OrderRowBean object = data.get(position);
        byte[] imageAsBytes = Base64.decode(object.img, Base64.NO_PADDING);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        holder.imgIcon.setImageBitmap(decodedByte);
        holder.txtProductName.setText(object.amount + "x " + object.productName);
        holder.txtPrice.setText(object.price + " zł");
        holder.txtAddons.setText(object.addons);
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("AAA", "onClick: " + object.amount);
                Intent i = new Intent(getContext(), OrderDetailsActivity.class);
                i.putExtra("id", object.id);
                i.putExtra("productName", object.productName);
                i.putExtra("price", object.price);
                i.putExtra("imgStr", object.img);
                i.putExtra("addons", object.addons);
                i.putExtra("amount", object.amount);
                getContext().startActivity(i);
            }
        });
        return row;
    }

    static class OrderRowBeanHolder {
        ImageView imgIcon;
        TextView txtProductName;
        TextView txtPrice;
        TextView txtAddons;

    }
}
