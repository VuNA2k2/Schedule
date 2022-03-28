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
import androidx.appcompat.app.AppCompatDelegate;

import com.example.schedule.R;
import com.example.schedule.View.MainActivity;

public class IntroActivity extends AppCompatActivity {
    private TextView txtAppName, txtDevloped;
    private ImageView imgLogo;
    private Animation up, down, left;
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
        txtDevloped = findViewById(R.id.txtDeveloped);
        txtAppName = findViewById(R.id.txtAppName);
        imgLogo = findViewById(R.id.imgLogo);
        up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_up);
        down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_down);
        left = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_left);
        txtAppName.setAnimation(down);
        imgLogo.setAnimation(up);
        txtDevloped.setAnimation(left);
    }
}