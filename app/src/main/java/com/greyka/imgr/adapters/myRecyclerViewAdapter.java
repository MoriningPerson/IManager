package com.greyka.imgr.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.greyka.imgr.R;

import java.util.ArrayList;

public class myRecyclerViewAdapter extends RecyclerView.Adapter<myRecyclerViewAdapter.ViewHolder> {

    private final String[] taskTitleList;
    private final String[] taskTimeList;


    public myRecyclerViewAdapter(ArrayList<String> taskTitleList, ArrayList<String> taskTimeList) {
        this.taskTitleList = taskTitleList.toArray(new String[taskTitleList.size()]);
        this.taskTimeList = taskTimeList.toArray(new String[taskTimeList.size()]);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTaskTitle().setText(taskTitleList[position]);
        viewHolder.getTaskTime().setText(taskTimeList[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return taskTitleList.length;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView taskTitle;
        private final TextView taskTime;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            taskTitle = view.findViewById(R.id.taskTitle);
            taskTime = view.findViewById(R.id.taskTime);
        }

        public TextView getTaskTitle() {return taskTitle;}
        public TextView getTaskTime(){return taskTime;}
    }
}
