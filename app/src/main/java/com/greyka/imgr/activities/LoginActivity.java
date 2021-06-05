package com.greyka.imgr.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.greyka.imgr.R;
import com.greyka.imgr.data.Data;
import com.greyka.imgr.dialogs.myPermissionDialogFragment;
import com.greyka.imgr.utilities.Constants;
import com.greyka.imgr.utilities.myUtils;

import java.util.Map;
import java.util.Set;

import static com.greyka.imgr.utilities.Constants.ERROR_RESPONSE;
import static com.greyka.imgr.utilities.Constants.EXCEPTION;
import static com.greyka.imgr.utilities.Constants.NEGATIVE_RESPONSE;
import static com.greyka.imgr.utilities.Constants.NETWORK_UNAVAILABLE;
import static com.greyka.imgr.utilities.Constants.NO_RESPONSE;
import static com.greyka.imgr.utilities.Constants.POSITIVE_RESPONSE;
import static com.greyka.imgr.utilities.Constants.UNKNOWN_RESPONSE;
<<<<<<< HEAD
import static com.greyka.imgr.utilities.GetData.*;
=======
import static com.greyka.imgr.utilities.Constants.NETWORK_UNAVAILABLE;
import static com.greyka.imgr.utilities.GetData.attemptLogin;
import static com.greyka.imgr.utilities.GetData.attemptQueryUser;
import static com.greyka.imgr.utilities.GetData.attemptRegister;
>>>>>>> 7ea58c4b62671b15bfc696547187edf07bc62ef5

public class LoginActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> activityResultLauncher;
    private EditText UserName;
    private EditText PassWord;
    private String username = "";
    private String password = "";
    private ImageView Login;
    private View view;
    private boolean logined = false;
    private boolean registerMode = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE |
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
        bindViews();
        initViews();
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserPassword", Context.MODE_PRIVATE);
        String username = sp.getString("username",null);
        SharedPreferences share = getApplicationContext().getSharedPreferences("Session", MODE_PRIVATE);
        //String password = sp.getString("password",null);
        if(username == null){
        }else{
            Log.d("MainActivity","clickLogin");
<<<<<<< HEAD
            int result = attemptTestSessionId(getApplicationContext());
=======
            Data.User temp = attemptQueryUser(getApplicationContext());

            int result = attemptLogin(getApplicationContext(),username,password);
>>>>>>> 7ea58c4b62671b15bfc696547187edf07bc62ef5
            if(result == POSITIVE_RESPONSE){
                myUtils.myToastHelper.showText(getApplicationContext(),"欢迎回来",Toast.LENGTH_SHORT);
                startMainActivity();
            }else{
                if(result == NEGATIVE_RESPONSE) {
                    myUtils.myToastHelper.showText(getApplicationContext(), "登录失效 请重新登录", Toast.LENGTH_LONG);
                }else if(result == NETWORK_UNAVAILABLE){
                    myUtils.myToastHelper.showText(getApplicationContext(), "连接异常 请检查网络", Toast.LENGTH_LONG);
                }
                SharedPreferences.Editor editor = sp.edit();
                editor.remove("username");
                editor.commit();
            }
        }
        myUtils.SoftHideKeyBoardUtil.assistActivity(this);
    }

    private void bindViews(){
        UserName = findViewById(R.id.username_login);
        PassWord = findViewById(R.id.password_login);
        Login = findViewById(R.id.ic_login);
    }
    private void initViews(){
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkUN()){
                    Log.d("aasd",username);
                    myUtils.myToastHelper.showText(getApplicationContext(),"用户名需大于6位且至少包含1位英文字母",Toast.LENGTH_LONG);
                }else if(!checkPW()){
                    myUtils.myToastHelper.showText(getApplicationContext(),"请输入至少8位密码",Toast.LENGTH_LONG);
                }else{
                    if(!registerMode) {
                        submitLogin();
                    }else{
                        submitRegister();
                    }
                }
            }
        });
        Login.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                registerMode = !registerMode;
                if(registerMode) {
                    myUtils.myToastHelper.showText(getApplicationContext(),"请注册",Toast.LENGTH_LONG);
                    Login.setRotation(270);
                }else{
                    myUtils.myToastHelper.showText(getApplicationContext(),"请登录",Toast.LENGTH_LONG);
                    Login.setRotation(0);
                }
                return true;
            }
        });
        UserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                username = s.toString();
            }
        });
        PassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                password = s.toString();
            }
        });
        PassWord.setTransformationMethod(PasswordTransformationMethod
                .getInstance());
    }
    private boolean checkUN(){
        Log.d("aasd",username);
        boolean hasAlpha = false;
        for(int i = 0; i < username.length(); i ++){
            if(username.charAt(i) >= 'a' && username.charAt(i) <= 'z'
                || username.charAt(i) >= 'A' && username.charAt(i) <= 'Z'){
                hasAlpha = true;
            }
        }
        return hasAlpha && username.length() >= 6;
    }
    private boolean checkPW(){
        return password.length() >= 8;
    }

    private void submitRegister(){
        myUtils.myToastHelper.showText(getApplicationContext(),"注册中",Toast.LENGTH_LONG);
        int result = attemptRegister(new Data().new User(0,username,"",password));
        Log.d("abc",result+"");
        if(result == POSITIVE_RESPONSE) {
            myUtils.myToastHelper.showText(getApplicationContext(),"注册成功",Toast.LENGTH_LONG);
            submitLogin();
        }else if(result == NEGATIVE_RESPONSE){
<<<<<<< HEAD
            myUtils.myToastHelper.showText(getApplicationContext(),"注册失败 用户名已被注册",Toast.LENGTH_LONG);
        }else if (result == ERROR_RESPONSE){
            myUtils.myToastHelper.showText(getApplicationContext(),"服务器异常 请稍后再试",Toast.LENGTH_LONG);
        } else if(result == EXCEPTION){
            myUtils.myToastHelper.showText(getApplicationContext(),"连接异常 请检查网络",Toast.LENGTH_LONG);
=======
            myUtils.myToastHelper.showText(getApplicationContext(),"注册失败 请重新尝试或更换用户名",Toast.LENGTH_LONG);
        }else if(result == NETWORK_UNAVAILABLE){
            myUtils.myToastHelper.showText(getApplicationContext(),"无法连接服务器 请检查网络",Toast.LENGTH_LONG);
        }else if(result == UNKNOWN_RESPONSE){
            myUtils.myToastHelper.showText(getApplicationContext(),"未知错误 请重试",Toast.LENGTH_LONG);
        }else if(result == EXCEPTION){
            myUtils.myToastHelper.showText(getApplicationContext(),"出现异常 请重试",Toast.LENGTH_LONG);
        }else if(result == ERROR_RESPONSE){
            myUtils.myToastHelper.showText(getApplicationContext(),"系统异常 请重试",Toast.LENGTH_LONG);
>>>>>>> 7ea58c4b62671b15bfc696547187edf07bc62ef5
        }
    }
    private void submitLogin(){
        myUtils.myToastHelper.showText(getApplicationContext(),registerMode?"注册成功 登陆中":"登陆中",Toast.LENGTH_LONG);
        int result = attemptLogin(getApplicationContext(), username, password);
        Log.d("result",result+"");
        if (result == POSITIVE_RESPONSE) {
            myUtils.myToastHelper.showText(getApplicationContext(), "欢迎登录", Toast.LENGTH_LONG);
            SharedPreferences sp = getApplicationContext().getSharedPreferences("UserPassword", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("username", username);
            editor.putString("password", password);
            editor.commit();
            logined = true;
            startMainActivity();
        } else if(result == NEGATIVE_RESPONSE){
            myUtils.myToastHelper.showText(getApplicationContext(), "登陆失败 请检查用户名和密码", Toast.LENGTH_LONG);
        } else if(result == UNKNOWN_RESPONSE){
            myUtils.myToastHelper.showText(getApplicationContext(), "服务器异常 请重试", Toast.LENGTH_LONG);
        } else if(result == NO_RESPONSE){
            myUtils.myToastHelper.showText(getApplicationContext(), "连接异常 请检查网络", Toast.LENGTH_LONG);
        } else if(result == EXCEPTION){
            myUtils.myToastHelper.showText(getApplicationContext(), "连接异常 请检查网络", Toast.LENGTH_LONG);
        }
    }
    private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}