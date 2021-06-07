package com.greyka.imgr.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.greyka.imgr.R;
import com.greyka.imgr.data.Data;

import java.util.ArrayList;
import java.util.List;

public class myRecyclerViewAdapter extends RecyclerView.Adapter<myRecyclerViewAdapter.ViewHolder> {

    private final String[] taskTitleList;
    private final String[] taskTimeList;

    public myRecyclerViewAdapter(List<Data.Task> list) {
        ArrayList<String> title = new ArrayList<>();
        ArrayList<String> time = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            title.add(list.get(i).getTask_name());
            time.add(list.get(i).getStart_time().substring(0,5) + " ~ " + list.get(i).getEnd_time().substring(0,5));
        }
        this.taskTitleList = title.toArray(new String[title.size()]);
        this.taskTimeList = time.toArray(new String[time.size()]);
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

        public TextView getTaskTitle() {
            return taskTitle;
        }

        public TextView getTaskTime() {
            return taskTime;
        }
    }
}
