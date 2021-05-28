package com.greyka.imgr.fragments;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.greyka.imgr.R;
import com.greyka.imgr.activities.MainActivity;
import com.greyka.imgr.activities.Timer;
import com.greyka.imgr.classes.mottoManager;
import com.greyka.imgr.data.Data;
import com.greyka.imgr.data.Data.Task;
import com.greyka.imgr.dialogs.TaskListSelector;
import com.greyka.imgr.dialogs.TodayTaskDialog;
import com.greyka.imgr.utilities.myUtils;

import java.util.List;

public class FragHome extends Fragment {
    private TextView home_notice_board_title;
    private CardView timer;
    private CardView button2;
    private TextView total_complete_percent;
    private TextView motto;
    private ImageButton refresh;
    private TodayTaskDialog todayTaskDialog;
    private TaskListSelector taskListSelector;
    private List<Task> taskList=Task.taskList;
    private ImageView add_task;


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
        motto = (TextView)view.findViewById(R.id.string_motto);
        motto.setText(mottoManager.getRandomMotto());
        refresh = (ImageButton)view.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshHomeData();
            }
        });
        home_notice_board_title = view.findViewById(R.id.home_notice_board_title);
        home_notice_board_title.setText(myUtils.myCalenderHelper.getChineseTotal());
        timer = view.findViewById(R.id.home_timer_button);
        timer.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), Timer.class);
            startActivity(intent);
        });
        total_complete_percent = view.findViewById(R.id.total_complete_percent);
        total_complete_percent.setText("0%");
        total_complete_percent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity","click");
             showSelectorDialog();


            }
        });
        add_task = view.findViewById(R.id.home_add);

        add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity","clickAdd");
                new BaseFullBottomSheetFragment().show(getFragmentManager(), "dialog");


            }
        });
        /*button2 = view.findViewById(R.id.home_button2);
        button2.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MapDisplayActivity.class);
            startActivity(intent);
        });*/
    }
    void refreshHomeData(){
        motto.setText(mottoManager.getRandomMotto());
    }
}
