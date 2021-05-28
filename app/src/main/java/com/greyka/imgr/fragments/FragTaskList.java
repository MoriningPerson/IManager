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
import com.greyka.imgr.adapters.TaskDialogMemberAdapter;
import com.greyka.imgr.data.Data;
import com.greyka.imgr.dialogs.TaskItemDialog;
import com.greyka.imgr.dialogs.ViewUpdator;

import java.util.List;

public class FragTaskList extends Fragment implements TaskDialogMemberAdapter.OnItemClickListener, ViewUpdator {
    public static final String ARG_OBJECT = "object";
    private Context context;
    private List<Data.Task> taskList;
    private RecyclerView rv_selector_branch;
    private TaskDialogMemberAdapter mSelectorBranchAdapter;
    private static int mPosition;
    private TaskItemDialog taskItemDialog;
    private View view;

    public FragTaskList(Context context, List<Data.Task> mSimpleListItemEntity) {
        super();
        this.context = context;
        this.taskList = mSimpleListItemEntity;

    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_task_list, container, false);

    }

    private void InitViews(View view) {

        rv_selector_branch = (RecyclerView)view.findViewById(R.id.task_recycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_selector_branch.setLayoutManager(layoutManager);
        mSelectorBranchAdapter = new TaskDialogMemberAdapter(taskList,getContext());
        mSelectorBranchAdapter.setOnItemClickListener(this);
        rv_selector_branch.setAdapter(mSelectorBranchAdapter);
    }






    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view=view;
        InitViews(view);
    }

    @Override
    public void onItemClick(int position) {
        mPosition=position;
        taskItemDialog = new TaskItemDialog(this,context,taskList.get(position));
        taskItemDialog.setCancelable(false);
        taskItemDialog.show();
        Activity myActivity=(Activity)context;
        LayoutInflater inflater = LayoutInflater.from(myActivity);
        View viewDialog = inflater.inflate(R.layout.task_info, null);

        Display display = myActivity.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
//设置dialog的宽高为屏幕的宽高
        ViewGroup.LayoutParams layoutParams = new  ViewGroup.LayoutParams(width, height);
        taskItemDialog.setContentView(viewDialog, layoutParams);
        taskItemDialog.InitViews();
    }

    @Override
    public void UpdateViews(Data.Task task_edited){
        Log.d("myActivity",task_edited.getTask_name());
        mSelectorBranchAdapter.UpdateItem(mPosition,task_edited);
        this.InitViews(view);
        // this.show();
    }
}
