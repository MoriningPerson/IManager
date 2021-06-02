package com.greyka.imgr.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.greyka.imgr.R;
import com.greyka.imgr.adapters.myRecyclerViewAdapter;
import com.greyka.imgr.data.Data;
import com.greyka.imgr.data.Data.Task;
import com.greyka.imgr.utilities.myUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FragCalendar extends Fragment {


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

    public List<Task> taskList;

    List<Data.Task> task;
    View memo;
    RecyclerView recyclerView;
    myUtils.myViewMover calenderCardMover;
    myUtils.myDensityHelper density;
    CardView calenderCard;
    CalendarView calendarView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_calendar, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        bindViews(view);
        initViews();
        refreshTodayTask(myUtils.myCalenderHelper.getYear(), myUtils.myCalenderHelper.getMonth(), myUtils.myCalenderHelper.getDay());
        super.onViewCreated(view, savedInstanceState);
    }
    private void bindViews(View view){
        calenderCard = view.findViewById(R.id.Calendar_card);
        memo = view.findViewById(R.id.memo);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.requireContext()));
        calendarView = view.findViewById(R.id.calendarView);
        calenderCardMover = new myUtils.myViewMover(calenderCard);
        density = new myUtils.myDensityHelper(this.requireContext());
    }
    private void initViews(){
        calenderCard.setOnTouchListener((v, event) -> {
            calenderCardMover.move(event, 0, 0, -v.getHeight() + density.dp2px(90), 0);
            memo.layout(memo.getLeft(), calenderCard.getBottom() + density.dp2px(10), memo.getRight(), memo.getBottom());
            recyclerView.layout(recyclerView.getLeft(), recyclerView.getTop(), recyclerView.getRight(), memo.getHeight() - density.dp2px(15));
            return true;
        });
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                myUtils.myToastHelper.showText(view.getContext(), +year + "年" + (month+1) + "月" + dayOfMonth + "日", Toast.LENGTH_SHORT);
                refreshTodayTask(year, month+1 , dayOfMonth);
                recyclerView.setAdapter(new myRecyclerViewAdapter(task));
            }
        });
    }
    void refreshTodayTask(int year, int month, int day){
        //Data data= new Data();
        //Data.Task task=data.new Task();
        //List<Data.Task> tasks=task.taskList; //获取今日任务列表
        taskList = Arrays.asList(task1, task2, task3, task4, task5,task1, task2, task3, task4, task5);
        myComparator_memo cmp = new myComparator_memo();
        Collections.sort(taskList,cmp);
        recyclerView.setAdapter(new myRecyclerViewAdapter(taskList));
    }
    class myComparator_memo implements Comparator {

        @Override
        public int compare(Object t1, Object t2) {
            String str1 = ((Data.Task)(t1)).getStart_time();
            String str2 = ((Data.Task)(t2)).getStart_time();
            return str1.compareTo(str2);
        }
    }
}
