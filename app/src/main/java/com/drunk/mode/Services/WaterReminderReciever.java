package com.drunk.mode.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Calendar;

public class WaterReminderReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR);
        if (hour == 0) {
            hour = 12;
        }

        String minutestr;
        int minute = calendar.get(Calendar.MINUTE);
        if (minute < (int) 10) {
            minutestr = "0" + minute;
        }
        else {
            minutestr = "" + minute;
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Drunk Mode")
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle("Water Reminders")
                .setContentText("It is now: "+ hour + ":" + minutestr + ". DRINK WATER!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(false);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(0, builder.build());
    }
}
