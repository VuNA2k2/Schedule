package com.example.schedule.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.schedule.R;
import com.example.schedule.View.MainActivity;

public class IntroActivity extends AppCompatActivity {
    private TextView txtAppName;
    private ImageView imgLogo;
    private Animation up, down;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_activity);
        init();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        },1500);
    }

    private void init() {
        txtAppName = (TextView) findViewById(R.id.txtAppName);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_up);
        down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_down);
        txtAppName.setAnimation(down);
        imgLogo.setAnimation(up);
    }
}