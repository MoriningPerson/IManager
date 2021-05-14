package com.greyka.imgr.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.greyka.imgr.R;

public class launcher extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGHT = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        new android.os.Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(launcher.this, MainActivity.class);
            launcher.this.startActivity(mainIntent);
            launcher.this.finish();
        }, SPLASH_DISPLAY_LENGHT);
    }
}