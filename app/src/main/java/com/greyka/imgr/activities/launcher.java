package com.greyka.imgr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class launcher extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGHT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_launcher);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE |
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
        new android.os.Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(launcher.this, LoginActivity.class);
            launcher.this.startActivity(mainIntent);
            launcher.this.finish();
        }, SPLASH_DISPLAY_LENGHT);
    }

}