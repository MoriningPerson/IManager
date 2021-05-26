package com.greyka.imgr.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Data {
    public static class Task{

        public Task(long task_id, String task_name, String task_description, String create_date, String start_date,
                    int duration, int cycle, int repeat_count, String end_date,
                    String place_name, double longitude, double latitude, int remind,
                    int allowed, int clock, int completed, int task_week_id) {
            this.task_id = task_id;
            this.task_name = task_name;
            this.task_description = task_description;
            this.create_date = create_date;
            this.start_date = start_date;
            this.duration = duration;
            this.cycle = cycle;
            this.repeat_count = repeat_count;
            this.end_date = end_date;
            this.place_name = place_name;
            this.longitude = longitude;
            this.latitude = latitude;
            this.remind = remind;
            this.allowed = allowed;
            this.clock = clock;
            this.completed = completed;
            this.task_week_id = task_week_id;
        }

        public Task(){}

        public static Task task1 = new Task(1,"打太极拳","一日之计在于晨","2021/5/23","2021/5/23 06:00:00",60,2,20,"2021/7/1",
                "长风公园",0,0,1,1,0,1,1);
        public static Task task2 = new Task(2,"UML","太难了","2021/5/10","2021/5/10 10:00:00",60,7,2,"2021/5/24",
                "田家炳",0,0,1,1,0,1,2);
        public static Task task3 = new Task(3,"数据库","考太差了","2021/4/9","2021/4/9 14:00:00",120,7,3,"2021/4/30",
                "图书馆",0,0,1,1,0,0,3);
        public static Task task4 = new Task(4,"打网球","体育不能挂科","2021/5/23","2021/5/23 18:00:00",40,7,2,"2021/6/6",
                "网球场",0,0,1,1,0,0,4);
        public static Task task5 = new Task(5,"健步走","体育不能挂科","2021/5/23","2021/5/23 20:00:00",30,7,2,"2021/6/6",
                "共青场",0,0,1,1,0,0,5);

        public static List<Task> taskList = Arrays.asList(task1, task2, task3, task4, task5,task1, task2, task3, task4, task5);


        private long task_id;
        private String task_name;
        private String task_description;
        private String create_date;
        private String start_date;
        private int duration;
        //其实我不确定要不要加，是否循环，是否添加地点，这种选项
        private int cycle;
        private int repeat_count;//这个还完全没处理，到时候看下需求
        private String end_date;//我希望是这一天的0点之后，就结束了，所以赋值为最后一天的后一天
        private String place_name;
        private double longitude;
        private double latitude;
        private int remind;
        private int allowed;
        private int clock;
        private int completed;
        private int task_week_id;


        public long getTask_id() {
            return task_id;
        }

        public void setTask_id(long task_id) { this.task_id = task_id; }

        public String getTask_name() {
            return task_name;
        }

        public void setTask_name(String task_name) {
            this.task_name = task_name;
        }

        public String getTask_description() {
            return task_description;
        }

        public void setTask_description(String task_description) {
            this.task_description = task_description;
        }

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }

        public String getStart_date() {
            return start_date;
        }

        public void setStart_date(String start_date) {
            this.start_date = start_date;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getCycle() {
            return cycle;
        }

        public void setCycle(int cycle) {
            this.cycle = cycle;
        }

        public int getRepeat_count() {
            return repeat_count;
        }

        public void setRepeat_count(int repeat_count) {
            this.repeat_count = repeat_count;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public String getPlace_name() {
            return place_name;
        }

        public void setPlace_name(String place_name) {
            this.place_name = place_name;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public int getRemind() {
            return remind;
        }

        public void setRemind(int remind) {
            this.remind = remind;
        }

        public int getAllowed() {
            return allowed;
        }

        public void setAllowed(int allowed) {
            this.allowed = allowed;
        }

        public int getClock() {
            return clock;
        }

        public void setClock(int clock) {
            this.clock = clock;
        }

        public int getCompleted() {
            return completed;
        }

        public void setCompleted(int completed) {
            this.completed = completed;
        }

        public int getTask_week_id() {
            return task_week_id;
        }

        public void setTask_week_id(int task_week_id) {
            this.task_week_id = task_week_id;
        }
    }

    public static class Plan{
        public Plan(long plan_id, String plan_name, String plan_description, String plan_date) {
            this.plan_id = plan_id;
            this.plan_name = plan_name;
            this.plan_description = plan_description;
            this.plan_date = plan_date;
        }

        private long plan_id;
        private String plan_name;
        private String plan_description;
        private String plan_date;

        public Plan(){}
        public static Plan plan1 = new Plan(1,"做晨练","一日之计在于晨","2021/5/23");
        public static Plan plan2 = new Plan(2,"期末复习","风雨过后才有彩虹","2021/5/23");
        public static Plan plan3 = new Plan(3,"体育打卡","课外活动也很重要","2021/5/23");
        public static Plan plan4 = new Plan(4,"每天7杯水","水是生命之源","2021/5/23");
        public static Plan plan5 = new Plan(5,"每天读英语","学好英语走遍天下都不怕","2021/5/23");
        public static Plan plan6 = new Plan(6,"背书毛概","小心胡航不给你过","2021/5/23");
        public static Plan plan7 = new Plan(7,"背200个单词","你是想裸考了吗？","2021/5/23");

        public static List<Plan> planList = Arrays.asList(plan1, plan2, plan3, plan4, plan5,plan6, plan7);

        public long getPlan_id() {
            return plan_id;
        }

        public void setPlan_id(long plan_id) {
            this.plan_id = plan_id;
        }

        public String getPlan_name() {
            return plan_name;
        }

        public void setPlan_name(String plan_name) {
            this.plan_name = plan_name;
        }

        public String getPlan_description() {
            return plan_description;
        }

        public void setPlan_description(String plan_description) {
            this.plan_description = plan_description;
        }

        public String getPlan_date() {
            return plan_date;
        }

        public void setPlan_date(String plan_date) {
            this.plan_date = plan_date;
        }
    }
}
