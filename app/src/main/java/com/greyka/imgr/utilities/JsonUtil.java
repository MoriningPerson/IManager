package com.greyka.imgr.utilities;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.greyka.imgr.data.Data.Plan;
import com.greyka.imgr.data.Data.Task;
import com.greyka.imgr.data.Data.User;

import java.util.ArrayList;


public class JsonUtil {
    public static String userToJson(User user) {
        String jsonStr = JSONObject.toJSONString(user);
        return jsonStr;
    }


    public static User jsonToUser(String data) {
        ArrayList<User> user = JSON.parseObject(data, new TypeReference<ArrayList<User>>() {
        });
        return user.get(0);
    }

    public static String taskToJson(Task task) {
        String jsonStr = JSONObject.toJSONString(task);
        return jsonStr;
    }

    public static ArrayList<Task> jsonToTasks(String data) {
        ArrayList<Task> tasks = JSON.parseObject(data, new TypeReference<ArrayList<Task>>() {
        });
        return tasks;
    }

    public static Task jsonToTask(String data) {
        Task task = JSON.parseObject(data, new TypeReference<Task>() {
        });
        return task;
    }

    public static String planToJson(Plan plan) {
        String jsonStr = JSONObject.toJSONString(plan);
        return jsonStr;
    }

    public static ArrayList<Plan> jsonToPlans(String data) {
        ArrayList<Plan> plans = JSON.parseObject(data, new TypeReference<ArrayList<Plan>>() {
        });
        return plans;
    }

    public static Plan jsonToPlan(String data) {
        Plan plan = JSON.parseObject(data, new TypeReference<Plan>() {
        });
        return plan;
    }


}
