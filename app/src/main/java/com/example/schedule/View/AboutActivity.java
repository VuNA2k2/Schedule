package com.example.schedule.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.schedule.R;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView btnFacebook, btnProPTIT, btnGitHub;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);

        btnFacebook = findViewById(R.id.btnFacebook);
        btnProPTIT = findViewById(R.id.btnProPTIT);
        btnGitHub = findViewById(R.id.btnGitHub);

        btnFacebook.setOnClickListener(this);
        btnProPTIT.setOnClickListener(this);
        btnGitHub.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnFacebook:
                Intent intentFacebook = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/vuvovan2k2"));
                startActivity(intentFacebook);
                break;
            case R.id.btnProPTIT:
                Intent intentProPTIT = new Intent(Intent.ACTION_VIEW, Uri.parse("https://proptit.com"));
                startActivity(intentProPTIT);
                break;
            case R.id.btnGitHub:
                Intent intentGitHub = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/VuNA2k2"));
                startActivity(intentGitHub);
                break;
            default:
                break;
        }
    }
}
