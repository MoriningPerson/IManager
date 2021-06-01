package com.greyka.imgr.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.greyka.imgr.R;
import com.greyka.imgr.activities.Timer;
import com.greyka.imgr.classes.mottoManager;
import com.greyka.imgr.data.Data;
import com.greyka.imgr.data.Data.Task;
import com.greyka.imgr.dialogs.BaseFullBottomSheetFragment;
import com.greyka.imgr.dialogs.TaskListSelector;
import com.greyka.imgr.dialogs.TodayTaskDialog;
import com.greyka.imgr.utilities.myUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FragHome extends Fragment {

    Data data = new Data();
    Task taskExample= data.new Task();

    public Task task1 = data.new Task(1,"打太极拳","一日之计在于晨","2021/5/23","2021/5/23 06:00:00",60,2,20,"2021/7/1",
            "长风公园",0,0,1,1,0,1,"06:00:00","07:00:00",0,0,0,0,0,0,0,0);
    public Task task2 = data.new Task(2,"UML","太难了","2021/5/10","2021/5/10 10:00:00",60,7,2,"2021/5/24",
            "田家炳",0,0,1,1,0,2,"10:00:00","11:00:00",0,0,0,0,0,0,0,0);
    public Task task3 = data.new Task(3,"数据库","考太差了","2021/4/9","2021/4/9 14:00:00",120,7,3,"2021/4/30",
            "图书馆",0,0,1,1,0,0,"14:00:00","16:00:00",0,0,0,0,0,0,0,0);
    public Task task4 = data.new Task(4,"打网球","体育不能挂科","2021/5/23","2021/5/23 18:00:00",40,7,2,"2021/6/6",
            "网球场",0,0,1,1,0,1,"18:00:00","18:40:00",0,0,0,0,0,0,0,0);
    public Task task5 = data.new Task(5,"健步走","体育不能挂科","2021/5/23","2021/5/23 20:00:00",30,7,2,"2021/6/6",
            "共青场",0,0,1,1,0,2,"20:00:00","20:30:00",0,0,0,0,0,0,0,0);



    private List<Task> taskCompleted[] = new List[2];

    private CardView timer;
    private CardView button2;
    private ImageButton refresh;
    private TodayTaskDialog todayTaskDialog;
    private TaskListSelector taskListSelector;
    private CardView add_task;
    private TextView home_string_next;
    private TextView home_string_next_tasktitle;
    private TextView task_time_location;
    private TextView uncomplete_percent;
    private TextView complete_percent;
    private TextView total_complete_percent;
    private TextView motto;
    private TextView home_notice_board_title;
    private ImageView todayTaskCompleted;
    private ImageView todayTaskUncompleted;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_home, container, false);
    }
    public void showSelectorDialog(int completed) {
        refreshTaskList();
        taskListSelector = new TaskListSelector(getActivity(),getFragmentManager(),taskCompleted[completed]);
        //taskListSelector.setCancelable(true);
        taskListSelector.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        taskListSelector.show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        initViews();
        refreshHomeData();
        /*button2 = view.findViewById(R.id.home_button2);
        button2.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MapDisplayActivity.class);
            startActivity(intent);
        });*/
    }
    private void bindViews(View view){
        todayTaskCompleted = view.findViewById(R.id.home_complete);
        todayTaskUncompleted = view.findViewById(R.id.home_uncomplete);
        add_task = view.findViewById(R.id.home_add_button);
        total_complete_percent = view.findViewById(R.id.total_complete_percent);
        timer = view.findViewById(R.id.home_timer_button);
        home_notice_board_title = view.findViewById(R.id.home_notice_board_title);
        home_string_next = view.findViewById(R.id.home_string_next);
        home_string_next_tasktitle = view.findViewById(R.id.home_string_next_tasktitle);
        task_time_location = view.findViewById(R.id.task_time_location);
        uncomplete_percent = view.findViewById(R.id.uncomplete_percent);
        complete_percent = view.findViewById(R.id.complete_percent);
        refresh = (ImageButton)view.findViewById(R.id.refresh);
        motto = (TextView)view.findViewById(R.id.string_motto);
    }
    private void initViews()
    {
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshHomeData();
            }
        });
        timer.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), Timer.class);
            startActivity(intent);
        });
        todayTaskCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectorDialog(1);
            }
        });
        todayTaskUncompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectorDialog(0);
            }
        });
        add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity","clickAdd");
                BaseFullBottomSheetFragment bfbsf = new BaseFullBottomSheetFragment();
                bfbsf.show(getFragmentManager(), "dialog");
            }
        });
    }
    void refreshHomeData(){
        motto.setText(mottoManager.getRandomMotto());
        home_notice_board_title.setText(myUtils.myCalenderHelper.getChineseTotal());
        total_complete_percent.setText("0%");//今日任务完成比例
        home_string_next.setText("下一任务");//or 正在执行
        home_string_next_tasktitle.setText("任务主题任务主题");//任务title
        task_time_location.setText("00:00:00\n田家炳教育书院");//任务开始时间&地点
        uncomplete_percent.setText("2/5");//未完成/总
        complete_percent.setText("1/5");//完成/总
    }
    private void refreshTaskList(){
        taskCompleted[0] =  Arrays.asList(task3, task3);
        taskCompleted[1] =  Arrays.asList(task1, task2, task4, task5,task1, task2, task4, task5);
        myComparator_task cmp = new myComparator_task();
        Collections.sort(taskCompleted[0],cmp);
        Collections.sort(taskCompleted[1],cmp);
    }
    class myComparator_task implements Comparator {

        @Override
        public int compare(Object t1, Object t2) {
            Data.Task T1 = (Data.Task) t1;
            Data.Task T2 = (Data.Task) t2;
            if (T1.getTodayCompleted() != T2.getTodayCompleted()) {
                return T1.getTodayCompleted() - T2.getTodayCompleted();
            }
            return T1.getStart_time().compareTo(T2.getStart_time());
        }
    }
}

