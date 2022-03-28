package com.example.schedule.View;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.schedule.Model.Music;
import com.example.schedule.R;

import java.text.SimpleDateFormat;

public class AlarmActivity extends AppCompatActivity {

    private ImageView imgBell;
    private TextView txtAlarmTime;
    private TextView txtAlarmName;
    private TextView txtAlarmNote;
    private Button btnCancel;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_activity);
        init();
    }

    private void init() {
        Bundle bundle = getIntent().getExtras();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), bundle.getInt("music"));
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(100, 100);
        mediaPlayer.start();
        imgBell = findViewById(R.id.imgBell);
        txtAlarmTime = findViewById(R.id.txtAlarmTime);
        txtAlarmName = findViewById(R.id.txtAlarmName);
        txtAlarmNote = findViewById(R.id.txtAlarmNote);
        btnCancel = findViewById(R.id.btnCancel);
        Glide.with(getApplicationContext()).load(R.drawable.bell).into(imgBell);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        txtAlarmTime.setText(sdf.format(System.currentTimeMillis()));

        txtAlarmName.setText(bundle.getString("eventName"));

        txtAlarmNote.setText(bundle.getString("eventNote"));
    }

    @Override
    public void finish() {
        super.finish();
        onStop();
        onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
