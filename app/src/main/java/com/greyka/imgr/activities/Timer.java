package com.greyka.imgr.activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.greyka.imgr.R;
import com.greyka.imgr.classes.CirclePgBar;
import com.greyka.imgr.utilities.myUtils;

public class Timer extends AppCompatActivity {

    private Boolean locked = false;
    private Boolean alwaysOn = false;
    private ImageView ic_alwaysOn;
    private CirclePgBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        pb = (CirclePgBar)findViewById(R.id.timer_PgBar);
        ic_alwaysOn = (ImageView)findViewById(R.id.timer_alwaysOn);
        pb.setProgress(20,"19:30");
        ViewGroup mPannel = (ViewGroup)findViewById(R.id.timer_pannel);
        mPannel.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(alwaysOn){
                    myUtils.myToastHelper.showText(getApplicationContext(),"屏幕常亮：关闭", Toast.LENGTH_SHORT);
                    ic_alwaysOn.setColorFilter(getColor(R.color.grey81));
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
                else{
                    myUtils.myToastHelper.showText(getApplicationContext(),"屏幕常亮：开启", Toast.LENGTH_SHORT);
                    ic_alwaysOn.setColorFilter(getColor(R.color.dimgrey));
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
                alwaysOn = !alwaysOn;

            }
        });
    }

}
