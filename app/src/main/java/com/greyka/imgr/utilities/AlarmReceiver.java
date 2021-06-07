package com.greyka.imgr.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("notification")) {
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");
            NotificationUtil.startNotificationService(context, title, text);
        } else if (intent.getAction().equals("alarmUtil")) {
            AlarmUtil.alarmUtil(context);
        }
    }
}