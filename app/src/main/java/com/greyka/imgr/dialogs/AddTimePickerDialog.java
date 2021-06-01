package com.greyka.imgr.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Service;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.greyka.imgr.R;
import com.greyka.imgr.classes.PickerView;
import com.greyka.imgr.utilities.myUtils;

import java.util.ArrayList;

public class AddTimePickerDialog extends DialogFragment {

    private boolean editable = true;

    private void setEditable(boolean editable){
        this.editable = editable;
    }
    private void setValues(){
        //need implementation
    }
    @SuppressLint("DefaultLocale")
    private void setStaticPage(){
        startHourPicker.setEnabled(false);
        startMinutePicker.setEnabled(false);
        lengthMinutePicker.setEnabled(false);
        lengthHourPicker.setEnabled(false);
        startMinutePicker.setSelected(String.format("%02d",startMinute));
        startHourPicker.setSelected(String.format("%02d",startHour));
        lengthMinutePicker.setSelected(String.format("%02d",lengthMinute));
        lengthHourPicker.setSelected(String.format("%02d",lengthHour));
    }

    private Vibrator vb;
    private myUtils.beeper scrollerBeep;
    private PickerView startHourPicker;
    private PickerView startMinutePicker;
    private PickerView lengthHourPicker;
    private PickerView lengthMinutePicker;
    private Button submit;

    private int startHour;
    private int startMinute;
    private int lengthHour;
    private int lengthMinute;


    Callback mCallback;
    interface Callback{
        void getTimeSelected(int hour,int minute,int lhour,int lminute);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("oncreateview","vrea");
        View view =inflater.inflate(R.layout.timer_picker, container, false);
        return view;
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */

    @Override
    public void onResume() {
        super.onResume();
        myUtils.myDensityHelper myDensityHelper = new myUtils.myDensityHelper(getContext());
        getDialog().getWindow().setLayout(myDensityHelper.dp2px(350),myDensityHelper.dp2px(200));
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_time_picker, null);
        vb = (Vibrator)getActivity().getSystemService(Service.VIBRATOR_SERVICE);
        scrollerBeep = new myUtils.beeper(getContext());
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(view);
        bindViews(view);
        initViews();
        if(!editable){
            setStaticPage();
        }
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        // Create the AlertDialog object and return it
        return dialog;
    }
    @SuppressLint("DefaultLocale")
    private void bindViews(View view){
        submit = view.findViewById(R.id.submit_add_time_picker);
        startHourPicker = view.findViewById(R.id.time_picker_hour_add);
        startMinutePicker = view.findViewById(R.id.time_picker_minute_add);
        lengthHourPicker = view.findViewById(R.id.time_picker_hour2_add);
        lengthMinutePicker = view.findViewById(R.id.time_picker_minute2_add);
    }
    @SuppressLint("DefaultLocale")
    private void initViews(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.getTimeSelected(startHour,startMinute,lengthHour,lengthMinute);
                dismiss();
            }
        });
        startMinutePicker.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                startMinute = Integer.parseInt(text);
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelectedChange() {
                scrollerBeep.play(1);
                vb.vibrate(VibrationEffect.createOneShot(25,75));
            }
        });
        startHourPicker.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                startHour = Integer.parseInt(text);
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelectedChange() {
                scrollerBeep.play(1);
                vb.vibrate(VibrationEffect.createOneShot(25,75));
            }
        });
        lengthMinutePicker.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                lengthMinute = Integer.parseInt(text);
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelectedChange() {
                scrollerBeep.play(1);
                vb.vibrate(VibrationEffect.createOneShot(25,75));
            }
        });
        lengthHourPicker.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                lengthHour = Integer.parseInt(text);
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelectedChange() {
                scrollerBeep.play(1);
                vb.vibrate(VibrationEffect.createOneShot(25,75));
            }
        });
        ArrayList<String> sHour = new ArrayList<>();
        ArrayList<String> sMinute = new ArrayList<>();
        ArrayList<String> lHour = new ArrayList<>();
        ArrayList<String> lMinute = new ArrayList<>();
        for(int i=0;i<24;i++){
            sHour.add(String.format("%02d",i));
            lHour.add(String.format("%02d",i));
        }
        for(int i=0;i<60;i++){
            sMinute.add(String.format("%02d",i));
            lMinute.add(String.format("%02d",i));
        }
        startHourPicker.setData(sHour);
        startHourPicker.setSelected(0);
        lengthHourPicker.setData(lHour);
        lengthHourPicker.setSelected(0);
        startMinutePicker.setData(sMinute);
        startMinutePicker.setSelected(0);
        lengthMinutePicker.setData(lMinute);
        lengthMinutePicker.setSelected(0);
    }
    public void setCallback(Callback callback){
        mCallback = callback;
    }
}
