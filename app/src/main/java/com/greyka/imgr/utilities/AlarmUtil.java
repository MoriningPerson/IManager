package com.greyka.imgr.utilities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.greyka.imgr.data.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AlarmUtil {
    public static void alarmUtil(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("lastDay", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences1 = context.getSharedPreferences("task_id", Context.MODE_PRIVATE);
        String date = sharedPreferences.getString("date", "");
        java.sql.Date dateNow = new java.sql.Date(System.currentTimeMillis());
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (!Objects.equals(date, dateNow.toString())) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
            editor.putString("date", dateNow.toString());
            editor.apply();
            PendingIntent alarmIntent;
            for (Data.Task i : getTodayTaskList()) {
                java.text.SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
                try {
                    Date date1 = formatter.parse(i.getStart_time());
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, date1.getHours());
                    calendar.set(Calendar.MINUTE, date1.getMinutes());
                    calendar.set(Calendar.SECOND, date1.getSeconds());
                    Intent intent = new Intent(context, AlarmReceiver.class).setAction("notification");
                    intent.putExtra("title", i.getTask_name());
                    intent.putExtra("text", i.getTask_description());
                    alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
                    alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
                    calendar.add(Calendar.MINUTE, -10);
                    alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
                    editor1.putBoolean(i.getTask_name(), true);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            editor1.apply();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 1);
            calendar.set(Calendar.SECOND, 2);
            Intent intent = new Intent(context, AlarmReceiver.class).setAction("alarmUtil");
            alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        }
    }
    public static void setAlarm(Data.Task task){

    }
    private static List<Data.Task> getTodayTaskList() {
        return null;
    }
}
