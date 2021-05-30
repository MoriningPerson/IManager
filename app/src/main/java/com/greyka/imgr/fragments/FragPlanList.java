package com.greyka.imgr.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.greyka.imgr.R;
import com.greyka.imgr.adapters.PlanDialogMemberAdapter;
import com.greyka.imgr.adapters.TaskDialogMemberAdapter;
import com.greyka.imgr.data.Data;
import com.greyka.imgr.data.Data.Task;
import com.greyka.imgr.dialogs.TaskItemDialog;
import com.greyka.imgr.dialogs.TaskListSelector;
import com.greyka.imgr.dialogs.TodayTaskDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FragPlanList extends Fragment implements PlanDialogMemberAdapter.OnItemClickListener{

    Data data = new Data();
    Task taskExample= data.new Task();
    public Task task1 = data.new Task(1,"打太极拳","一日之计在于晨","2021/5/23","2021/5/23 06:00:00",60,2,20,"2021/7/1",
            "长风公园",0,0,1,1,0,1,1,"06:00:00","07:00:00");
    public Task task2 = data.new Task(2,"UML","太难了","2021/5/10","2021/5/10 10:00:00",60,7,2,"2021/5/24",
            "田家炳",0,0,1,1,0,1,2,"10:00:00","11:00:00");
    public Task task3 = data.new Task(3,"数据库","考太差了","2021/4/9","2021/4/9 14:00:00",120,7,3,"2021/4/30",
            "图书馆",0,0,1,1,0,0,3,"14:00:00","16:00:00");
    public Task task4 = data.new Task(4,"打网球","体育不能挂科","2021/5/23","2021/5/23 18:00:00",40,7,2,"2021/6/6",
            "网球场",0,0,1,1,0,0,4,"18:00:00","18:40:00");
    public Task task5 = data.new Task(5,"健步走","体育不能挂科","2021/5/23","2021/5/23 20:00:00",30,7,2,"2021/6/6",
            "共青场",0,0,1,1,0,0,5,"20:00:00","20:30:00");

    private List<Task> taskList = Arrays.asList(task1, task2, task3, task4, task5,task1, task2, task3, task4, task5);


    public static final String ARG_OBJECT = "object";
    private Context context;
    private static List<Data.Plan> planList;
    private RecyclerView rv_selector_branch;
    private PlanDialogMemberAdapter mSelectorBranchAdapter;
    private static int mPosition;
    private View view;
    private Data.Plan plan;
    private TaskListSelector taskListSelector;

    public FragPlanList(Context context) {
        super();
        Log.d("aaa","vbreage");
        this.context = context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_plan_list, container, false);
    }

    private void InitViews(View view) {
        rv_selector_branch = (RecyclerView)view.findViewById(R.id.plan_recycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_selector_branch.setLayoutManager(layoutManager);
        mSelectorBranchAdapter = new PlanDialogMemberAdapter(planList);
        mSelectorBranchAdapter.setOnItemClickListener(this);
        rv_selector_branch.setAdapter(mSelectorBranchAdapter);
    }






    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view=view;
        refreshPlanList();
        InitViews(view);
    }

    public void showSelectorDialog() {
        taskListSelector = new TaskListSelector(getActivity(),taskList);
        taskListSelector.setCancelable(false);
        taskListSelector.show();
    }

    @Override
    public void onItemClick(int position) {
        mPosition=position;
        plan=planList.get(mPosition);
        Long plan_id=plan.getPlan_id();
        //请求plan_id的不重复的taskList
        showSelectorDialog();
    }
    public static void refreshPlanList(){
        planList = new ArrayList<>();//获取任务
        myComparator_plan cmp = new myComparator_plan();
        Collections.sort(planList,cmp);
        Log.d("ref","plan");
    }
}
class myComparator_plan implements Comparator {

    @Override
    public int compare(Object t1, Object t2) {
        Data.Plan T1 = (Data.Plan)t1;
        Data.Plan T2 = (Data.Plan)t2;
        if(T1.getCompleted() != T2.getCompleted()){
            return T1.getCompleted() - T2.getCompleted();
        }
        return T1.getPlan_date().compareTo(T2.getPlan_date());
    }
}
