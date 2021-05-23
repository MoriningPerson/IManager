package com.greyka.imgr.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greyka.imgr.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.greyka.imgr.data.Data.Task;

import java.util.List;

/**
 * Created by jie on 2018/9/9.
 */

public class DialogMemberAdapter extends RecyclerView.Adapter<DialogMemberAdapter.ViewHolder> {
    private List<Task> list;
    private OnItemClickListener mOnItemClickListener;

    static public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView description;
        TextView status;
        LinearLayout item_selected;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.task_name);
            description = itemView.findViewById(R.id.task_desc);
            status =itemView.findViewById(R.id.task_status);
            item_selected = itemView.findViewById(R.id.rl_branch_item_root);
        }
    }

    public DialogMemberAdapter(List<Task> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int mPosition = position;
        Task lock = list.get(mPosition);
        holder.name.setText(lock.getTask_name());
        holder.description.setText(lock.getTask_description());
        int task_status=lock.getCompleted();
        String status_string;
        if(task_status==0)
        status_string="已完成";
        else if (task_status==1)
            status_string="未完成";
        else status_string="完成失败";
        holder.status.setText(status_string);

        holder.item_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mOnItemClickListener.onItemClick(mPosition);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * @param onItemClickListener 监听设置
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * 监听回调接口
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
