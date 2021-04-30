package com.greyka.imgr.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.greyka.imgr.R;
import com.greyka.imgr.fragments.myDialogFragment;
import com.greyka.imgr.utilities.myUtils;

public class MainActivity extends AppCompatActivity  implements myDialogFragment.NoticeDialogListener {

    private final ActivityResultLauncher<String> requestPermissionLauncher=
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    myUtils.myToastHelper.showText(this,"获取权限成功",Toast.LENGTH_SHORT);
                } else {
                    myUtils.myToastHelper.showText(this,"获取权限失败",Toast.LENGTH_SHORT);
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new myUtils.myWindowManager().setWindow(this);
        new myUtils.myPermissionManager(this).getPermissionDialog();
        new myUtils.myNavigationManagerForMainActivity().runNavigation(this);
    }
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        new myUtils.myPermissionManager(this).applyForLocationPermission(requestPermissionLauncher);
    }

}