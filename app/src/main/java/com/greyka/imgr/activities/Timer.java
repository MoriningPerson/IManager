package com.greyka.imgr.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.greyka.imgr.R;
import com.greyka.imgr.classes.CirclePgBar;
import com.greyka.imgr.interfaces.timer_handler;
import com.greyka.imgr.utilities.myUtils;

public class Timer extends AppCompatActivity{

    private boolean locked = false;
    private boolean alwaysOn = false;
    private ImageView ic_alwaysOn;
    private CirclePgBar mPgBar;
    private TextView mTimeRemain;
    private TextView mTimeTotal;
    private ViewGroup mPanel;
    private myUtils.myCountDownTimerHelper mCDT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        mPgBar = (CirclePgBar)findViewById(R.id.timer_PgBar);
        ic_alwaysOn = (ImageView)findViewById(R.id.timer_alwaysOn);
        mTimeRemain = (TextView)findViewById(R.id.timer_remain);
        mTimeTotal = (TextView)findViewById(R.id.timer_total);
        mPanel = (ViewGroup)findViewById(R.id.timer_pannel);
        ic_alwaysOn.setOnClickListener(new View.OnClickListener() {
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
        mCDT = new myUtils.myCountDownTimerHelper(60, new timer_handler() {
            @Override
            public void onTickEvent() {
                mTimeRemain.setText(mCDT.getTimeRemain());
                mPgBar.setProgress((int)(1000 * (mCDT.getmMillisTotal() - mCDT.getmMillisRemain()) / mCDT.getmMillisTotal()));
            }

            @Override
            public void onFinishEvent() {
                mPgBar.setProgress(mPgBar.getTotalProgress());
            }
            @Override
            public void onCreateEvent(int secInFuture,timer_handler TH){
            }
        });
        mTimeTotal.setText("总 "+mCDT.getTimeTotal());
        mPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCDT.getIsRunning()){
                    myUtils.myToastHelper.showText(getApplicationContext(),"暂停",Toast.LENGTH_SHORT);
                    mCDT.mCancel();
                }
                else{
                    myUtils.myToastHelper.showText(getApplicationContext(),"开始",Toast.LENGTH_SHORT);
                    mCDT.mStart();
                }
            }
        });
        mCDT.mStart();
    }
}
