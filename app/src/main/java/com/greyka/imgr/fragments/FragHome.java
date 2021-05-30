package com.greyka.imgr.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.greyka.imgr.R;
import com.greyka.imgr.activities.Timer;
import com.greyka.imgr.classes.mottoManager;
import com.greyka.imgr.data.Data.Task;
import com.greyka.imgr.dialogs.BaseFullBottomSheetFragment;
import com.greyka.imgr.dialogs.TaskListSelector;
import com.greyka.imgr.dialogs.TodayTaskDialog;
import com.greyka.imgr.utilities.myUtils;

import java.util.List;

public class FragHome extends Fragment {

    private CardView timer;
    private CardView button2;
    private ImageButton refresh;
    private TodayTaskDialog todayTaskDialog;
    private TaskListSelector taskListSelector;
    private List<Task> taskList=Task.taskList;
    private CardView add_task;
    private TextView home_string_next;
    private TextView home_string_next_tasktitle;
    private TextView task_time_location;
    private TextView uncomplete_percent;
    private TextView complete_percent;
    private TextView total_complete_percent;
    private TextView motto;
    private TextView home_notice_board_title;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_home, container, false);
    }
    public void showSelectorDialog() {
        taskListSelector = new TaskListSelector(getActivity(),taskList);
        taskListSelector.setCancelable(false);
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
        total_complete_percent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity","click");
                showSelectorDialog();


            }
        });
        add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity","clickAdd");
                new BaseFullBottomSheetFragment().show(getFragmentManager(), "dialog");


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
}
