package com.greyka.imgr.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.greyka.imgr.R;
import com.greyka.imgr.data.Data;
import com.greyka.imgr.data.Data.Task;
import com.greyka.imgr.dialogs.CreatePlanDialog;
import com.greyka.imgr.dialogs.PlanDeleteDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jie on 2018/9/9.
 */

public class PlanDialogMemberAdapter extends RecyclerView.Adapter<PlanDialogMemberAdapter.ViewHolder> {
    private static ViewHolder holder;
    private List<Data.Plan> list;
    private OnItemClickListener mOnItemClickListener;
    private Context mContext;

    public PlanDialogMemberAdapter(List<Data.Plan> list, Context mContext) {
        list = new ArrayList<>(list);
        list.add(null);
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {

        return isLastItem(position) ? 0 : 1;
    }

    private boolean isLastItem(int position) {
        return position == list.size() - 1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_add, parent, false);
        }
        holder = new ViewHolder(view, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!isLastItem(position)) {
            final int mPosition = position;
            Data.Plan lock = list.get(position);
            holder.name.setText(lock.getPlan_name());
            holder.description.setText(lock.getPlan_description());
            holder.start_date.setText(lock.getPlan_date());
            holder.item_selected.setOnClickListener(v -> mOnItemClickListener.onItemClick(position, getItemViewType(position)));
            holder.item_selected.setOnLongClickListener(v -> {
                PlanDeleteDialog dialog = new PlanDeleteDialog(list.get(position));
                dialog.setNoticDialogListener(this::deletePlan);
                dialog.show(((FragmentActivity) mContext).getSupportFragmentManager(), "dialog");
                return true;
            });
        } else {
            holder.item_selected.setOnClickListener(v -> {
                CreatePlanDialog cpd = new CreatePlanDialog(mContext, getUncompletedTaskList());
                cpd.show();
            });

        }
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

    public void UpdateItem(int pos, Data.Plan plan_edited) {
        Log.d("myAdapter", plan_edited.getPlan_name());
        holder.name.setText(plan_edited.getPlan_name());
        String test = (String) holder.name.getText();
        Log.d("myAdapter", test);
        holder.description.setText(plan_edited.getPlan_description());
    }

    private List<Data.Task> getUncompletedTaskList() {
        Data data = new Data();
        Task task1 = data.new Task(1, "打太极拳", "一日之计在于晨", "2021/5/23", "2021/5/23 06:00:00", 60, 2, 20, "2021/7/1",
                "长风公园", 0, 0, 1, 1, 0, 1, "06:00:00", "07:00:00", 0, 0, 0, 0, 0, 0, 0, 0);
        Task task2 = data.new Task(2, "UML", "太难了", "2021/5/10", "2021/5/10 10:00:00", 60, 7, 2, "2021/5/24",
                "田家炳", 0, 0, 1, 1, 0, 2, "10:00:00", "11:00:00", 0, 0, 0, 0, 0, 0, 0, 0);
        Task task3 = data.new Task(3, "数据库", "考太差了", "2021/4/9", "2021/4/9 14:00:00", 120, 7, 3, "2021/4/30",
                "图书馆", 0, 0, 1, 1, 0, 0, "14:00:00", "16:00:00", 0, 0, 0, 0, 0, 0, 0, 0);
        Task task4 = data.new Task(4, "打网球", "体育不能挂科", "2021/5/23", "2021/5/23 18:00:00", 40, 7, 2, "2021/6/6",
                "网球场", 0, 0, 1, 1, 0, 1, "18:00:00", "18:40:00", 0, 0, 0, 0, 0, 0, 0, 0);
        Task task5 = data.new Task(5, "健步走", "体育不能挂科", "2021/5/23", "2021/5/23 20:00:00", 30, 7, 2, "2021/6/6",
                "共青场", 0, 0, 1, 1, 0, 2, "20:00:00", "20:30:00", 0, 0, 0, 0, 0, 0, 0, 0);
        return Arrays.asList(task1, task2, task4, task5, task1, task2, task4, task5);
    }

    private void deletePlan(long plan_id) {

    }

    /**
     * 监听回调接口
     */
    public interface OnItemClickListener {
        void onItemClick(int position, int itemType);
    }

    static public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView description;
        TextView start_date;
        LinearLayout item_selected;

        public ViewHolder(View itemView, int itemType) {
            super(itemView);
            if (itemType == 1) {
                name = itemView.findViewById(R.id.plan_name);
                description = itemView.findViewById(R.id.plan_desc);
                start_date = itemView.findViewById(R.id.plan_start);
            }
            item_selected = itemView.findViewById(R.id.rl_branch_item_root);
        }
    }
}
