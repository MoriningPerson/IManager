package com.greyka.imgr.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.greyka.imgr.R;
import com.greyka.imgr.data.Data.Task;

import java.util.List;

/**
 * Created by jie on 2018/9/9.
 */

public class TaskDialogMemberAdapter extends RecyclerView.Adapter<TaskDialogMemberAdapter.ViewHolder> {
    private static ViewHolder holder;
    private final Context mcontext;
    private List<Task> list;
    private OnItemClickListener mOnItemClickListener;
    private boolean once;

    public TaskDialogMemberAdapter(List<Task> list, Context mcontext, boolean once) {
        this.once = once;
        this.list = list;
        this.mcontext = mcontext;
    }
    public void setData(List<Task> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int mPosition = position;
        Task lock = list.get(mPosition);
        holder.name.setText(lock.getTask_name());
        holder.description.setText(lock.getTask_description());
        int task_status;
        if (once) {
            task_status = lock.getTodayCompleted();
        } else {
            task_status = lock.getCompleted();
        }
        if (task_status == 0) {
            holder.status.setImageResource(R.drawable.ic_task_uncompleted);
            holder.status.setColorFilter(mcontext.getColor(R.color.myThemeShallow));
            holder.colorBar.setCardBackgroundColor(mcontext.getColor(R.color.myThemeShallow));
            holder.name.setTextColor(mcontext.getColor(R.color.black));
            holder.description.setTextColor(mcontext.getColor(R.color.dimgrey));
        } else if (task_status == 1) {
            holder.status.setImageResource(R.drawable.ic_task_failed);
            holder.status.setColorFilter(mcontext.getColor(R.color.dimgrey));
            holder.colorBar.setCardBackgroundColor(mcontext.getColor(R.color.defaultgrey));
            holder.name.setTextColor(mcontext.getColor(R.color.defaultgrey));
            holder.description.setTextColor(mcontext.getColor(R.color.defaultgrey));
        } else {
            holder.status.setImageResource(R.drawable.ic_task_completed);
            holder.status.setColorFilter(mcontext.getColor(R.color.grey));
            holder.colorBar.setCardBackgroundColor(mcontext.getColor(R.color.grey));
            holder.name.setTextColor(mcontext.getColor(R.color.grey));
            holder.description.setTextColor(mcontext.getColor(R.color.grey));
        }

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

    public void UpdateItem(int pos, Task task_edited) {
        Log.d("myAdapter", task_edited.getTask_name());
        holder.name.setText(task_edited.getTask_name());
        String test = (String) holder.name.getText();
        Log.d("myAdapter", test);
        holder.description.setText(task_edited.getTask_description());
    }

    /**
     * 监听回调接口
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    static public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView description;
        ImageView status;
        CardView colorBar;
        LinearLayout item_selected;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.task_name);
            description = itemView.findViewById(R.id.task_desc);
            status = itemView.findViewById(R.id.task_status);
            item_selected = itemView.findViewById(R.id.rl_branch_item_root);
            colorBar = itemView.findViewById(R.id.color_bar);
        }
    }
}
