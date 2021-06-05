package com.greyka.imgr.utilities;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.greyka.imgr.data.Data;
import com.greyka.imgr.data.Data.User;
import com.greyka.imgr.data.Data.Plan;
import com.greyka.imgr.data.Data.Task;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static android.content.Context.MODE_PRIVATE;
import static com.greyka.imgr.utilities.Constants.*;

public class GetData
{
    //测过
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
                return NETWORK_UNAVAILABLE;
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
            Log.d("abcde",e.getMessage());
            return EXCEPTION;
        }
    }
    public static int attemptLogout(Context context)
    {
        try
        {
            String response  = RequestUtil.postRequestWithSessionWithoutParameter(context,new String("http://1.117.107.95:8081/signOut"));
            if(response==null)
                return NETWORK_UNAVAILABLE;
            else {
                return POSITIVE_RESPONSE;
            }
        }
        catch (Exception e)
        {
            return EXCEPTION;
        }
    }

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
    //测过
    public static int attemptRegister(User user)
    {
        try
        {
            MediaType type = MediaType.parse("application/json;charset=utf-8");
            RequestBody requestBody = FormBody.create(type,JsonUtil.userToJson(user));
            String response = RequestUtil.postRequestWithoutSession("http://1.117.107.95:8081/register",requestBody);
            //Log.d("okk", response);
            if (response == null) {
                return NETWORK_UNAVAILABLE;
            }else if(response.equals("用户名重复")){
                return NEGATIVE_RESPONSE;
            }
            else if(response.equals("创建成功")){
                return POSITIVE_RESPONSE;
            }else if(response.equals("创建失败")){
                return ERROR_RESPONSE;
            }else {
                return UNKNOWN_RESPONSE;
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return EXCEPTION;
        }
    }

    public static int attemptDeleteUser(Context context)
    {
        try
        {
            String response  = RequestUtil.postRequestWithSessionWithoutParameter(context,new String("http://1.117.107.95:8081/signOut"));
            if(response==null)
                return NETWORK_UNAVAILABLE;
            else if(response.equals("删除成功")){
                return POSITIVE_RESPONSE;
            }else if(response.equals("删除失败")){
                return POSITIVE_RESPONSE;
            }else{
                return UNKNOWN_RESPONSE;
            }
        }
        catch (Exception e)
        {
            return EXCEPTION;
        }
    }

    //没测过
    public static User attemptQueryUser(Context context)
    {
        try
        {
            String response;
            try {
                response = RequestUtil.getWithSession(context, new String("http://1.117.107.95:8081/user/userinfo"));
            }catch (Exception e){
                return null;
            }
            if (response == null)
            {
                return null;
            }else{
                return JsonUtil.jsonToUser(response);
            }
        }
        catch (Exception e)
        {
            return null;
        }
    }

    //如果她接好了的话？
    public static ArrayList<Plan> attemptQueryPlans(Context context)
    {
        try
        {
            String url = "http://1.117.107.95:8081/user/my_plan";
            String response = RequestUtil.getWithSession(context,url);
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
    //等下测
    public static long attemptCreatePlan(Context context,Plan plan)
    {
        try
        {
            String url = "http://1.117.107.95:8081/user/create/plan";
            MediaType type = MediaType.parse("application/json;charset=utf-8");
            RequestBody requestBody = FormBody.create(type,JsonUtil.planToJson(plan));
            String response = RequestUtil.postRequestWithSession(context,url,requestBody);
            if(response==null){
                return NETWORK_UNAVAILABLE;
            }else if (response.equals("-1")){
                return ERROR_RESPONSE;
            }else {
                try {
                    Log.d("asdfgrthy",response);
                    long plan_id= Long.parseLong(response);
                    return plan_id;
                }catch (Exception e){
                    return UNKNOWN_RESPONSE;
                }
            }
        }
        catch (Exception e)
        {
            Log.d("abcdde",e.getMessage());
            return EXCEPTION;
        }
    }
    public static int attemptAddTaskToPlan(Context context,long plan_id,long task_id)
    {
        try
        {
            String url = "http://1.117.107.95:8081/user/relate/plan/task";
            RequestBody requestBody = new FormBody.Builder()
                    .add("plan_id", String.valueOf(plan_id))
                    .add("task_id", String.valueOf(task_id))
                    .build();
            String response = RequestUtil.postRequestWithSession(context,url,requestBody);
            if(response==null){
                return NETWORK_UNAVAILABLE;
            }else if(response.equals("0")){
                return POSITIVE_RESPONSE;
            }else{
                return UNKNOWN_RESPONSE;
            }
        }
        catch (Exception e)
        {
            return EXCEPTION;
        }
    }
    ///////////////////
    public static int attemptDeletePlan(Context context,long plan_id)
    {
        try
        {
            String url = "http://1.117.107.95:8081/user/delete/plan";
            RequestBody requestBody = new FormBody.Builder()
                    .add("plan_id", String.valueOf(plan_id))
                    .build();
            String response = RequestUtil.postRequestWithSession(context,url,requestBody);
            if(response==null){
                return NETWORK_UNAVAILABLE;
            }else if(response.equals("删除成功")){
                return POSITIVE_RESPONSE;
            }else if(response.equals("删除失败")){
                return ERROR_RESPONSE;
            }else{
                return UNKNOWN_RESPONSE;
            }
        }
        catch (Exception e)
        {
            return EXCEPTION;
        }
    }

    //从这开始要重测
    public static int attemptCreateTask(Context context,Task task)
    {
        try
        {
            String url = "http://1.117.107.95:8081/user/create/task";
            MediaType type = MediaType.parse("application/json;charset=utf-8");
            RequestBody requestBody = FormBody.create(type,JsonUtil.taskToJson(task));
            String response = RequestUtil.postRequestWithSession(context,url,requestBody);
            if(response==null){
                return NETWORK_UNAVAILABLE;
            }else if(response.equals("有时间冲突，创建失败")){
                return NEGATIVE_RESPONSE;
            }else if(response.equals("创建成功")){
                return POSITIVE_RESPONSE;
            }else if(response.equals("创建失败")||response.equals("日期格式错误")||response.equals("传入的循环类型不存在")){
                return ERROR_RESPONSE;
            }else{
                return UNKNOWN_RESPONSE;
            }
        }
        catch (Exception e)
        {
            return EXCEPTION;
        }
    }

    public static int attemptDeleteTask(Context context,long task_id)
    {
        try
        {
            String url = "http://1.117.107.95:8081/user/delete/task";
            RequestBody requestBody = new FormBody.Builder()
                    .add("task_id", String.valueOf(task_id))
                    .build();
            String response = RequestUtil.postRequestWithSession(context,url,requestBody);
            if(response==null){
                return NETWORK_UNAVAILABLE;
            }else if(response.equals("删除成功")){
                return POSITIVE_RESPONSE;
            }else if(response.equals("删除失败")){
                return ERROR_RESPONSE;
            }else{
                return UNKNOWN_RESPONSE;
            }
        }
        catch (Exception e)
        {
            return EXCEPTION;
        }
    }
    public static List<Task> attemptGetUserAllTask(Context context)
    {
        try
        {
            String url = "http://1.117.107.95:8081/user/task/all";
            String response = RequestUtil.getWithSession(context,url);
            //Log.d("response",response);
            if (response == null){
                return null;
            }else{
                return JsonUtil.jsonToTasks(response);
            }
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static List<Task> attemptGetUserAllUnCompletedTask(Context context)
    {
        try
        {
            String url = "http://1.117.107.95:8081/user/task/allUncompleted";
            String response = RequestUtil.getWithSession(context,url);
            //Log.d("response",response);
            if (response == null){
                return null;
            }else{
                return JsonUtil.jsonToTasks(response);
            }
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static List<Task> attemptGetTodayAllTask(Context context)
    {
        try
        {
            String url = "http://1.117.107.95:8081/user/task/today";
            String response = RequestUtil.getWithSession(context,url);
            Log.d("response",response);
            if (response == null){
                return null;
            }else{
                return JsonUtil.jsonToTasks(response);
            }
        }
        catch (Exception e)
        {
            return null;
        }
    }
    public static List<Task> attemptGetTodayCompletedTask(Context context)
    {
        try
        {
            String url = "http://1.117.107.95:8081/user/today/succeed";
            String response = RequestUtil.getWithSession(context,url);
            Log.d("response",response);
            if (response == null){
                return null;
            }else{
                return JsonUtil.jsonToTasks(response);
            }
        }
        catch (Exception e)
        {
            return null;
        }
    }
    //既包括失败也包括未完成
    public static List<Task> attemptGetTodayUncompletedTask(Context context)
    {
        try
        {
            List<Task> tasks=new ArrayList<Task>();
            String url1 = "http://1.117.107.95:8081/user/today/uncompleted";
            String url2 = "http://1.117.107.95:8081/user/today/failed";
            String response1 = RequestUtil.getWithSession(context,url1);
            String response2 = RequestUtil.getWithSession(context,url2);
            Log.d("response",response1);
            if (response1 == null){
                return null;
            }else{
                tasks.addAll(JsonUtil.jsonToTasks(response1));
            }
            if (response2 == null){
                return null;
            }else{
                tasks.addAll(JsonUtil.jsonToTasks(response2));
            }
            return tasks;
        }
        catch (Exception e)
        {
            return null;
        }
    }
    public static List<Task> attemptGetSomedayAllTask(Context context,String date)
    {
        try
        {
            String url = "http://1.117.107.95:8081/user/task/someday?date="+date;
            String response = RequestUtil.getWithSession(context,url);
            Log.d("response",response);
            Log.d("someday","1");
            if (response == null){
                Log.d("someday","2");
                return null;
            }
            else{
                Log.d("someday","3");
                return JsonUtil.jsonToTasks(response);
            }
        }
        catch (Exception e)
        {
            Log.d("someday","4");
            return null;
        }
    }
    public static List<Task> attemptGetSomedayCompletedTask(Context context,String date)
    {
        try
        {
            String url = "http://1.117.107.95:8081/user/task/succeed?date="+date;
            String response = RequestUtil.getWithSession(context,url);
            Log.d("response",response);
            if (response == null)
                return null;
            else
                return JsonUtil.jsonToTasks(response);
        }
        catch (Exception e)
        {
            return null;
        }
    }
    //既包括失败也包括未完成
    public static List<Task> attemptGetSomedayUncompletedTask(Context context,String date)
    {
        try
        {
            List<Task> tasks=new ArrayList<Task>();
            String url1 = "http://1.117.107.95:8081/user/task/uncompleted?date="+date;
            String url2 = "http://1.117.107.95:8081/user/task/failed?date="+date;
            String response1 = RequestUtil.getWithSession(context,url1);
            String response2 = RequestUtil.getWithSession(context,url2);
            Log.d("response",response1);
            if (response1 == null){
                return null;
            }else{
                tasks.addAll(JsonUtil.jsonToTasks(response1));
            }
            if (response2 == null){
                return null;
            }else{
                tasks.addAll(JsonUtil.jsonToTasks(response2));
            }
            return tasks;
        }
        catch (Exception e)
        {
            return null;
        }
    }
    public static List<Task> attemptGetTaskToDo(Context context)
    {
        try
        {
            String url = "http://1.117.107.95:8081/user/task/todo";
            String response = RequestUtil.getWithSession(context,url);
            Log.d("response",response);
            if (response == null){
                return null;
            }else{
                return JsonUtil.jsonToTasks(response);
            }
        }
        catch (Exception e)
        {
            return null;
        }
    }
    public static List<Task> attemptGetTaskNow(Context context)
    {
        try
        {
            String url = "http://1.117.107.95:8081/user/task/now";
            String response = RequestUtil.getWithSession(context,url);
            Log.d("response",response);
            if (response == null){
                return null;
            }else{
                return JsonUtil.jsonToTasks(response);
            }
        }
        catch (Exception e)
        {
            return null;
        }
    }
    public static int attemptSetTaskSucceed(Context context,long task_id)
    {
        try
        {
            String url = "http://1.117.107.95:8081/user/set/ClockInTask/succeed";
            RequestBody requestBody = new FormBody.Builder()
                    .add("task_id", String.valueOf(task_id))
                    .build();
            String response = RequestUtil.postRequestWithSession(context,url,requestBody);
            //Log.d("response",response);
            if(response==null){
                return NETWORK_UNAVAILABLE;
            }else if(response.equals("任务当前不是进行状态")){
                return NEGATIVE_RESPONSE;
            }else if(response.equals("更新成功")){
                return POSITIVE_RESPONSE;
            }else if(response.equals("更新失败")){
                return ERROR_RESPONSE;
            }else{
                return UNKNOWN_RESPONSE;
            }
        }
        catch (Exception e)
        {
            return EXCEPTION;
        }
    }
    public static int attemptSetTaskFailed(Context context,long task_id)
    {
        try
        {
            String url = "http://1.117.107.95:8081/user/set/UnClockInTask/failed";
            RequestBody requestBody = new FormBody.Builder()
                    .add("task_id", String.valueOf(task_id))
                    .build();
            String response = RequestUtil.postRequestWithSession(context,url,requestBody);
            if(response==null){
                return NETWORK_UNAVAILABLE;
            }else if(response.equals("任务当前不是进行状态")){
                return NEGATIVE_RESPONSE;
            }else if(response.equals("更新成功")){
                return POSITIVE_RESPONSE;
            }else if(response.equals("更新失败")){
                return ERROR_RESPONSE;
            }else{
                return UNKNOWN_RESPONSE;
            }
        }
        catch (Exception e)
        {
            return EXCEPTION;
        }
    }
    public static int attemptCancelTask(Context context,long task_id)
    {
        try
        {
            String url = "http://1.117.107.95:8081/user/cancel/task";
            RequestBody requestBody = new FormBody.Builder()
                    .add("task_id", String.valueOf(task_id))
                    .build();
            String response = RequestUtil.postRequestWithSession(context,url,requestBody);
            Log.d("response",response);
            if (response == null){
                return NETWORK_UNAVAILABLE;
            }else if(response.equals("-1")) {
                return ERROR_RESPONSE;
            }else if (response.equals("1"))
                return POSITIVE_RESPONSE;
            else{
                return UNKNOWN_RESPONSE;
            }
        }
        catch (Exception e)
        {
            return EXCEPTION;
        }
    }
    public static List<Data.Task> attemptGetTasksInPlan(Context context, long plan_id)
    {
        try
        {
            String url = "http://1.117.107.95:8081/user/plan/task?plan_id="+String.valueOf(plan_id);
            String response = RequestUtil.getWithSession(context,url);
            //Log.d("response",response);
            if (response == null){
                Log.d("asdfqd","1");
                return null;
            }
            else{
                Log.d("asdfqd","2");
                return JsonUtil.jsonToTasks(response);
            }
        }
        catch (Exception e)
        {
            Log.d("asdfqd","3");
            return null;
        }
    }
}
