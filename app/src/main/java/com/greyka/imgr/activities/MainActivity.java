package com.greyka.imgr.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.greyka.imgr.R;
import com.greyka.imgr.fragments.myPermissionDialogFragment;
import com.greyka.imgr.utilities.myUtils;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements myPermissionDialogFragment.NoticeDialogListener {

    private final ActivityResultLauncher<String> requestLocationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    myUtils.myToastHelper.showText(this, "获取位置权限成功", Toast.LENGTH_SHORT);
                } else {
                    myUtils.myToastHelper.showText(this, "获取位置权限失败", Toast.LENGTH_SHORT);
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
        myUtils.myPermissionManager mpm = new myUtils.myPermissionManager(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            mpm.applyForLocationPermission(requestLocationPermissionLauncher);
        }
        if (!mpm.checkFloatingPermission()){
            mpm.applyForFloatingPermission();
        }
    }

}