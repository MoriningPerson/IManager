package com.greyka.imgr.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.greyka.imgr.utilities.myUtils;

public class MyService extends Service {

    private Callback callback;
    private myUtils.myCountDownTimerHelper mCDT = null;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new myUtils.myNotificationHelper(this, "aaa");
        this.startForeground(1, new NotificationCompat.Builder(this, "aaa").build());
        return super.onStartCommand(intent, flags, startId);
    }

    public void timerSetter(int sec) {
        mCDT = new myUtils.myCountDownTimerHelper(sec, new myUtils.timer_handler() {
            @Override
            public void onTickEvent() {
                setTime();
            }

            @Override
            public void onFinishEvent() {
                timerFinish();
            }

            @Override
            public void onCreateEvent(int secInFuture, myUtils.timer_handler TH) {
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