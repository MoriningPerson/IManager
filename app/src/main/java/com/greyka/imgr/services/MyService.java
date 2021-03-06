package com.greyka.imgr.services;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.greyka.imgr.R;
import com.greyka.imgr.activities.MainActivity;
import com.greyka.imgr.data.Data;
import com.greyka.imgr.utilities.myUtils;

public class MyService extends Service {

    private Callback callback;
    private myUtils.myCountDownTimerHelper mCDT = null;
    private Activity timerActivity;
    private boolean screenAlwaysOn = false;
    private boolean lockEnabled = false;
    private boolean popWindowEnabled = false;
    private myUtils.myDensityHelper mdh;
    private int freeTimeRemain = 0;
    private SharedPreferences mSP;
    private boolean binded = false;
    private View screenLocker;
    private WindowManager windowManager;

    public boolean getScreenAlwaysOn() {
        return screenAlwaysOn;
    }

    public void changeScreenStatus() {
        screenAlwaysOn = !screenAlwaysOn;
    }

    public boolean getLockEnabled() {
        return lockEnabled;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myUtils.NotifyHelper.CreateChannel(this, "test_channel", "test_channel", "测试");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mdh == null) {
            mdh = new myUtils.myDensityHelper(getApplicationContext());
        }

        Notification notification = myUtils.NotifyHelper.createForeNotification(this, "test_channel", null);
        startForeground(1, notification);
        Log.e("on", "onStart");
        return super.onStartCommand(intent, flags, startId);
    }

    public void timerSetter(int sec, int remain, int freeTime, boolean lock) {
        this.lockEnabled = lock;
        this.freeTimeRemain = freeTime;
        mCDT = new myUtils.myCountDownTimerHelper(sec, remain, new myUtils.myCountDownTimerHelper.timer_handler() {
            @Override
            public void onTickEvent() {
                if (lockEnabled) {
                    if (myUtils.myForegroundActivityManager.isForeground(timerActivity)
                        || myUtils.myForegroundActivityManager.isForeground(MainActivity.getInstance())) {
                        if (popWindowEnabled) {
                            dismissWindow();
                        }
                    } else {
                        if (freeTimeRemain > 0) {
                            freeTimeRemain--;
                        } else if (!popWindowEnabled) {
                            popWindow();
                        }
                    }
                }
                setTime();
            }

            @Override
            public void onFinishEvent() {
                timerFinish();
                showTimer();
                myUtils.myToastHelper.showText(getApplicationContext(),"计时结束", Toast.LENGTH_LONG);
            }

            @Override
            public void onCreateEvent(int secInFuture, myUtils.myCountDownTimerHelper.timer_handler TH) {

            }
        });
    }

    public void setTime() {
        callback.onDataChange(mCDT.getTimeRemain(), (int) (1000 * (mCDT.getmMillisTotal() - mCDT.getmMillisRemain()) / mCDT.getmMillisTotal()));
        callback.setTotalTime(true);
    }

    public void clearTime() {
        callback.onDataChange("00:00:00", 0);
        callback.setTotalTime(false);
    }

    public boolean getCDTisRunning() {
        return cdtHasInstance() && mCDT.getIsRunning();
    }

    public boolean cdtHasInstance() {
        return mCDT != null;
    }

    public String getTimeTotal() {
        return mCDT.getTimeTotal();
    }

    public void timerPause() {
        if (cdtHasInstance()) {
            mCDT.mPause();
        }
    }

    public void timerStart() {
        if (cdtHasInstance()) {
            mCDT.mStart();
        }
    }

//    public void timerCancel() {
//        if (mSP == null) {
//            mSP = this.getSharedPreferences("timerData", Context.MODE_PRIVATE);
//        }
//        mSP.edit().putBoolean("timerSetted", false);
//        lockEnabled = false;
//        screenAlwaysOn = false;
//    }

    public void timerCancel() {
        clearTime();
        if (cdtHasInstance()) {
            mCDT.mCancel();
            mCDT = null;
        }
    }

    public void timerFinish() {
        callback.onDataChange(mCDT.getTimeRemain(), 1000);
        if (cdtHasInstance()) {
            mCDT.mCancel();
            mCDT = null;
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void popWindow() {
        if (!myUtils.myForegroundActivityManager.isForeground(timerActivity) &&
            !myUtils.myForegroundActivityManager.isForeground(MainActivity.getInstance())) {
            if (Settings.canDrawOverlays(timerActivity)) {
                // 获取WindowManager服务
                if (windowManager == null) {
                    windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
                }
                // 新建悬浮窗控件
                screenLocker = LayoutInflater.from(getApplicationContext()).inflate(R.layout.pop_up_screen_locker, null);
                Log.d("aa", String.valueOf(screenLocker == null));
                screenLocker.setOnClickListener(v -> showTimer());
                // 设置LayoutParam
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                } else {
                    layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                }
                layoutParams.format = PixelFormat.TRANSPARENT;
                layoutParams.width = mdh.dp2px(300);
                layoutParams.height = mdh.dp2px(70);
                layoutParams.gravity = Gravity.CENTER;
                // 将悬浮窗控件添加到WindowManager
                windowManager.addView(screenLocker, layoutParams);
            }
            popWindowEnabled = true;
        }
    }

    public void dismissWindow() {
        if(popWindowEnabled) {
            windowManager.removeView(screenLocker);
            popWindowEnabled = false;
        }
    }

    public boolean getPopWindowEnabled() {
        return popWindowEnabled;
    }

    public boolean getLocked() {
        return freeTimeRemain == 0;
    }

    private void showTimer() {
        Intent intent = new Intent(getApplicationContext(), com.greyka.imgr.activities.Timer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    public void activitySetter(Activity activity) {
        this.timerActivity = activity;
    }

    public interface Callback {
        void onDataChange(String timeRemain, int progress);

        void setTotalTime(boolean opt);
    }

    public class MyBinder extends Binder {
        //向Activity返回MyService实例
        public MyService getService() {
            return MyService.this;
        }
    }
}