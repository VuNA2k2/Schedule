package com.example.schedule.Controller.Receiver;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

import com.example.schedule.Controller.Service.AlarmService;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent mIntent = new Intent(context, AlarmService.class);
        Bundle bundle = intent.getExtras();
        mIntent.putExtra("eventName", bundle.getString("eventName"));
        mIntent.putExtra("eventNote", bundle.getString("eventNote"));
        mIntent.putExtra("eventId", bundle.getString("eventId"));
        Log.e("Output", "hello from receiver");
        Log.e("Output", bundle.getString("eventName") + " " + bundle.getString("eventNote") + " " + bundle.getString("eventId"));
        context.startForegroundService(mIntent);
    }
}
