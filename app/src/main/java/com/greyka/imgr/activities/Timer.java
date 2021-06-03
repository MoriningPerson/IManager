package com.greyka.imgr.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
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
import com.greyka.imgr.dialogs.myTimerCancelFragment;
import com.greyka.imgr.dialogs.myTimerPickerDialogFragment;
import com.greyka.imgr.services.MyService;
import com.greyka.imgr.utilities.myUtils;

@RequiresApi(api = Build.VERSION_CODES.M)

public class Timer extends AppCompatActivity implements myTimerCancelFragment.NoticeDialogListener {

    private final Intent serviceIntent = new Intent();
    private boolean alwaysOn = false;
    private ImageView ic_alwaysOn;
    private ImageView ic_lockEnabled;
    private CirclePgBar mPgBar;
    private TextView mTimeRemain;
    private TextView mTimeTotal;
    private ViewGroup mPanel;
    private MyService.MyBinder binder = null;
    private MyService myService;
    private Activity mActivity;
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MyService.MyBinder) service;
            myService = binder.getService();
            myService.setCallback(new MyService.Callback() {
                @Override
                public void onDataChange(String timeRemain, int progress) {
                    mTimeRemain.setText(timeRemain);
                    mPgBar.setProgress(progress);
                }

                @Override
                public void setTotalTime(boolean opt) {
                    if (opt) {
                        mTimeTotal.setText("总 " + myService.getTimeTotal());
                    } else {
                        mTimeTotal.setText("总 " + "00:00:00");
                    }
                }
            });
            myService.activitySetter(mActivity);
            if (myService.cdtHasInstance()) {
                myService.setTime();
            }
            refreshIcons();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("aaa", "123456");
            myService = null;
        }
    };
    private myTimerPickerDialogFragment pickerDialog;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        getTimerService();

        mPgBar = (CirclePgBar) findViewById(R.id.timer_PgBar);
        ic_alwaysOn = (ImageView) findViewById(R.id.timer_alwaysOn);
        ic_lockEnabled = (ImageView) findViewById(R.id.timer_lock);
        mTimeRemain = (TextView) findViewById(R.id.timer_remain);
        mTimeTotal = (TextView) findViewById(R.id.timer_total);
        mPanel = (ViewGroup) findViewById(R.id.timer_pannel);
        ic_alwaysOn.setOnClickListener(v -> {
            if (!myService.cdtHasInstance()) {
                return;
            }
            myService.changeScreenStatus();
            if (!myService.getScreenAlwaysOn()) {
                myUtils.myToastHelper.showText(getApplicationContext(), "屏幕常亮：关闭", Toast.LENGTH_SHORT);
                refreshIcons();
            } else {
                myUtils.myToastHelper.showText(getApplicationContext(), "屏幕常亮：开启", Toast.LENGTH_SHORT);
                refreshIcons();
            }
        });

//        mPgBar = findViewById(R.id.timer_PgBar);
//        ic_alwaysOn = findViewById(R.id.timer_alwaysOn);
//        mTimeRemain = findViewById(R.id.timer_remain);
//        mTimeTotal = findViewById(R.id.timer_total);
//        mPanel = findViewById(R.id.timer_pannel);
//        ic_alwaysOn.setOnClickListener(v -> {
//            if (alwaysOn) {
//                myUtils.myToastHelper.showText(getApplicationContext(), "屏幕常亮：关闭", Toast.LENGTH_SHORT);
//                ic_alwaysOn.setColorFilter(getColor(R.color.grey81));
//                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//            } else {
//                myUtils.myToastHelper.showText(getApplicationContext(), "屏幕常亮：开启", Toast.LENGTH_SHORT);
//                ic_alwaysOn.setColorFilter(getColor(R.color.dimgrey));
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//            }
//            alwaysOn = !alwaysOn;
//
//        });
        mPanel.setOnClickListener(v -> {
            Log.d("click", "click");
            if (myService == null || myService.getLockEnabled() && myService.getCDTisRunning()) {
                return;
            } else if (!myService.cdtHasInstance()) {
                myService.timerCancel();
                refreshIcons();
                pickerDialog = new myTimerPickerDialogFragment();
                pickerDialog.setDialogListener((hour, minute, second, lockPercent, lockEnabled) -> {
                    int totSec = hour * 3600 + minute * 60 + second;
                    myService.timerSetter(totSec, totSec, totSec * (100 - lockPercent) / 100, lockEnabled);
                    myService.setTime();
                    refreshIcons();
                });
                pickerDialog.show(getSupportFragmentManager(), "pickerDialog");
                Log.d("aa", "abcde");
            } else if (binder.getService().getCDTisRunning()) {
                myUtils.myToastHelper.showText(getApplicationContext(), "暂停", Toast.LENGTH_SHORT);
                myService.timerPause();
            } else {
                myUtils.myToastHelper.showText(getApplicationContext(), "开始", Toast.LENGTH_SHORT);
                myService.timerStart();
            }
        });
        mPanel.setOnLongClickListener(v -> {
            if (myService.cdtHasInstance() && !myService.getLockEnabled()) {
                myTimerCancelFragment mTCF = new myTimerCancelFragment();
                mTCF.show(getSupportFragmentManager(), "aa");
            }
            return true;
        });
    }

    private void getTimerService() {
        serviceIntent.setClass(this, MyService.class);
        bindService(serviceIntent, connection, BIND_AUTO_CREATE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    private void refreshIcons() {
        if (myService.getLockEnabled()) {
            ic_lockEnabled.setColorFilter(getColor(R.color.dimgrey));
        } else {
            ic_lockEnabled.setColorFilter(getColor(R.color.grey81));
        }
        if (myService.getScreenAlwaysOn()) {
            ic_alwaysOn.setColorFilter(getColor(R.color.dimgrey));
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            ic_alwaysOn.setColorFilter(getColor(R.color.grey81));
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    protected void onDestroy() {
        if (myService.getLocked() && !myService.getPopWindowEnabled() && myService.getCDTisRunning()) {
            myService.popWindow();
        }
        if (myService != null && !myService.cdtHasInstance()) {
            myService.stopSelf();
            unbindService(connection);
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (myService.getLocked() && !myService.getPopWindowEnabled() && myService.getCDTisRunning()) {
            myService.popWindow();
        }
        super.onPause();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        myService.timerCancel();
        refreshIcons();
    }
}
