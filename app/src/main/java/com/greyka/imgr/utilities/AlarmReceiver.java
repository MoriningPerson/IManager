package com.greyka.imgr.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.greyka.imgr.utilities.AlarmUtil.alarmUtil;
import static com.greyka.imgr.utilities.NotificationUtil.startNotificationService;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("notification")) {
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");
            startNotificationService(context, title, text);
        } else if (intent.getAction().equals("alarmUtil")) {
            alarmUtil(context);
        }
    }
}