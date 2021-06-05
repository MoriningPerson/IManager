package com.greyka.imgr.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.greyka.imgr.R;
import com.greyka.imgr.adapters.TaskDialogMemberAdapter;
import com.greyka.imgr.data.Data;
import com.greyka.imgr.data.Data.Task;
import com.greyka.imgr.dialogs.BaseFullBottomSheetFragment;
import com.greyka.imgr.dialogs.ViewUpdator;
import com.greyka.imgr.utilities.GetData;
import com.greyka.imgr.utilities.myUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FragTaskList extends Fragment implements TaskDialogMemberAdapter.OnItemClickListener, ViewUpdator {


    public static final String ARG_OBJECT = "object";
    private static List<Data.Task> taskList = new ArrayList<>();
    private static int mPosition;
    Data data = new Data();
    public Data.Task task1 = new Task(1, "打太极拳", "一日之计在于晨", "2021/5/23", "2021/5/23 06:00:00", 60, 2, 20, "2021/7/1",
            "长风公园", 0, 0, 1, 1, 0, 1, "06:00:00", "07:00:00", 0, 0, 0, 0, 0, 0, 0, 0);
    public Data.Task task2 = new Task(2, "UML", "太难了", "2021/5/10", "2021/5/10 10:00:00", 60, 7, 2, "2021/5/24",
            "田家炳", 0, 0, 1, 1, 0, 2, "10:00:00", "11:00:00", 0, 0, 0, 0, 0, 0, 0, 0);
    public Data.Task task3 = new Task(3, "数据库", "考太差了", "2021/4/9", "2021/4/9 14:00:00", 120, 7, 3, "2021/4/30",
            "图书馆", 0, 0, 1, 1, 0, 0, "14:00:00", "16:00:00", 0, 0, 0, 0, 0, 0, 0, 0);
    public Data.Task task4 = new Task(4, "打网球", "体育不能挂科", "2021/5/23", "2021/5/23 18:00:00", 40, 7, 2, "2021/6/6",
            "网球场", 0, 0, 1, 1, 0, 1, "18:00:00", "18:40:00", 0, 0, 0, 0, 0, 0, 0, 0);
    public Data.Task task5 = new Task(5, "健步走", "体育不能挂科", "2021/5/23", "2021/5/23 20:00:00", 30, 7, 2, "2021/6/6",
            "共青场", 0, 0, 1, 1, 0, 2, "20:00:00", "20:30:00", 0, 0, 0, 0, 0, 0, 0, 0);
    Data.Task taskExample = new Task();
    private static Context context;
    private RecyclerView rv_selector_branch;
    private static  TaskDialogMemberAdapter mSelectorBranchAdapter;
    private BaseFullBottomSheetFragment taskItemDialog;
    private View view;

    public FragTaskList(Context context) {
        super();
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_task_list, container, false);
    }

    private void InitViews(View view) {
        rv_selector_branch = (RecyclerView) view.findViewById(R.id.task_recycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_selector_branch.setLayoutManager(layoutManager);
        mSelectorBranchAdapter = new TaskDialogMemberAdapter(taskList, getContext(), false);
        mSelectorBranchAdapter.setOnItemClickListener(this);
        rv_selector_branch.setAdapter(mSelectorBranchAdapter);
        refreshTaskList();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        Log.d("aaa", "revc");
        InitViews(view);
    }

    @Override
    public void onItemClick(int position) {
        mPosition = position;
        taskItemDialog = new BaseFullBottomSheetFragment();
        taskItemDialog.setEditable(false);
        taskItemDialog.setOnce(false);
        taskItemDialog.setValues(taskList.get(position));
        taskItemDialog.show(getFragmentManager(), "taskItemDialog");
    }

    @Override
    public void UpdateViews(Data.Task task_edited) {
        Log.d("myActivity", task_edited.getTask_name());
        mSelectorBranchAdapter.UpdateItem(mPosition, task_edited);
        this.InitViews(view);

        // this.show();
    }

    public static void refreshTaskList() {
        //taskList = Arrays.asList(task1, task2, task3, task4, task5, task1, task2, task3, task4, task5);//获取任务
        taskList= GetData.attemptGetUserAllTask(context);
        if(taskList==null){
            myUtils.myToastHelper.showText(context,"系统异常 请重试", Toast.LENGTH_LONG);
            return;
        }
        myComparator_task cmp = new myComparator_task();
        Collections.sort(taskList, cmp);
        if(mSelectorBranchAdapter != null) {
            mSelectorBranchAdapter.setData(taskList);
        }
        Log.d("ref", "task");
    }

    static class myComparator_task implements Comparator {

        @Override
        public int compare(Object t1, Object t2) {
            Data.Task T1 = (Data.Task) t1;
            Data.Task T2 = (Data.Task) t2;
            if (T1.getCompleted() != T2.getCompleted()) {
                return T1.getCompleted() - T2.getCompleted();
            }
            return T1.getTask_name().compareTo(T2.getTask_name());
        }
    }
}
