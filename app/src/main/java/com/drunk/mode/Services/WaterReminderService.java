package com.drunk.mode.Services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Calendar;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WaterReminderService extends IntentService {

    public WaterReminderService() {
        super("WaterReminderService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

            Intent i = new Intent(this, WaterReminderReciever.class);

            //creating a pending intent using the intent
            PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);

            if (action.equals("stop")){
                am.cancel(pi);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), "Drunk Mode")
                        .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                        .setContentTitle("Water Reminders")
                        .setContentText("Water Reminders Stopped!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setOngoing(false);

                createNotificationChannel();

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getBaseContext());

                notificationManager.notify(0, builder.build());

            }
            else {
                Calendar calendar = Calendar.getInstance();

                long currtime = calendar.getTimeInMillis();
                long alarmtime = currtime + 1800000;

                am.setRepeating(AlarmManager.RTC_WAKEUP, alarmtime,1800000, pi);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), "Drunk Mode")
                        .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                        .setContentTitle("Water Reminders")
                        .setContentText("Water Reminders Started!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setOngoing(false);

                createNotificationChannel();

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getBaseContext());

                notificationManager.notify(0, builder.build());
            }
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Drunk Mode";
            String description = "Water Reminders";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Drunk Mode", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
