package com.example.schedule.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.schedule.Controller.Service.AlarmService;
import com.example.schedule.R;

import java.text.SimpleDateFormat;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        init();
    }

    private void init() {
        Bundle bundle = getIntent().getExtras();
        ImageView imgBell = findViewById(R.id.imgBell);
        TextView txtAlarmTime = findViewById(R.id.txtAlarmTime);
        TextView txtAlarmName = findViewById(R.id.txtAlarmName);
        TextView txtAlarmNote = findViewById(R.id.txtAlarmNote);
        Button btnCancel = findViewById(R.id.btnCancel);
        Glide.with(getApplicationContext()).load(R.drawable.bell).into(imgBell);
        btnCancel.setOnClickListener(view -> {
            stopService(new Intent(AlarmActivity.this, AlarmService.class));
            finish();
        });

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        txtAlarmTime.setText(sdf.format(System.currentTimeMillis()));

        txtAlarmName.setText(bundle.getString("eventName"));

        txtAlarmNote.setText(bundle.getString("eventNote"));
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
