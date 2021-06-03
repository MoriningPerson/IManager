package com.greyka.imgr.fragments;

import static com.greyka.imgr.utilities.GetData.attemptRegister;
import static com.greyka.imgr.utilities.GetData.attemptLogin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.greyka.imgr.R;
import com.greyka.imgr.activities.MainActivity;
import com.greyka.imgr.activities.Timer;
import com.greyka.imgr.classes.mottoManager;
import com.greyka.imgr.data.Data;
import com.greyka.imgr.data.Data.Task;
import com.greyka.imgr.dialogs.BaseFullBottomSheetFragment;
import com.greyka.imgr.dialogs.TaskListSelector;
import com.greyka.imgr.dialogs.TodayTaskDialog;
import com.greyka.imgr.utilities.myUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FragHome extends Fragment {

    Data data = new Data();

    private Data.User user= data.new User(0, "StellaDing", "13462057288", "Drx123456");
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

    private List<Task> taskList = Arrays.asList(task1, task2, task3, task4, task5,task1, task2, task3, task4, task5);


    private List<Task> taskCompleted[] = new List[2];
    private Task NextTask;


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

    private ImageView logo;



    private ImageView todayTaskCompleted;
    private ImageView todayTaskUncompleted;
    private ImageView nextTask;
    private ActivityResultLauncher<Intent> activityResultLauncher;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        activityResultLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        Intent data = result.getData();
//                        Log.d("name", data.getStringExtra("name"));
//                    }
//                });
        return inflater.inflate(R.layout.frag_home, container, false);
    }

    private void showSelectorDialog(int completed) {
        taskListSelector = new TaskListSelector(getActivity(), getFragmentManager(), taskCompleted[completed]);
        //taskListSelector.setCancelable(true);
        taskListSelector.show();
    }

    private void showNextTask() {
        BaseFullBottomSheetFragment next = new BaseFullBottomSheetFragment();
        next.setOnce(true);
        next.setEditable(false);
        next.setValues(NextTask);
        next.show(getFragmentManager(), "nextTask");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        initViews();
        refreshHomeData();
    }

    private void bindViews(View view) {
        nextTask = view.findViewById(R.id.home_next);
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
        logo=(ImageView) view.findViewById(R.id.imageView2);

    }

    private void initViews() {
        nextTask.setOnClickListener(v -> showNextTask());
        refresh.setOnClickListener(v -> refreshHomeData());
        timer.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), Timer.class);
            startActivity(intent);
        });
        todayTaskCompleted.setOnClickListener(v -> showSelectorDialog(1));
        todayTaskUncompleted.setOnClickListener(v -> showSelectorDialog(0));
        add_task.setOnClickListener(v -> {
            Log.d("MainActivity", "clickAdd");
            BaseFullBottomSheetFragment bfbsf = new BaseFullBottomSheetFragment();
            bfbsf.show(getFragmentManager(), "dialog");
//            Intent intent = new Intent(getContext(), MapPoiSearch.class);
//            activityResultLauncher.launch(intent);
        });
//        logo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // Log.d("MainActivity","clickRegister");
//               // String result=attemptRegister(user);
//               // Log.d("Register",result);
//                Log.d("MainActivity","clickLogin");
//                String result=attemptLogin(getContext(),"StellaDing","Drx123456");
//                Log.d("Login",result);
//            }
//        });

    }

    void refreshHomeData() {
        refreshTaskList();
        int tc0 = taskCompleted[0].size();
        int tc1 = taskCompleted[1].size();
        int tot = tc0 + tc1;
        motto.setText(mottoManager.getRandomMotto());
        home_notice_board_title.setText(myUtils.myCalenderHelper.getChineseTotal());
        total_complete_percent.setText((tc1 * 100 / tot) + "%");//今日任务完成比例
        home_string_next.setText("下一任务");//or 正在执行
        home_string_next_tasktitle.setText(NextTask.getTask_name());//任务title
        task_time_location.setText(NextTask.getStart_time().substring(0, 5) + " ~ " + NextTask.getEnd_time().substring(0, 5) + "\n" + NextTask.getPlace_name());//任务开始时间&地点
        uncomplete_percent.setText(tc0 + "/" + tot);//未完成/总
        complete_percent.setText(tc1 + "/" + tot);//完成/总
    }

    private void refreshTaskList() {
        refreshTaskList(0);
        refreshTaskList(1);
        refreshNextTask();
    }

    private void refreshTaskList(int completed) {
        taskCompleted[completed] = Arrays.asList(task1, task2, task4, task5, task1, task2, task4, task5);
        myComparator_task cmp = new myComparator_task();
        Collections.sort(taskCompleted[completed], cmp);
    }

    private void refreshNextTask() {
        NextTask = task1;
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

