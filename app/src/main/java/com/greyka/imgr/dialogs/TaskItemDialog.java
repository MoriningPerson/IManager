package com.greyka.imgr.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.greyka.imgr.R;
import com.greyka.imgr.adapters.DialogMemberAdapter;
import com.greyka.imgr.data.Data.Task;
import com.greyka.imgr.utilities.myUtils;

public class TaskItemDialog extends Dialog {
    private Task task = new Task();
    private Context context;
    private TextView title;
    private TextView description;
    private TextView create_date;

    public TaskItemDialog(@NonNull Context context, Task task){
        super(context);
        this.context = context;
        this.task = task;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.task_info);
        this.setCanceledOnTouchOutside(true); // 点击外部会消失
        InitViews();
    }
    private void InitViews() {

        title =  findViewById(R.id.my_title);
        title.setText(task.getTask_name());
        description= findViewById(R.id.my_desc);
        description.setText(task.getTask_description());
        create_date= findViewById(R.id.my_create_date);
        create_date.setText(task.getCreate_date());
    }


}
