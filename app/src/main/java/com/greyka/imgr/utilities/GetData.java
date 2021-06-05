package com.greyka.imgr.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.greyka.imgr.data.Data.User;
import com.greyka.imgr.data.Data.Plan;

import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static android.content.Context.MODE_PRIVATE;
import static com.greyka.imgr.utilities.Constants.*;

public class GetData
{
    public static int attemptTestSessionId(Context context)
    {
        try
        {
            String response;
            response = RequestUtil.getWithSession(context, new String("http://1.117.107.95:8081/user/userinfo"));
            if (response == null)
            {
                return NETWORK_UNAVAILABLE;
            }else{
                try {
                    User user = JsonUtil.jsonToUser(response);
                    return POSITIVE_RESPONSE;
                }catch (Exception e){
                    return NEGATIVE_RESPONSE;
                }
            }
        }
        catch (Exception e)
        {
            return EXCEPTION;
        }
    }

    public static int attemptLogin(Context context, String username, String password)
    {
        try
        {
            RequestBody requestBody = new FormBody.Builder()
                    .add("username",username)
                    .add("password",password)
                    .build();
            String response  = RequestUtil.postRequestGetSession(context,new String("http://1.117.107.95:8081/signIn"),requestBody);
            if (response == null){
                return NO_RESPONSE;
            }else if(response.equals("1")) {
                return NEGATIVE_RESPONSE;
            }
            else if (response.equals("0")) {
                return POSITIVE_RESPONSE;
            }
            else{
                return UNKNOWN_RESPONSE;
            }
        }
        catch (Exception e)
        {
            return EXCEPTION;
        }
//            if (response == null || response.equals("1"))
//                return "登录失败";
//            else if (response.equals("0"))
//                return "登陆成功";
//            else return "服务器错误";
//        }
//        catch (Exception e)
//        {
//            return "登陆失败";
//        }
    }
    public static int attemptLogout(Context context)
    {
        try
        {
            String response  = RequestUtil.postRequestWithSessionWithoutParameter(context,new String("http://1.117.107.95:8081/signOut"));
            SharedPreferences share = context.getSharedPreferences("Session", MODE_PRIVATE);
            SharedPreferences.Editor edit = share.edit();
            edit.remove("sessionid");
            edit.commit();
            return POSITIVE_RESPONSE;
        }
        catch (Exception e)
        {
            return EXCEPTION;
        }
    }
    public static int attemptRegister(User user)
    {
        try
        {
            MediaType type = MediaType.parse("application/json;charset=utf-8");
            RequestBody requestBody = FormBody.create(type,JsonUtil.userToJson(user));
            String response = RequestUtil.postRequestWithoutSession("http://1.117.107.95:8081/register",requestBody);
            if (response.equals("创建成功")) {
                return POSITIVE_RESPONSE;
            }else if(response.equals("用户名重复")) {
                return NEGATIVE_RESPONSE;
            }else if(response.equals("创建失败")) {
                return ERROR_RESPONSE;
            }else{
                Log.d("abc",response);
                return UNKNOWN_RESPONSE;
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return EXCEPTION;
        }
    }

    public static User attemptQueryUser(Context context)
    {
        try
        {
            String response = RequestUtil.getWithSession(context,new String("http://1.117.107.95:8081/user/userinfo"));
            Log.d("response",response);
            if (response == null)
            {
                return null;
            }
            else
            {
                return JsonUtil.jsonToUser(response);
            }
        }
        catch (Exception e)
        {
            return null;
        }
    }




    public static String attemptUpdateUsername(Context context,String name)
    {
        try
        {
            RequestBody requestBody = new FormBody.Builder()
                    .add("username",name)
                    .build();
            String response = RequestUtil.postRequestWithSession(context,new String("http://1.117.107.95:8081/user/update/username"),requestBody);
            if (response == null)
                return "更新失败";
            else return response;
        }
        catch (Exception e)
        {
            return "更新失败";
        }
    }


    public static ArrayList<Plan> attemptQueryPlans(Context context)
    {
        try
        {
            String url = "http://1.117.107.95:8081/user/my_plan";
            String response = RequestUtil.getWithSession(context,url);
            Log.d("response",response);
            if (response == null)
                return null;
            else
                return JsonUtil.jsonToPlans(response);
        }
        catch (Exception e)
        {
            return null;
        }
    }




}
