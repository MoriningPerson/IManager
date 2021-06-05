package com.greyka.imgr.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.greyka.imgr.data.Data;

public class PlanDeleteDialog extends DialogFragment {

    // Use this instance of the interface to deliver action events
    PlanDeleteDialog.NoticeDialogListener listener;

    private Data.Plan plan;

    public PlanDeleteDialog(Data.Plan plan) {
        this.plan = plan;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d("jsi","iqodj");
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle("删除提示")
                .setMessage("是否删除任务：" + plan.getPlan_name() + " ?")
                .setPositiveButton("确定", (dialog, id) -> {
                    // Send the positive button event back to the host activity
                    listener.onDialogPositiveClick(plan.getPlan_id());
                })
                .setNegativeButton("取消", (dialog, id) -> {
                });
        // Create the AlertDialog object and return it
        Log.d("jsi", String.valueOf(plan.getPlan_id()));
        return builder.create();
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public void setNoticDialogListener(NoticeDialogListener listener) {
        this.listener = listener;
    }

    public interface NoticeDialogListener {
        void onDialogPositiveClick(long plan_id);
    }

}