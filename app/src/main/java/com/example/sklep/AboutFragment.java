package com.example.sklep;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;


public class AboutFragment extends Fragment {

    Button aboutBtn;

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        aboutBtn = view.findViewById(R.id.aboutBtn);
        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getString(R.string.about_info), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(getResources().getColor(R.color.info_background))
                        .setTextColor(getResources().getColor(R.color.white))
                        .show();
                Toast.makeText(getContext(), getString(R.string.about_info), Toast.LENGTH_LONG).show();

                NotificationChannel channel = new NotificationChannel("1", "chanel", NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription("desc");
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "1")
                        .setSmallIcon(R.drawable.computer_solid)
                        .setContentTitle(getString(R.string.about))
                        .setContentText(getString(R.string.about_info));

                NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                manager.createNotificationChannel(channel);
                manager.notify(0, builder.build());
                new Handler(Looper.getMainLooper()).postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                new MaterialAlertDialogBuilder(getContext())
                                        .setTitle(getString(R.string.about))
                                        .setMessage(getString(R.string.about_info))
                                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        }).show();
                            }
                        }, 500
                );
            }
        });
        super.onViewCreated(view, savedInstanceState);


    }
}