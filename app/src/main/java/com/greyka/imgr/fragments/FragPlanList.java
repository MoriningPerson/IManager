package com.greyka.imgr.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.greyka.imgr.R;
import com.greyka.imgr.adapters.PlanDialogMemberAdapter;
import com.greyka.imgr.adapters.TaskDialogMemberAdapter;
import com.greyka.imgr.data.Data;
import com.greyka.imgr.dialogs.TaskItemDialog;
import com.greyka.imgr.dialogs.TodayTaskDialog;

import java.util.List;

public class FragPlanList extends Fragment implements PlanDialogMemberAdapter.OnItemClickListener{
    public static final String ARG_OBJECT = "object";
    private Context context;
    private List<Data.Plan> planList;
    private RecyclerView rv_selector_branch;
    private PlanDialogMemberAdapter mSelectorBranchAdapter;
    private static int mPosition;
    private View view;

    public FragPlanList(Context context, List<Data.Plan> mSimpleListItemEntity) {
        super();
        this.context = context;
        this.planList = mSimpleListItemEntity;

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_plan_list, container, false);
    }

    private void InitViews(View view) {

        rv_selector_branch = (RecyclerView)view.findViewById(R.id.plan_recycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_selector_branch.setLayoutManager(layoutManager);
        mSelectorBranchAdapter = new PlanDialogMemberAdapter(planList);
        mSelectorBranchAdapter.setOnItemClickListener(this);
        rv_selector_branch.setAdapter(mSelectorBranchAdapter);
    }






    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view=view;
        InitViews(view);
    }

    @Override
    public void onItemClick(int position) {
        mPosition=position;
        TodayTaskDialog demo = new TodayTaskDialog();
        demo.show(getActivity().getSupportFragmentManager(), null);
    }


}
