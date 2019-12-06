package com.drunk.mode.Fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;


import com.drunk.mode.Services.WaterReminderService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.drunk.mode.Activities.AlertCenterActivity;
import com.drunk.mode.Activities.MainActivity;
import com.drunk.mode.Activities.SendAlertActivity;
import com.drunk.mode.R;

import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmergencyFragment extends RootFragment
{
   @BindView(R.id.alertCenterCardView) CardView cardViewAlertCenter;
   @BindView(R.id.sendAlertCardView) CardView cardViewSendAlert;
   @BindView(R.id.aboutDevId) CardView cardViewLogout;
   @BindView(R.id.button_timepicker) Button button_timepicker;
   @BindView(R.id.button_cancel) Button button_Cancel;


   FirebaseAuth auth;
   FirebaseUser firebaseUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  =  inflater.inflate(R.layout.fragment_emergency, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        auth = FirebaseAuth.getInstance();

        firebaseUser = auth.getCurrentUser();

        cardViewAlertCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getContext(), AlertCenterActivity.class);
                startActivity(intent);

            }
        });

        cardViewSendAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getContext(), SendAlertActivity.class);
                startActivity(intent);

            }
        });


        cardViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).finish();
            }
        });

        button_timepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext().getApplicationContext(), WaterReminderService.class);
                i.setAction("");
                getContext().getApplicationContext().startService(i);
            }
        });

        button_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext().getApplicationContext(), WaterReminderService.class);
                i.setAction("stop");
                getContext().getApplicationContext().startService(i);
            }
        });

//        createNotificationChannel();
//
//        button_timepicker.setOnClickListener(v-> {
//            Intent intent = new Intent(getContext(), ReminderBroadcast.class);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
//
//            long timeAtButtonClick = System.currentTimeMillis();
//
//            long tenSecondsInMillis = 1000 * 10;
//
//            alarmManager.set(AlarmManager.RTC_WAKEUP,
//                    timeAtButtonClick+tenSecondsInMillis,
//                    pendingIntent);
//
//        });

    }

//    private void createNotificationChannel()
//    {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            CharSequence name = "ReminderChannel";
//            String description = "Channel for reminders";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel notificationChannel = new NotificationChannel("notify", name, importance);
//            notificationChannel.setDescription(description);
//
//            NotificationManager notificationManager = Objects.requireNonNull(getActivity()).getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(notificationChannel);
//        }
//    }
}
