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
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
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

public class AddRecyclePickerDialog extends DialogFragment {

    private Vibrator vb;
    private myUtils.beeper scrollerBeep;
    private int recycleType = 0;
    private int cycle = 0;
    private int lastDaySelected = 1;
    private boolean[] dayOfWeek = new boolean[]{false,false,false,false,false,false,false};

    private Button submit;
    private PickerView cyclePicker;
    private RadioButton cycleSingle;
    private RadioButton cycleDay;
    private RadioButton cycleWeek;
    private TextView[] daySelector = new TextView[7];

    Callback mCallback;
    interface Callback{
        void getCycleSelected(int recycleType, int cycle, boolean[] dayOfWeak);
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
    // Use this instance of the interface to deliver action events
    @Override
    public void onResume() {
        super.onResume();
        myUtils.myDensityHelper myDensityHelper = new myUtils.myDensityHelper(getContext());
        getDialog().getWindow().setLayout(myDensityHelper.dp2px(380),myDensityHelper.dp2px(200));
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_recycle_picker, null);
        vb = (Vibrator)getActivity().getSystemService(Service.VIBRATOR_SERVICE);
        scrollerBeep = new myUtils.beeper(getContext());
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(view);
        bindViews(view);
        initViews();
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        // Create the AlertDialog object and return it
        return dialog;
    }
    @SuppressLint("DefaultLocale")
    private void bindViews(View view){
        daySelector[0] = view.findViewById(R.id.one);
        daySelector[1] = view.findViewById(R.id.two);
        daySelector[2] = view.findViewById(R.id.three);
        daySelector[3] = view.findViewById(R.id.four);
        daySelector[4] = view.findViewById(R.id.five);
        daySelector[5] = view.findViewById(R.id.six);
        daySelector[6] = view.findViewById(R.id.seven);
        cycleSingle = view.findViewById(R.id.cycle_single);
        cycleDay = view.findViewById(R.id.cycle_day);
        cycleWeek = view.findViewById(R.id.cycle_week);
        cyclePicker = view.findViewById(R.id.recycle_picker_cycle_add);
        submit = view.findViewById(R.id.submit_add_recycle_picker);
    }
    @SuppressLint("DefaultLocale")
    private void initViews(){
        for(int i = 0; i < 7; i++){
            Log.d("init","inti");
            daySelector[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("aaa","aaaa");
                    performSelect(v);
                }
            });
        }
        cycleSingle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    recycleType = 0;
                    cyclePicker.setEnabled(false);
                    clearDaySelected();
                }
            }
        });
        cycleSingle.setChecked(true);
        cycleDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    recycleType = 1;
                    clearDaySelected();
                    setDayChecked(0, true);
                    if(!cyclePicker.isEnabled()) {
                        cyclePicker.setEnabled(true);
                        Log.d("click","day");
                    }
                }
            }
        });
        cycleWeek.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    recycleType = 2;
                    clearDaySelected();
                    setDayChecked(0, true);
                    if(!cyclePicker.isEnabled()) {
                        cyclePicker.setEnabled(true);
                    }
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.getCycleSelected(recycleType,cycle,dayOfWeek);
                dismiss();
            }
        });
        ArrayList<String> mCycle = new ArrayList<>();
        for(int i = 2;i < 30; i++){
            mCycle.add(String.format("%02d",i));
        }
        cyclePicker.setData(mCycle);
        cyclePicker.setSelected("02");
        cycle = 2;
        cyclePicker.setEnabled(false);
        cyclePicker.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                cycle = Integer.parseInt(text);
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelectedChange() {
                scrollerBeep.play(1);
                vb.vibrate(VibrationEffect.createOneShot(25,75));
            }
        });
    }
    public void setCallback(Callback callback){
        mCallback = callback;
    }
    private void performSelect(View v){
        Log.d("p","se");
        if(recycleType == 1)
        {
            for(int i = 0; i < 6; i ++){
                if(v == daySelector[i]){
                    changeDayChecked(i);
                    Log.d("click","checked"+i);
                    return;
                }
            }
        }else if(recycleType == 2){
            for(int i = 0; i < 7; i ++){
                if(v == daySelector[i]){
                    if(dayOfWeek[i] && getDayCheckCnt() > 1){
                        setDayChecked(i, false);
                    }else if(!dayOfWeek[i]){
                        setDayChecked(i, true);
                    }
                }
            }
        }
    }
    private void changeDayChecked(int id){
        setDayChecked(lastDaySelected,false);
        setDayChecked(id, true);
    }
    private void setDayChecked(int id, boolean checked){
        dayOfWeek[id] = checked;
        if(checked){
            lastDaySelected = id;
            daySelector[id].setTextColor(getActivity().getColor(R.color.white));
            daySelector[id].setBackgroundColor(getActivity().getColor(R.color.myThemeShallow));
        }else{
            daySelector[id].setTextColor(getActivity().getColor(R.color.dimgrey));
            daySelector[id].setBackgroundColor(getActivity().getColor(R.color.transparent));
        }
    }
    private void clearDaySelected(){
        for(int i = 0; i < 7; i++){
            setDayChecked(i, false);
        }
    }
    private int getDayCheckCnt(){
        int cnt = 0;
        for(int i = 0; i < 7; i++){
            if(dayOfWeek[i]) cnt++;
        }
        return cnt;
    }
}
