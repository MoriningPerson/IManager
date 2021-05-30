package com.greyka.imgr.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.greyka.imgr.R;
import com.greyka.imgr.data.Data;
import com.greyka.imgr.data.Data.Task;

public class TaskItemDialog extends Dialog {
    Data data= new Data();
    private Task task = data.new Task();
    private Context context;
    private TextView title;
    private TextView description;
    private TextView create_date;
    private ImageView editTitle;
    private EditText editText;
    private ViewUpdator dialog;
    private TextView submit;

    public TaskItemDialog(@NonNull ViewUpdator dialog, Context context, Task task){
        super(context);
        this.dialog=dialog;
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
    public void InitViews() {

        title =  findViewById(R.id.my_title);
        title.setText(task.getTask_name());
        description= findViewById(R.id.my_desc);
        description.setText(task.getTask_description());
        create_date= findViewById(R.id.my_create_date);
        create_date.setText(task.getCreate_date());
        submit=findViewById(R.id.submit);
        submit.setOnClickListener(v -> {
            this.dismiss();
        });
        EditTitle();
    }
    public void UpdateViews(){
        title.setText(task.getTask_name());
        description.setText(task.getTask_description());
        create_date.setText(task.getCreate_date());

    }
    public void EditTitle(){
        editTitle = (ImageView) findViewById(R.id.title_edit);
        editTitle.setOnClickListener(v -> {
        showInputDialog("修改任务标题");
        });
    }
       private void showInputDialog(String editTarget) {

        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(context);
           final View dialogView = LayoutInflater.from(context)
                   .inflate(R.layout.task_edit_dialog,null);
           ViewUpdator mDialog = this.dialog;
           inputDialog.setTitle(editTarget);
           inputDialog.setView(dialogView);
           EditText edit_text =
                   (EditText) dialogView.findViewById(R.id.edit_text);
           edit_text.setHint(task.getTask_name());
           inputDialog.setPositiveButton("确定",
                   new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           // 获取EditView中的输入内容
                           task.setTask_name(edit_text.getText().toString());
                           UpdateViews();

                           mDialog.UpdateViews(task);

                       }
                   });
           inputDialog.show();
    }


}
