package com.greyka.imgr.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.greyka.imgr.R;
import com.greyka.imgr.adapters.TaskDialogMemberAdapter;
import com.greyka.imgr.data.Data;
import com.greyka.imgr.data.Data.Task;

import java.util.List;


public class TaskListSelector extends Dialog implements TaskDialogMemberAdapter.OnItemClickListener, ViewUpdator {
    private static int mPosition;
    Data data = new Data();
    Callback callback;
    private List<Task> taskList;    //选择列表的数据
    private Task task = new Task();
    private Context context;
    private TaskDialogMemberAdapter mSelectorBranchAdapter;
    private RecyclerView rv_selector_branch;
    //private TodayTaskDialog todayTaskDialog;
    //private TaskItemDialog taskItemDialog;
    private ImageView editTitle;
    private FragmentManager fm;

    public TaskListSelector(Context context, FragmentManager fm, List<Task> mSimpleListItemEntity) {
        super(context);
        this.fm = fm;
        this.context = context;
        this.taskList = mSimpleListItemEntity;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setData(List<Task> list) {
        this.taskList = list;
        mSelectorBranchAdapter.setData(this.taskList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.task_dialog);
        InitViews();
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void InitViews() {

        rv_selector_branch = findViewById(R.id.task_selector);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(getContext());
        rv_selector_branch.setLayoutManager(layoutmanager);
        mSelectorBranchAdapter = new TaskDialogMemberAdapter(taskList, getContext(), true);
        mSelectorBranchAdapter.setOnItemClickListener(this);
        rv_selector_branch.setAdapter(mSelectorBranchAdapter);
    }

    /**
     * adpter里面的checkbox监听接口
     *
     * @param position item的位置
     *                 改变元数据集的内容
     */
    @Override
    public void onItemClick(int position) {
        // todayTaskDialog = new TodayTaskDialog();
        // final FragmentActivity myActivity=(FragmentActivity) context;
        //todayTaskDialog.show(myActivity.getSupportFragmentManager(), "TaskDialog"); ;
        mPosition = position;
        BaseFullBottomSheetFragment taskItemDialog = new BaseFullBottomSheetFragment();
        //taskItemDialog.setCancelable(false);
        taskItemDialog.setEditable(false);
        taskItemDialog.setOnce(true);
        Log.d("here", "a");
        taskItemDialog.setValues(taskList.get(position));
        taskItemDialog.setCallback(new BaseFullBottomSheetFragment.Callback() {
            @Override
            public void callback() {
                taskList = callback.callback();
                mSelectorBranchAdapter.setData(taskList);
            }
        });
        taskItemDialog.show(fm, "aa");
    }

    @Override
    public void UpdateViews(Task task_edited) {
        Log.d("myActivity", task_edited.getTask_name());
        mSelectorBranchAdapter.UpdateItem(mPosition, task_edited);
        this.InitViews();
        // this.show();
    }

    public interface Callback {
        List<Task> callback();
    }
}
