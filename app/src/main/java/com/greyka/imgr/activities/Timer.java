package com.greyka.imgr.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.greyka.imgr.R;
import com.greyka.imgr.classes.CirclePgBar;
import com.greyka.imgr.fragments.myTimerCancelFragment;
import com.greyka.imgr.services.MyService;
import com.greyka.imgr.utilities.myUtils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Timer extends AppCompatActivity implements myTimerCancelFragment.NoticeDialogListener{

    private boolean alwaysOn = false;
    private ImageView ic_alwaysOn;
    private CirclePgBar mPgBar;
    private TextView mTimeRemain;
    private TextView mTimeTotal;
    private ViewGroup mPanel;
    private MyService.MyBinder binder = null;
    private final Intent serviceIntent = new Intent();
    private MyService myService;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MyService.MyBinder) service;
            myService = binder.getService();
            Log.d("th","2");
            myService.setCallback(new MyService.Callback() {
                @Override
                public void onDataChange(String timeRemain, int progress) {
                    mTimeRemain.setText(timeRemain);
                    mPgBar.setProgress(progress);
                }
                @Override
                public void setTotalTime(boolean opt){
                    if(opt == true) {
                        mTimeTotal.setText("总 " + myService.getTimeTotal());
                    }
                    else{
                        mTimeTotal.setText("总 " + "00:00:00");
                    }
                }
            });
            if(myService.cdtHasInstance()) {
                myService.setTime();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("aaa","123456");
            myService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTimerService();
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
        mPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click","click");
                if(myService == null){
                    return;
                }
                else if(!myService.cdtHasInstance()){
                    myService.timerSetter(40);
                    myService.timerStart();
                }
                else if(binder.getService().getCDTisRunning()){
                    myUtils.myToastHelper.showText(getApplicationContext(),"暂停",Toast.LENGTH_SHORT);
                    myService.timerPause();
                }
                else{
                    myUtils.myToastHelper.showText(getApplicationContext(),"开始",Toast.LENGTH_SHORT);
                    myService.timerStart();
                }
            }
        });
        mPanel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(myService.cdtHasInstance()) {
                    myTimerCancelFragment mTCF = new myTimerCancelFragment();
                    mTCF.show(getSupportFragmentManager(), "aa");
                }
                return true;
            }
        });
    }
    private void getTimerService(){
        serviceIntent.setClass(this,MyService.class);
        bindService(serviceIntent,connection,BIND_AUTO_CREATE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myService != null && !myService.cdtHasInstance()) {
            myService.stopSelf();
            unbindService(connection);
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        myService.timerCancel();
    }
}
