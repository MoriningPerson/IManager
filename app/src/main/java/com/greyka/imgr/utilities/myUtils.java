package com.greyka.imgr.utilities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.greyka.imgr.R;
import com.greyka.imgr.fragments.FragCalendar;
import com.greyka.imgr.fragments.FragHome;
import com.greyka.imgr.fragments.FragList;
import com.greyka.imgr.fragments.FragMine;
import com.greyka.imgr.fragments.myLocationPermissionDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class myUtils {
    public interface timer_handler {
        void onTickEvent();

        void onFinishEvent();

        void onCreateEvent(int secInFuture, timer_handler TH);
    }

    public static class myDensityHelper {
        private final Context context;

        public myDensityHelper(Context context) {
            this.context = context;
        }

        /*
        根据手机的分辨率从 dp 单位转为 px（像素）
        */
        public int dp2px(float dpValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }
    }

    public static class myCalenderHelper {
        private static final Calendar cal = Calendar.getInstance();
        static String[] ChineseDayOfWeek = {"日", "一", "二", "三", "四", "五", "六"};

        public static int getYear() {
            return cal.get(Calendar.YEAR);
        }

        public static int getMonth() {
            return (cal.get(Calendar.MONTH)) + 1;
        }

        public static int getDay() {
            return cal.get(Calendar.DAY_OF_MONTH);
        }

        public static int getHour() {
            return cal.get(Calendar.HOUR_OF_DAY);
        }

        public static int getMinute() {
            return cal.get(Calendar.MINUTE);
        }

        public static int getSecond() {
            return cal.get(Calendar.SECOND);
        }

        public static int getDayOfWeek() {
            return cal.get(Calendar.DAY_OF_WEEK);
        }

        public static String getChineseDayOfWeek() {
            return ChineseDayOfWeek[getDayOfWeek() - 1];
        }

        public static String getChineseTotal() {
            String d = String.valueOf(myUtils.myCalenderHelper.getDay());
            String m = String.valueOf(myUtils.myCalenderHelper.getMonth());
            String y = String.valueOf(myUtils.myCalenderHelper.getYear());
            return y + "年" + m + "月" + d + "日" + "    " + "星期" + myCalenderHelper.getChineseDayOfWeek();
        }

        public static String getSeason() {
            int month = getMonth();
            if (month == 3 || month == 4 || month == 5) return "Spring";
            else if (month == 6 || month == 7 || month == 8) return "Summer";
            else if (month == 9 || month == 10 || month == 11) return "Autumn";
            else return "Winter";
        }
    }

    public static class myPermissionManager extends AppCompatActivity {
        private final FragmentActivity myActivity;

        public myPermissionManager(FragmentActivity main) {
            this.myActivity = main;
        }

        public void applyForLocationPermission(ActivityResultLauncher<String> requestPermissionLauncher) {
            Log.d("this", "mmm");
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        public void getPermissionDialog() {
            if (ContextCompat.checkSelfPermission(myActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                myLocationPermissionDialogFragment mdf = new myLocationPermissionDialogFragment();
                mdf.show(myActivity.getSupportFragmentManager(), "aa");
            }
        }
    }

    public static class myWindowManager extends AppCompatActivity {
        public void setWindow(Activity myActivity) {
            View decorView = myActivity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;      //   MainActivity写了太多的冗杂代码  代码太臃肿了   你是想在MainActivity中写一万行？

            decorView.setSystemUiVisibility(option);
            //设置导航栏颜色为透明
            //getWindow().setNavigationBarColor(Color.TRANSPARENT);
            //设置通知栏颜色为透明
            myActivity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public static class myNavigationManagerForMainActivity extends AppCompatActivity {
        private final FragHome fragHome = new FragHome();
        private final FragList fragList = new FragList();
        private final FragMine fragMine = new FragMine();
        private final FragCalendar fragCalendar = new FragCalendar();
        private ArrayList<Fragment> myFragments;
        private Fragment now;

        public void runNavigation(FragmentActivity myActivity) {
            FragmentManager myFragmentManager = myActivity.getSupportFragmentManager();
            myFragmentManager
                    .beginTransaction()
                    .add(R.id.FL_container, fragHome)
                    .add(R.id.FL_container, fragList)
                    .add(R.id.FL_container, fragMine)
                    .add(R.id.FL_container, fragCalendar)
                    .show(fragHome)
                    .hide(fragList)
                    .hide(fragMine)
                    .hide(fragCalendar)
                    .commit();
            now = fragHome;
            BottomNavigationView navigationView = myActivity.findViewById(R.id.navigation_launch);
            navigationView.setOnNavigationItemSelectedListener(item -> {
                switch (item.getItemId()) {
                    case R.id.home_bottom_navigation:
                        if (now != fragHome) {
                            myFragmentManager
                                    .beginTransaction()
                                    .show(fragHome)
                                    .hide(now)
                                    .commit();
                            now = fragHome;
                        }
                        break;
                    case R.id.task_bottom_navigation:
                        if (now != fragList) {
                            Log.d("asd", "asd");
                            myFragmentManager
                                    .beginTransaction()
                                    .show(fragList)
                                    .hide(now)
                                    .commit();
                            now = fragList;
                        }
                        break;
                    case R.id.mine_bottom_navigation:
                        if (now != fragMine) {
                            myFragmentManager
                                    .beginTransaction()
                                    .show(fragMine)
                                    .hide(now)
                                    .commit();
                            now = fragMine;
                        }
                        break;
                    case R.id.calendar_bottom_navigation:
                        if (now != fragCalendar) {
                            myFragmentManager
                                    .beginTransaction()
                                    .show(fragCalendar)
                                    .hide(now)
                                    .commit();
                            now = fragCalendar;
                        }
                        break;
                }

                // 默认 false
                // false 的话 下面颜色不会变化
                return true;
            });
        }

    }

    public static class myNotificationHelper extends AppCompatActivity {
        private final String channelID;
        private final Context myActivity;

        public myNotificationHelper(Context myActivity, String channelID) {
            this.channelID = channelID;
            this.myActivity = myActivity;
            createNotificationChannel(myActivity.getPackageName(), null);
        }

        public void createNotificationChannel(CharSequence name, String description) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(channelID, name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = myActivity.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        }

        public void sendNotification(int notificationID, String Title, String bigText, int Priority) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(myActivity, channelID)
                    .setSmallIcon(R.drawable.ic_launcher_colored)
                    .setContentTitle(Title)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(bigText))
                    .setPriority(Priority);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(myActivity);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(notificationID, builder.build());
        }
    }

    public static class myToastHelper {
        private static Toast toast = null;

        private myToastHelper() {
        }

        @SuppressLint("ShowToast")
        public static void showText(Context context, String text, int length) {
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(context, text, length);
            toast.show();
        }
    }

    public static class myViewMover {
        private final View v;
        Boolean init = false;
        int Top, Left;
        int height, width;
        private int lastX, lastY;

        public myViewMover(View view) {
            this.v = view;
        }

        public void move(MotionEvent event, int upX, int downX, int upY, int downY) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            Log.d("toiuch", "ttt");
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.d("abasd", "qqq");
                    if (!init) {
                        Top = v.getTop();
                        Left = v.getLeft();
                        height = v.getHeight();
                        width = v.getWidth();
                        init = true;
                    }
                    lastX = x;
                    lastY = y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.d("as", "as");
                    //计算移动的距离
                    int offX = x - lastX;
                    int offY = y - lastY;
                    //调用layout方法来重新放置它的位置

                    int top = Math.max(Math.min(v.getTop() + offY, Top + downY), Top + upY);
                    int left = Math.max(Math.min(v.getTop() + offX, Left + upX), Left + downX);
                    v.layout(left, top,
                            left + width, top + height);
                    break;
            }
        }
    }

    public static class myCountDownTimerHelper {
        private final long mMillisTotal;
        private final timer_handler TH;
        private long mMillisRemain;
        private boolean isRunning = false;
        private CountDownTimer CDT;

        public myCountDownTimerHelper(int secInFuture, timer_handler TH) {
            mMillisTotal = (long) secInFuture * 1000 + 500;
            mMillisRemain = mMillisTotal;
            this.TH = TH;
            this.TH.onCreateEvent(secInFuture, TH);
        }

        public void mStart() {
            isRunning = true;
            CDT = new CountDownTimer(mMillisRemain, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Log.d("aa", String.valueOf(millisUntilFinished));
                    mMillisRemain = millisUntilFinished;
                    TH.onTickEvent();
                }

                @Override
                public void onFinish() {
                    mMillisRemain = 0;
                    TH.onFinishEvent();
                }
            };
            CDT.start();
        }

        public void mPause() {
            isRunning = false;
            CDT.cancel();
        }

        public void mCancel() {
            CDT.cancel();
        }

        public Boolean getIsRunning() {
            return isRunning;
        }

        public long getmMillisTotal() {
            return mMillisTotal;
        }

        public long getmMillisRemain() {
            return mMillisRemain;
        }

        @SuppressLint("DefaultLocale")
        private String millisToString(long millis) {
            int res = (int) (millis / 1000);
            int sec = res % 60;
            int min = res / 60 % 60;
            int hour = res / 3600 % 24;
            return String.format("%02d:%02d:%02d", hour, min, sec);
        }

        public String getTimeRemain() {
            return millisToString(mMillisRemain);
        }

        public String getTimeTotal() {
            return millisToString(mMillisTotal);
        }
    }

    public static class beeper {
        public static final int scrollWheel = 1; // 滚轮
        private static SoundPool soundPool = null;
        private static int nowSound;
        private final HashMap<Integer, Integer> soundID = new HashMap<>();

        public beeper(Context context) {
            if (soundPool == null) {
                AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
                soundPool = new SoundPool.Builder().setMaxStreams(16).setAudioAttributes(audioAttributes).build();
                soundID.put(scrollWheel, soundPool.load(context, R.raw.scroll_wheel, 1));
            }
        }

        public void play(int sound) {
            if (soundID.containsKey(sound)) {
                nowSound = soundPool.play(soundID.get(sound), 1, 1, 1, 0, 1);
            } else throw new IllegalStateException("Unexpected value: " + sound);
        }

        public void stop() {
            soundPool.stop(nowSound);
        }
    }
}
