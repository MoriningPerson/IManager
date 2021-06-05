package com.greyka.imgr.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.greyka.imgr.R;
import com.greyka.imgr.adapters.TaskDialogSelectAdapter;
import com.greyka.imgr.data.Data;
import com.greyka.imgr.data.Data.Task;
import com.greyka.imgr.utilities.GetData;
import com.greyka.imgr.utilities.myUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.greyka.imgr.utilities.Constants.ERROR_RESPONSE;
import static com.greyka.imgr.utilities.Constants.EXCEPTION;
import static com.greyka.imgr.utilities.Constants.NETWORK_UNAVAILABLE;
import static com.greyka.imgr.utilities.Constants.POSITIVE_RESPONSE;
import static com.greyka.imgr.utilities.Constants.UNKNOWN_RESPONSE;


public class CreatePlanDialog extends Dialog implements TaskDialogSelectAdapter.OnItemClickListener, ViewUpdator {

    private static int mPosition;
    Data data = new Data();
    private List<Task> taskList;    //选择列表的数据
    private ArrayList<Boolean> itemIsSelected = new ArrayList<>();
    private List<Long> itemSelected = new ArrayList<>();
    private String Title = "无标题";
    private String Description = "无描述";
    private Task task = new Task();
    private Context context;
    private TaskDialogSelectAdapter mSelectorBranchAdapter;
    private RecyclerView rv_selector_branch;
    //private TodayTaskDialog todayTaskDialog;
    //private TaskItemDialog taskItemDialog;
    private EditText title;
    private EditText description;
    private Button submit;

    public CreatePlanDialog(Context context, List<Task> mSimpleListItemEntity) {
        super(context);
        this.context = context;
        myComparator_task cmp = new myComparator_task();
        Collections.sort(mSimpleListItemEntity, cmp);
        this.taskList = mSimpleListItemEntity;
        for (int i = 0; i < this.taskList.size(); i++) {
            itemIsSelected.add(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_add_plan);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        bindViews();
        initViews();
    }

    private void bindViews() {
        title = findViewById(R.id.plan_title_add);
        description = findViewById(R.id.plan_desc_add);
        submit = findViewById(R.id.submit);
        rv_selector_branch = (RecyclerView) findViewById(R.id.task_selector);
    }

    private void initViews() {
        LinearLayoutManager layoutmanager = new LinearLayoutManager(getContext());
        rv_selector_branch.setLayoutManager(layoutmanager);
        mSelectorBranchAdapter = new TaskDialogSelectAdapter(taskList, getContext(), true);
        mSelectorBranchAdapter.setOnItemClickListener(this);
        rv_selector_branch.setAdapter(mSelectorBranchAdapter);
        submit.setOnClickListener(v -> {
            submitPlan();
            dismiss();
        });
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Title = s.toString();
            }
        });
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Description = s.toString();
            }
        });
    }

    /**
     * adpter里面的checkbox监听接口
     *
     * @param position item的位置
     *                 改变元数据集的内容
     */
    @Override
    public boolean onItemClick(int position) {
        // todayTaskDialog = new TodayTaskDialog();
        // final FragmentActivity myActivity=(FragmentActivity) context;
        //todayTaskDialog.show(myActivity.getSupportFragmentManager(), "TaskDialog"); ;
        mPosition = position;
        Boolean temp = !itemIsSelected.get(position);
        itemIsSelected.set(position, temp);
        if (temp) {
            itemSelected.add(taskList.get(position).getTask_id());
        } else {
            itemSelected.remove(taskList.get(position).getTask_id());
        }
        return temp;

    }

    @Override
    public void UpdateViews(Task task_edited) {
        Log.d("myActivity", task_edited.getTask_name());
        mSelectorBranchAdapter.UpdateItem(mPosition, task_edited);
        this.initViews();
        // this.show();
    }

    private void submitPlan() {
        Data.Plan plan = new Data.Plan();
        plan.setPlan_name(Title);
        plan.setPlan_description(Description);
        long result1 = GetData.attemptCreatePlan(context, plan);
        long plan_id;
        if (result1 == NETWORK_UNAVAILABLE) {
            myUtils.myToastHelper.showText(context, "无法连接服务器 请检查网络", Toast.LENGTH_LONG);
            return;
        } else if (result1 == UNKNOWN_RESPONSE) {
            myUtils.myToastHelper.showText(context, "未知错误 请重试", Toast.LENGTH_LONG);
            return;
        } else if (result1 == EXCEPTION) {
            myUtils.myToastHelper.showText(context, "出现异常 请重试", Toast.LENGTH_LONG);
            return;
        } else if (result1 == ERROR_RESPONSE) {
            myUtils.myToastHelper.showText(context, "系统异常 请重试", Toast.LENGTH_LONG);
            return;
        } else {
            plan_id = result1;
        }
        for (int i = 0; i < itemSelected.size(); i++) {
            int result2 = GetData.attemptAddTaskToPlan(context, plan_id, itemSelected.get(i));
            if (result2 == POSITIVE_RESPONSE) {
                myUtils.myToastHelper.showText(context, "创建成功", Toast.LENGTH_LONG);
            } else if (result2 == NETWORK_UNAVAILABLE) {
                myUtils.myToastHelper.showText(context, "无法连接服务器 请检查网络", Toast.LENGTH_LONG);
                return;
            } else if (result2 == UNKNOWN_RESPONSE) {
                myUtils.myToastHelper.showText(context, "未知错误 请重试", Toast.LENGTH_LONG);
                return;
            } else if (result2 == EXCEPTION) {
                myUtils.myToastHelper.showText(context, "出现异常 请重试", Toast.LENGTH_LONG);
                return;
            } else if (result2 == ERROR_RESPONSE) {
                myUtils.myToastHelper.showText(context, "系统异常 请重试", Toast.LENGTH_LONG);
                return;
            }
        }
    }

    class myComparator_task implements Comparator {

        @Override
        public int compare(Object t1, Object t2) {
            Data.Task T1 = (Data.Task) t1;
            Data.Task T2 = (Data.Task) t2;
            return T1.getTask_name().compareTo(T2.getTask_name());
        }
    }


}
