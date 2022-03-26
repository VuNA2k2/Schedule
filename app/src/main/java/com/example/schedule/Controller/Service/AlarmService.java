package com.example.schedule.Controller.Service;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.schedule.App;
import com.example.schedule.R;
import com.example.schedule.View.AlarmActivity;

public class AlarmService extends Service {
    private Bundle bundle;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();

        KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();
        bundle = intent.getExtras();
        Log.e("Output", bundle.getString("eventName") + " " + bundle.getString("eventNote") + " " + bundle.getString("eventId"));

        sendNotification();

        return START_NOT_STICKY;
    }

    private void sendNotification() {
        Intent mIntent = new Intent(this, AlarmActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtra("eventName", bundle.getString("eventName"));
        mIntent.putExtra("eventNote", bundle.getString("eventNote"));
        mIntent.putExtra("eventId", bundle.getString("eventId"));

        PendingIntent pendingIntent = PendingIntent.getActivity(this, Integer.parseInt(bundle.getString("eventId")), mIntent, 0);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setContentTitle("Schedule")
                .setContentText(bundle.getString("eventName") + "\n" + bundle.getString("eventNote"))
                .setLargeIcon(bitmap)
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null) {
            notificationManager.notify(Integer.parseInt(bundle.getString("eventId")),notification);
        }
    }

}
