package com.greyka.imgr.utilities;

import android.content.Context;
import android.content.Intent;

import com.greyka.imgr.services.NotificationService;

public class NotificationUtil {
    public static void startNotificationService(Context context, String contentTitle, String contentText) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.putExtra("contentTitle", contentTitle);
        intent.putExtra("contentText", contentText);
        context.startService(intent);
    }
}
