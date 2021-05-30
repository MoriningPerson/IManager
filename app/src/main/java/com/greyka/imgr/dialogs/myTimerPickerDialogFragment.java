package com.greyka.imgr.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Service;
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

public class myTimerPickerDialogFragment extends DialogFragment {

    private Vibrator vb;
    private myUtils.beeper scrollerBeep;
    private PickerView hourPicker;
    private PickerView minutePicker;
    private PickerView secondPicker;
    private Button pickerCancel;
    private Button pickerSubmit;
    private int hour = 0;
    private int minute = 0;
    private int second = 0;
    private boolean lockEnabled = false;
    private int lockPercent = 0;
    private Switch lockerSwitch;
    private SeekBar lockPercentSeekBar;
    private TextView lockPercentText;
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
    public interface DialogListener {
        void onPositiveClick(int hour,int minute,int second,int lockPercent, boolean lockEnabled);
    }
    // Use this instance of the interface to deliver action events
    DialogListener listener;
    public void setDialogListener(DialogListener listener){
        this.listener = listener;
    }
    @Override
    public void onResume() {
        super.onResume();
        myUtils.myDensityHelper myDensityHelper = new myUtils.myDensityHelper(getContext());
        getDialog().getWindow().setLayout(myDensityHelper.dp2px(330),myDensityHelper.dp2px(315));
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.timer_picker, null);
        vb = (Vibrator)getActivity().getSystemService(Service.VIBRATOR_SERVICE);
        scrollerBeep = new myUtils.beeper(getContext());
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        builder.setView(view);
        initView(view);
        initPickers();
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        setCancelable(false);
        // Create the AlertDialog object and return it
        return dialog;
    }
    @SuppressLint("DefaultLocale")
    void initPickers(){
        hourPicker.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String value) {
                hour = Integer.parseInt(value);
            }
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelectedChange() {
                scrollerBeep.play(1);
                vb.vibrate(VibrationEffect.createOneShot(25,75));
            }
        });
        minutePicker.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String value) {
                minute = Integer.parseInt(value);
            }
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelectedChange() {
                scrollerBeep.play(1);
                vb.vibrate(VibrationEffect.createOneShot(25,75));
            }
        });
        secondPicker.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String value) {
                second = Integer.parseInt(value);
            }
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelectedChange() {
                scrollerBeep.play(1);
                vb.vibrate(VibrationEffect.createOneShot(25,75));
            }
        });
        ArrayList<String> mDataHour = new ArrayList<>();
        for(int value = 0; value <=23; value ++ ){
            mDataHour.add(String.format("%02d",value));
        }
        hourPicker.setData(mDataHour);
        hourPicker.setSelected(0);
        ArrayList<String> mDataMinute = new ArrayList<>();
        ArrayList<String> mDataSecond = new ArrayList<>();
        for(int value = 0; value <= 59; value ++){
            mDataMinute.add(String.format("%02d",value));
            mDataSecond.add(String.format("%02d",value));
        }
        secondPicker.setData(mDataSecond);
        secondPicker.setSelected(0);
        minutePicker.setData(mDataMinute);
        minutePicker.setSelected(0);
    }
    void initView(View view){
        lockPercentText = view.findViewById(R.id.lock_percnet_now);
        lockPercentSeekBar = view.findViewById(R.id.lock_percent);
        lockerSwitch = view.findViewById(R.id.lock_option);
        hourPicker = view.findViewById(R.id.timerpickerview_hour);
        minutePicker = view.findViewById(R.id.timerpickerview_minute);
        secondPicker = view.findViewById(R.id.timerpickerview_second);
        pickerSubmit = view.findViewById(R.id.timerpicker_submit);
        pickerCancel = view.findViewById(R.id.timerpicker_cancel);
        pickerSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onPositiveClick(hour, minute, second, lockPercent, lockEnabled);
                }
                dismiss();
            }
        });
        pickerCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        lockerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    lockPercentSeekBar.setEnabled(true);
                    lockEnabled = true;
                }
                else{
                    lockPercentSeekBar.setProgress(0);
                    lockPercentSeekBar.setEnabled(false);
                    lockEnabled = false;
                }
            }
        });
        lockPercentSeekBar.setEnabled(false);
        lockPercentSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lockPercent = progress / 10 * 10;
                lockPercentText.setText(lockPercent + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
