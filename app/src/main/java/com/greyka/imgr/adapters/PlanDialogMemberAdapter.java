package com.greyka.imgr.adapters;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greyka.imgr.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.greyka.imgr.data.Data;
import com.greyka.imgr.data.Data.Task;

import java.util.List;

/**
 * Created by jie on 2018/9/9.
 */

public class PlanDialogMemberAdapter extends RecyclerView.Adapter<PlanDialogMemberAdapter.ViewHolder> {
    private List<Data.Plan> list;
    private OnItemClickListener mOnItemClickListener;
    private static ViewHolder holder;

    static public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView description;
        TextView start_date;
        LinearLayout item_selected;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.plan_name);
            description = itemView.findViewById(R.id.plan_desc);
            start_date =itemView.findViewById(R.id.plan_start);
            item_selected = itemView.findViewById(R.id.rl_branch_item_root);
        }
    }

    public PlanDialogMemberAdapter(List<Data.Plan> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_item,parent,false);
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int mPosition = position;
        Data.Plan lock = list.get(mPosition);
        holder.name.setText(lock.getPlan_name());
        holder.description.setText(lock.getPlan_description());
        holder.start_date.setText(lock.getPlan_date());

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

    public void UpdateItem(int pos, Data.Plan plan_edited){
        Log.d("myAdapter",plan_edited.getPlan_name());
        holder.name.setText(plan_edited.getPlan_name());
        String test=(String)holder.name.getText();
        Log.d("myAdapter",test);
        holder.description.setText(plan_edited.getPlan_description());
    }

    /**
     * 监听回调接口
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
