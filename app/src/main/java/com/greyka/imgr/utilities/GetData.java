package com.greyka.imgr.utilities;

import android.content.Context;
import android.util.Log;


import com.greyka.imgr.data.Data.User;
import com.greyka.imgr.data.Data.Task;
import com.greyka.imgr.data.Data.Plan;

import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;



public class GetData
{
    public static String attemptLogin(Context context, String username, String password)
    {
        try
        {
            RequestBody requestBody = new FormBody.Builder()
                    .add("username",username)
                    .add("password",password)
                    .build();
            String response  = RequestUtil.postRequestGetSession(context,new String("http://10.0.2.2:8080/signIn"),requestBody);
            Log.d("response",response);
            if (response == null || response.equals("1"))
                return "登录失败";
            else if (response.equals("0"))
                return "登陆成功";
            else return "服务器错误";
        }
        catch (Exception e)
        {
            return "登陆失败";
        }
    }

    public static String attemptRegister(User user)
    {
        try
        {
            MediaType type = MediaType.parse("application/json;charset=utf-8");
            RequestBody requestBody = FormBody.create(type,JsonUtil.userToJson(user));
            String response = RequestUtil.postRequestWithoutSession("http://10.0.2.2:8080/register",requestBody);
            if (response == null)
                return "注册失败";
            else return response;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return "注册失败";
        }
    }

    public static User attemptQueryUser(Context context)
    {
        try
        {
            String response = RequestUtil.getWithSession(context,new String("http://10.0.2.2:8080/user/userinfo"));
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
            String response = RequestUtil.postRequestWithSession(context,new String("http://10.0.2.2:8080/user/update/username"),requestBody);
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
            String url = "http://10.0.2.2:8080/user/my_plan";
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
