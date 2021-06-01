package com.greyka.imgr.dialogs;

import com.greyka.imgr.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.greyka.imgr.data.Data;
import com.greyka.imgr.dialogs.CalendarDialog;
import com.greyka.imgr.dialogs.TaskListSelector;
import com.greyka.imgr.utilities.myUtils;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BaseFullBottomSheetFragment extends BottomSheetDialogFragment {

    private int completed;
    private long taskId;
    private boolean editable = true;
    private boolean once = false;
    public void setEditable(boolean editable){
        this.editable = editable;
    }
    public void setOnce(boolean once){
        this.once = once;
    }
    public void setValues(Data.Task task)  {
        taskId = task.getTask_id();
        completed = task.getCompleted();
        String str=task.getStart_date();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        try{
            date =sdf.parse(str);
        }catch (ParseException e){}
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Year = c.get(Calendar.YEAR);
        Month = c.get(Calendar.MONTH) + 1;
        Day = c.get(Calendar.DAY_OF_MONTH);
        startHour = c.get(Calendar.HOUR_OF_DAY);
        startMinute = c.get(Calendar.MINUTE);
        Signup=(task.getCompleted()>0);
        if(task.getRemind()==0){
            Alarm=false;
            StrongAlarm=false;
        }else if(task.getRemind()==1){
            Alarm=true;
            StrongAlarm=false;
        }else if(task.getRemind()==2){
            Alarm=true;
            StrongAlarm=false;
        }
        if(task.getAllowed()==100)
            lockEnabled=false;
        else{
            lockEnabled=true;
            lockPercent=task.getAllowed();
        }
        Title=task.getTask_name();
        Description=task.getTask_description();
        lenHour=task.getDuration()/60;
        lenMinute=task.getDuration()%60;
        RecycleType=task.getCycleType();
        Cycle=task.getRepeat_count();
        DayOfWeek[0]=((task.getSelected() & (1 << 6)) > 0);
        DayOfWeek[1]=((task.getSelected() & 1 ) > 0);
        DayOfWeek[2]=((task.getSelected() & (1 << 1)) > 0);
        DayOfWeek[3]=((task.getSelected() & (1 << 2)) > 0);
        DayOfWeek[4]=((task.getSelected() & (1 << 3)) > 0);
        DayOfWeek[5]=((task.getSelected() & (1 << 4)) > 0);
        DayOfWeek[6]=((task.getSelected() & (1 << 5)) > 0);
    }
    private void setStaticPage(){
        clickButton(rl_select,tv_select);
        date_selected.setText(Year + "年" + Month + "月" + Day + "日");
        refreshTimeInfo();
        refreshCycleInfo();
        signUp.setChecked(Signup);
        signUp.setEnabled(false);
        alarm.setChecked(Alarm);
        alarm.setEnabled(false);
        strongAlarm.setChecked(StrongAlarm);
        strongAlarm.setEnabled(false);
        lock.setChecked(lockEnabled);
        lock.setEnabled(false);
        lockPercentSeekBar.setProgress(lockPercent);
        lockPercentSeekBar.setEnabled(false);
        title.setHint((CharSequence)Title);
        title.setHintTextColor(getActivity().getColor(R.color.black));
        title.setEnabled(false);
        description.setHint((CharSequence)Description);
        description.setHintTextColor(getActivity().getColor(R.color.dimgrey));
        description.setEnabled(false);
        String submitInfo[][] = new String[][]{
                {"终止","删除","删除"},
                {"放弃","确定","确定"}
        };
        submit.setText(submitInfo[(once)?1:0][completed]);
    }
    private BottomSheetBehavior<FrameLayout> behavior;

    Data mdata = new Data();
    //private TextView mReBack;
    private Context mContext;
    private View view;
    private ImageView detail;
    private TextView tv_today;
    private TextView tv_tomorrow;
    private TextView tv_select;
    private RelativeLayout rl_today;
    private RelativeLayout rl_tomorrow;
    private RelativeLayout rl_select;
    private RelativeLayout timer_select;
    private RadioButton signUp;
    private RadioButton alarm;
    private Switch strongAlarm;
    private Switch lock;
    private SeekBar lockPercentSeekBar;
    private TextView lockPercentText;
    private EditText description;
    private EditText title;
    private Button submit;
    private int position = 1;
    private CalendarDialog calendarDialog;
    TextView date_selected;
    private TextView startTime;
    private TextView lengthTime;
    private ImageView addLocation;
    private ImageView ic_addRecycle;
    private TextView recycleInfo;
    private LinearLayout addRecycle;

    private int Year, Month, Day;
    private boolean Signup = false;
    private boolean Alarm = false;
    private boolean StrongAlarm = false;
    private boolean lockEnabled = false;
    private int lockPercent;
    private String Title = "无标题";
    private String Description = "无任务描述";
    private int startHour = 0;
    private int startMinute = 0;
    private int lenHour = 0;
    private int lenMinute = 1;
    private int RecycleType = 0;
    private int Cycle = 0;
    private boolean[] DayOfWeek = new boolean[]{false,false,false,false,false,false,false};

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //返回BottomSheetDialog的实例
        return new BottomSheetDialog(this.getContext(),R.style.BottomSheetStyle); //设置软键盘弹出时向上移动
    }
    private void initButton(RelativeLayout rl,TextView tv){
        if(rl == rl_select){
            detail.setColorFilter(getContext().getColor(R.color.dimgrey));
            date_selected.setText("");
            Year = myUtils.myCalenderHelper.getYear();
            Month = myUtils.myCalenderHelper.getMonth();
            Day = myUtils.myCalenderHelper.getDay();
        }
        GradientDrawable gradientDrawable1 = (GradientDrawable) rl.getBackground();
        gradientDrawable1 .setColor(getResources().getColor(R.color.grey91));
        tv.setTextColor(getResources().getColor(R.color.dimgrey));
    }
    private void clickButton(RelativeLayout rl,TextView tv){
        initButton(rl_today,tv_today);
        initButton(rl_tomorrow,tv_tomorrow);
        initButton(rl_select,tv_select);
        if(rl == rl_select){
            detail.setColorFilter(getContext().getColor(R.color.white));
            date_selected.setText(Year+"年"+Month+"月"+Day+"日");
        }
        if(rl == rl_today){
            Year = myUtils.myCalenderHelper.getYear();
            Month = myUtils.myCalenderHelper.getMonth();
            Day = myUtils.myCalenderHelper.getDay();
        }
        if(rl == rl_tomorrow){
            Year = myUtils.myCalenderHelper.getYearAfterDays(1);
            Month = myUtils.myCalenderHelper.getMonthAfterDays(1);
            Day = myUtils.myCalenderHelper.getDayAfterDays(1);
        }
        Log.d("click","bbbbbbbbbb");
        GradientDrawable gradientDrawable2 = (GradientDrawable) rl.getBackground();
        gradientDrawable2 .setColor(getResources().getColor(R.color.myThemeShallow));
        tv.setTextColor(getResources().getColor(R.color.white));
    }
    private void  init(){
        Year = myUtils.myCalenderHelper.getYear();
        Month = myUtils.myCalenderHelper.getMonth();
        Day = myUtils.myCalenderHelper.getDay();
        position=1;
        bindViews();
        initViews();
        if(!editable){
            setStaticPage();
        }else {
            clickButton(rl_today, tv_today);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        //把windowsd的默认背景颜色去掉，不然圆角显示不见
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //获取diglog的根部局
        FrameLayout bottomSheet = dialog.getDelegate().findViewById(R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            //获取根部局的LayoutParams对象
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomSheet.getLayoutParams();
            layoutParams.height = getPeekHeight();
            //修改弹窗的最大高度，不允许上滑（默认可以上滑）
            bottomSheet.setLayoutParams(layoutParams);

            behavior = BottomSheetBehavior.from(bottomSheet);
            //peekHeight即弹窗的最大高度
            behavior.setPeekHeight(getPeekHeight());
            // 初始为展开状态
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        }
        init();

    }

    /**
     * 弹窗高度，默认为屏幕高度的四分之三
     * 子类可重写该方法返回peekHeight
     *
     * @return height
     */
    protected int getPeekHeight() {
        return new myUtils.myDensityHelper(getContext()).dp2px(320);
    }

    private void bindViews(){
        //mReBack = view.findViewById(R.id.submit_add);
        recycleInfo = view.findViewById(R.id.recycle_info);
        ic_addRecycle = view.findViewById(R.id.ic_recycle);
        addLocation = view.findViewById(R.id.ic_location);
        addRecycle = view.findViewById(R.id.cycle_add);
        startTime = view.findViewById(R.id.start_time);
        lengthTime = view.findViewById(R.id.duraion);
        timer_select = view.findViewById(R.id.time_selector);
        submit = view.findViewById(R.id.submit_add);
        date_selected = view.findViewById(R.id.date_selected);
        tv_today=view.findViewById(R.id.today);
        rl_today=view.findViewById(R.id.today_button);
        tv_tomorrow=view.findViewById(R.id.tomorrow);
        rl_tomorrow=view.findViewById(R.id.tomorrow_button);
        tv_select=view.findViewById(R.id.select);
        rl_select=view.findViewById(R.id.select_button);
        detail = view.findViewById(R.id.date_detail);
        title = view.findViewById(R.id.task_title_add);
        description = view.findViewById(R.id.task_desc_add);
        lockPercentText = view.findViewById(R.id.lock_percent_now_add);
        lockPercentSeekBar = view.findViewById(R.id.lock_percent_add);
        lock = view.findViewById(R.id.lock_option_add);
        strongAlarm = view.findViewById((R.id.strong_alarm_option));
        alarm = view.findViewById(R.id.alarm);
        signUp = view.findViewById(R.id.sign_up);
    }
    private void initViews(){

        addRecycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRecyclePickerDialog recycleDialog = new AddRecyclePickerDialog();
                recycleDialog.setCallback(new AddRecyclePickerDialog.Callback() {
                    @Override
                    public void getCycleSelected(int recycleType, int cycle, boolean[] dayOfWeak) {
                        RecycleType = recycleType;
                        Cycle = cycle;
                        DayOfWeek = dayOfWeak;
                        refreshCycleInfo();
                    }
                });
                recycleDialog.setEditable(editable);
                recycleDialog.show(getActivity().getSupportFragmentManager(),"recycleDialog");
            }
        });
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                if(editable && !once) {
                    submitTask();
                }else{
                    if(completed == 0 && !once){
                        cancelTask();
                    }else if(!once) {
                        deleteTask();
                    }
                }
            }
        });
        if(editable) {
            //设置监听
//            mReBack.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //关闭弹窗
//                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//                }
//            });

            rl_today.setOnClickListener(v -> {
                if (position != 1) {
                    clickButton(rl_today, tv_today);
                }
                position = 1;

            });

            rl_tomorrow.setOnClickListener(v -> {
                if (position != 2) {
                    clickButton(rl_tomorrow, tv_tomorrow);
                }
                position = 2;

            });

            rl_select.setOnClickListener(v -> {
                if (position != 3) {
                    clickButton(rl_select, tv_select);
                }
                calendarDialog = new CalendarDialog(mContext);
                calendarDialog.setCancelable(false);
                calendarDialog.setDateSetter(new CalendarDialog.dateSetter() {
                    @Override
                    public void setDate(int year, int month, int day) {
                        Year = year;
                        Month = month;
                        Day = day;
                        date_selected.setText(Year + "年" + Month + "月" + Day + "日");
                    }
                });
                calendarDialog.show();
                position = 3;

            });
            timer_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddTimePickerDialog timeDialog = new AddTimePickerDialog();
                    timeDialog.setCallback(new AddTimePickerDialog.Callback() {
                        @Override
                        public void getTimeSelected(int hour, int minute, int lhour, int lminute) {
                            if (lhour + lminute == 0) lminute++;
                            startHour = hour;
                            startMinute = minute;
                            lenHour = lhour;
                            lenMinute = lminute;
                            refreshTimeInfo();
                        }
                    });
                    timeDialog.show(getActivity().getSupportFragmentManager(), "timeDialog");
                }
            });
            title.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    Title = s.toString();
                }
            });
            description.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    Description = s.toString();
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
            lock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        lockPercentSeekBar.setEnabled(true);
                        lockEnabled = true;
                    } else {
                        lockPercentSeekBar.setProgress(0);
                        lockPercentSeekBar.setEnabled(false);
                        lockEnabled = false;
                    }
                }
            });
            strongAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        StrongAlarm = true;
                    } else {
                        StrongAlarm = false;
                    }
                }
            });
            strongAlarm.setEnabled(false);
            alarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alarm.setChecked(!Alarm);
                    Alarm = !Alarm;
                    strongAlarm.setEnabled(Alarm);
                    if (!Alarm) {
                        strongAlarm.setChecked(false);
                    }
                }
            });
            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signUp.setChecked(!Signup);
                    Signup = !Signup;
                }
            });
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        view = inflater.inflate(R.layout.frag_bottomsheet, container, false);
        return view;
    }
    private void refreshTimeInfo(){
        startTime.setText(String.format("%02d:%02d", startHour, startMinute));
        lengthTime.setText(String.format("%02d:%02d", lenHour, lenMinute));
    }
    private void refreshCycleInfo(){
        if(RecycleType == 0){
            ic_addRecycle.setColorFilter(getActivity().getColor(R.color.grey));
            recycleInfo.setText("单次 无循环");
            recycleInfo.setTextColor(getActivity().getColor(R.color.defaultgrey));
        }else if(RecycleType == 1){
            ic_addRecycle.setColorFilter(getActivity().getColor(R.color.dimgrey));
            recycleInfo.setText("按天  " + Cycle + "周期");
            recycleInfo.setTextColor(getActivity().getColor(R.color.dimgrey));
        }else{
            ic_addRecycle.setColorFilter(getActivity().getColor(R.color.dimgrey));
            recycleInfo.setText("按周  " + Cycle + "周期");
            recycleInfo.setTextColor(getActivity().getColor(R.color.dimgrey));
        }
    }
    //提交给数据给后端
    private void submitTask(){
        myUtils.myToastHelper.showText(getContext(),"添加任务中", Toast.LENGTH_SHORT);
    }
    private void deleteTask(){
        myUtils.myToastHelper.showText(getContext(),"删除任务中",Toast.LENGTH_SHORT);
    }
    private void cancelTask(){
        myUtils.myToastHelper.showText(getContext(),"取消任务中",Toast.LENGTH_SHORT);
    }
}
