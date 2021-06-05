package com.greyka.imgr.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.greyka.imgr.R;
import com.greyka.imgr.activities.MapDisplayActivity;
import com.greyka.imgr.activities.MapPoiSearch;
import com.greyka.imgr.data.Data;
import com.greyka.imgr.data.Data.Task;
import com.greyka.imgr.fragments.FragTaskList;
import com.greyka.imgr.utilities.GetData;
import com.greyka.imgr.utilities.myUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.greyka.imgr.utilities.Constants.ERROR_RESPONSE;
import static com.greyka.imgr.utilities.Constants.EXCEPTION;
import static com.greyka.imgr.utilities.Constants.NEGATIVE_RESPONSE;
import static com.greyka.imgr.utilities.Constants.NETWORK_UNAVAILABLE;
import static com.greyka.imgr.utilities.Constants.POSITIVE_RESPONSE;
import static com.greyka.imgr.utilities.Constants.UNKNOWN_RESPONSE;

public class BaseFullBottomSheetFragment extends BottomSheetDialogFragment {

    Data mdata = new Data();
    TextView date_selected;
    private int completed;
    private long taskId;
    private boolean editable = true;
    private boolean once = false;
    private BottomSheetBehavior<FrameLayout> behavior;
    private ActivityResultLauncher<Intent> activityResultLauncher;
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
    private TextView startTime;
    private TextView lengthTime;
    private ImageView ic_addLocation;
    private TextView locationInfo;
    private LinearLayout addLocation;
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
    private boolean[] DayOfWeek = new boolean[]{false, false, false, false, false, false, false};

    private boolean haveLocation = false;
    private String locationNickname = "未添加位置信息";
    private double Latitude;
    private double Longitude;


    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void setOnce(boolean once) {
        this.once = once;
    }

    public void setValues(Data.Task task) {
        Latitude = task.getLatitude();
        Longitude = task.getLongitude();
        locationNickname = task.getPlace_name();
        haveLocation = (Latitude != 0 || Longitude != 0);
        taskId = task.getTask_id();
        completed = task.getCompleted();
        String str = task.getStart_date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Year = c.get(Calendar.YEAR);
        Month = c.get(Calendar.MONTH) + 1;
        Day = c.get(Calendar.DAY_OF_MONTH);
        startHour = c.get(Calendar.HOUR_OF_DAY);
        startMinute = c.get(Calendar.MINUTE);
        Signup = (task.getClock() > 0);
        if (task.getRemind() == 0) {
            Alarm = false;
            StrongAlarm = false;
        } else if (task.getRemind() == 1) {
            Alarm = true;
            StrongAlarm = false;
        } else if (task.getRemind() == 2) {
            Alarm = true;
            StrongAlarm = false;
        }
        if (task.getAllowed() == 100)
            lockEnabled = false;
        else {
            lockEnabled = true;
            lockPercent = task.getAllowed();
        }
        Title = task.getTask_name();
        Description = task.getTask_description();
        lenHour = task.getDuration() / 60;
        lenMinute = task.getDuration() % 60;
        RecycleType = task.getCycleType();
        Cycle = task.getRepeat_count();
        if (RecycleType == 1) {
            if (task.getCycle() == 1) {
                DayOfWeek[1] = true;
            } else if (task.getCycle() == 2) {
                DayOfWeek[2] = true;
            } else if (task.getCycle() == 3) {
                DayOfWeek[1] = true;
            } else if (task.getCycle() == 4) {
                DayOfWeek[1] = true;
            } else if (task.getCycle() == 5) {
                DayOfWeek[1] = true;
            } else if (task.getCycle() == 6) {
                DayOfWeek[1] = true;
            } else if (task.getCycle() == 7) {
                DayOfWeek[0] = true;
            }
        } else if (RecycleType == 2) {
            DayOfWeek[0] = ((task.getSelected() & (1 << 6)) > 0);
            DayOfWeek[1] = ((task.getSelected() & 1) > 0);
            DayOfWeek[2] = ((task.getSelected() & (1 << 1)) > 0);
            DayOfWeek[3] = ((task.getSelected() & (1 << 2)) > 0);
            DayOfWeek[4] = ((task.getSelected() & (1 << 3)) > 0);
            DayOfWeek[5] = ((task.getSelected() & (1 << 4)) > 0);
            DayOfWeek[6] = ((task.getSelected() & (1 << 5)) > 0);
        }

    }

    private void setStaticPage() {
        clickButton(rl_select, tv_select);
        date_selected.setText(Year + "年" + Month + "月" + Day + "日");
        refreshTimeInfo();
        refreshCycleInfo();
        refreshLocationInfo();
        if (!haveLocation) {
            addLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {
            addLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), MapDisplayActivity.class);
                    intent.putExtra("latitude", Latitude);
                    intent.putExtra("longitude", Longitude);
                    startActivity(intent);
                }
            });
        }
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
        title.setHint((CharSequence) Title);
        title.setHintTextColor(getActivity().getColor(R.color.black));
        title.setEnabled(false);
        description.setHint((CharSequence) Description);
        description.setHintTextColor(getActivity().getColor(R.color.dimgrey));
        description.setEnabled(false);
        String[][] submitInfo = new String[][]{
                {"终止", "删除", "删除"},
                {"放弃", "确定", "确定"}
        };
        submit.setText(submitInfo[(once) ? 1 : 0][completed]);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        initMapLauncher();
        //返回BottomSheetDialog的实例
        BottomSheetDialog dialog = new BottomSheetDialog(this.getContext(), R.style.BottomSheetStyle);
        myUtils.myWindowManager.setWindow(dialog);
        return dialog;//设置软键盘弹出时向上移动
    }

    private void initButton(RelativeLayout rl, TextView tv) {
        if (rl == rl_select) {
            detail.setColorFilter(getContext().getColor(R.color.dimgrey));
            date_selected.setText("");
            Year = myUtils.myCalenderHelper.getYear();
            Month = myUtils.myCalenderHelper.getMonth();
            Day = myUtils.myCalenderHelper.getDay();
        }
        GradientDrawable gradientDrawable1 = (GradientDrawable) rl.getBackground();
        gradientDrawable1.setColor(getResources().getColor(R.color.grey91));
        tv.setTextColor(getResources().getColor(R.color.dimgrey));
    }

    private void clickButton(RelativeLayout rl, TextView tv) {
        initButton(rl_today, tv_today);
        initButton(rl_tomorrow, tv_tomorrow);
        initButton(rl_select, tv_select);
        if (rl == rl_select) {
            detail.setColorFilter(getContext().getColor(R.color.white));
            date_selected.setText(Year + "年" + Month + "月" + Day + "日");
        }
        if (rl == rl_today) {
            Year = myUtils.myCalenderHelper.getYear();
            Month = myUtils.myCalenderHelper.getMonth();
            Day = myUtils.myCalenderHelper.getDay();
        }
        if (rl == rl_tomorrow) {
            Year = myUtils.myCalenderHelper.getYearAfterDays(1);
            Month = myUtils.myCalenderHelper.getMonthAfterDays(1);
            Day = myUtils.myCalenderHelper.getDayAfterDays(1);
        }
        Log.d("click", "bbbbbbbbbb");
        GradientDrawable gradientDrawable2 = (GradientDrawable) rl.getBackground();
        gradientDrawable2.setColor(getResources().getColor(R.color.myThemeShallow));
        tv.setTextColor(getResources().getColor(R.color.white));
    }

    private void init() {
        Year = myUtils.myCalenderHelper.getYear();
        Month = myUtils.myCalenderHelper.getMonth();
        Day = myUtils.myCalenderHelper.getDay();
        position = 1;
        bindViews();
        initViews();
        if (!editable) {
            setStaticPage();
        } else {
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

    private void bindViews() {
        //mReBack = view.findViewById(R.id.submit_add);
        recycleInfo = view.findViewById(R.id.recycle_info);
        ic_addRecycle = view.findViewById(R.id.ic_recycle);
        addRecycle = view.findViewById(R.id.cycle_add);
        locationInfo = view.findViewById(R.id.location_info);
        ic_addLocation = view.findViewById(R.id.ic_location);
        addLocation = view.findViewById(R.id.location_add);
        startTime = view.findViewById(R.id.start_time);
        lengthTime = view.findViewById(R.id.duraion);
        timer_select = view.findViewById(R.id.time_selector);
        submit = view.findViewById(R.id.submit_add);
        date_selected = view.findViewById(R.id.date_selected);
        tv_today = view.findViewById(R.id.today);
        rl_today = view.findViewById(R.id.today_button);
        tv_tomorrow = view.findViewById(R.id.tomorrow);
        rl_tomorrow = view.findViewById(R.id.tomorrow_button);
        tv_select = view.findViewById(R.id.select);
        rl_select = view.findViewById(R.id.select_button);
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

    private void initViews() {

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapPoiSearch.class);
                activityResultLauncher.launch(intent);
            }
        });
        addLocation.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                haveLocation = false;
                refreshLocationInfo();
                return true;
            }
        });
        addRecycle.setOnClickListener(v -> {
            AddRecyclePickerDialog recycleDialog = new AddRecyclePickerDialog();
            recycleDialog.setCallback((recycleType, cycle, dayOfWeak) -> {
                RecycleType = recycleType;
                Cycle = cycle;
                DayOfWeek = dayOfWeak;
                refreshCycleInfo();
            });
            recycleDialog.setEditable(editable);
            recycleDialog.show(getActivity().getSupportFragmentManager(), "recycleDialog");
        });
        submit.setOnClickListener(v -> {
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            if (editable && !once) {
                submitTask();
            } else {
                if (completed == 0 && !once) {
                    cancelTask();
                } else if (!once) {
                    deleteTask();
                }
            }
        });
        if (editable) {
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
                calendarDialog.setDateSetter((year, month, day) -> {
                    Year = year;
                    Month = month;
                    Day = day;
                    date_selected.setText(Year + "年" + Month + "月" + Day + "日");
                });
                calendarDialog.show();
                position = 3;

            });
            timer_select.setOnClickListener(v -> {
                AddTimePickerDialog timeDialog = new AddTimePickerDialog();
                timeDialog.setCallback((hour, minute, lhour, lminute) -> {
                    if (lhour + lminute == 0) lminute++;
                    startHour = hour;
                    startMinute = minute;
                    lenHour = lhour;
                    lenMinute = lminute;
                    refreshTimeInfo();
                });
                timeDialog.show(getActivity().getSupportFragmentManager(), "timeDialog");
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
            lock.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    lockPercentSeekBar.setEnabled(true);
                    lockEnabled = true;
                } else {
                    lockPercentSeekBar.setProgress(0);
                    lockPercentSeekBar.setEnabled(false);
                    lockEnabled = false;
                }
            });
            strongAlarm.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    StrongAlarm = true;
                } else {
                    StrongAlarm = false;
                }
            });
            strongAlarm.setEnabled(false);
            alarm.setOnClickListener(v -> {
                alarm.setChecked(!Alarm);
                Alarm = !Alarm;
                strongAlarm.setEnabled(Alarm);
                if (!Alarm) {
                    strongAlarm.setChecked(false);
                }
            });
            signUp.setOnClickListener(v -> {
                signUp.setChecked(!Signup);
                Signup = !Signup;
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

    private void refreshTimeInfo() {
        startTime.setText(String.format("%02d:%02d", startHour, startMinute));
        lengthTime.setText(String.format("%02d:%02d", lenHour, lenMinute));
    }

    private void refreshCycleInfo() {
        if (RecycleType == 0) {
            ic_addRecycle.setColorFilter(getActivity().getColor(R.color.grey));
            recycleInfo.setText("单次 无循环");
            recycleInfo.setTextColor(getActivity().getColor(R.color.defaultgrey));
        } else if (RecycleType == 1) {
            ic_addRecycle.setColorFilter(getActivity().getColor(R.color.dimgrey));
            recycleInfo.setText("按天  " + Cycle + "周期");
            recycleInfo.setTextColor(getActivity().getColor(R.color.dimgrey));
        } else {
            ic_addRecycle.setColorFilter(getActivity().getColor(R.color.dimgrey));
            recycleInfo.setText("按周  " + Cycle + "周期");
            recycleInfo.setTextColor(getActivity().getColor(R.color.dimgrey));
        }
    }

    private void refreshLocationInfo() {
        if (!haveLocation) {
            ic_addLocation.setColorFilter(getActivity().getColor(R.color.grey));
            locationInfo.setText("未设置位置信息");
            locationInfo.setTextColor(getActivity().getColor(R.color.defaultgrey));
        } else {
            ic_addLocation.setColorFilter(getActivity().getColor(R.color.dimgrey));
            locationInfo.setText(locationNickname);
            locationInfo.setTextColor(getActivity().getColor(R.color.dimgrey));
        }
    }

    private void initMapLauncher() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Latitude = data.getDoubleExtra("latitude", 0);
                        Longitude = data.getDoubleExtra("longitude", 0);
                        locationNickname = data.getStringExtra("nickname");
                        Log.d("name", locationNickname);
                        Log.d("name", Latitude + " " + Longitude);
                        haveLocation = true;
                        refreshLocationInfo();
                    }
                });
    }

    private void submitTask() {
        myUtils.myToastHelper.showText(getContext(), "添加任务中", Toast.LENGTH_SHORT);
        Data data = new Data();
        Data.Task task = new Task();
        task.setTask_name(Title);
        task.setTask_description(Description);
        String start_date = String.valueOf(Year) + "-" + String.valueOf(Month) + "-" + String.valueOf(Day) +
                " " + String.valueOf(startHour) + ":" + String.valueOf(startMinute) + ":00";
        task.setStart_date(start_date);
        task.setDuration(lenHour * 60 + lenMinute);
        task.setRepeat_count(Cycle);
        Log.d("taskaaa", String.valueOf(Cycle));
        task.setPlace_name(locationNickname);
        int addAddress;
        if (haveLocation) {
            addAddress = 1;
            task.setLatitude(Latitude);
            task.setLongitude(Longitude);
        } else addAddress = 0;
        task.setAddAddress(addAddress);
        if (Alarm && StrongAlarm) {
            task.setRemind(2);
        } else if (Alarm && (!StrongAlarm)) {
            task.setRemind(1);
        } else if (!Alarm) {
            task.setRemind(0);
        }
        if (lockEnabled) {
            task.setAllowed(lockPercent);
        } else {
            task.setAllowed(100);
        }
        if (Signup) {
            task.setClock(1);
        } else {
            task.setClock(0);
        }
        task.setCycleType(RecycleType);
        if (RecycleType == 1) {
            if (DayOfWeek[1] = true) {
                task.setCycle(1);
            } else if (DayOfWeek[2] = true) {
                task.setCycle(2);
            } else if (DayOfWeek[3] = true) {
                task.setCycle(3);
            } else if (DayOfWeek[4] = true) {
                task.setCycle(4);
            } else if (DayOfWeek[5] = true) {
                task.setCycle(5);
            } else if (DayOfWeek[6] = true) {
                task.setCycle(6);
            } else if (DayOfWeek[0] = true) {
                task.setCycle(7);
            }
        } else if (RecycleType == 2) {
            int select = 0;
            if (DayOfWeek[1] = true) {
                select += 1;
            }
            if (DayOfWeek[2] = true) {
                select += 2;
            }
            if (DayOfWeek[3] = true) {
                select += 4;
            }
            if (DayOfWeek[4] = true) {
                select += 8;
            }
            if (DayOfWeek[5] = true) {
                select += 16;
            }
            if (DayOfWeek[6] = true) {
                select += 32;
            }
            if (DayOfWeek[0] = true) {
                select += 64;
            }
            task.setSelected(select);
            task.setCycle_week(1);
        }
        int result = GetData.attemptCreateTask(mContext, task);
        if (result == POSITIVE_RESPONSE) {
            myUtils.myToastHelper.showText(getContext(), "创建成功", Toast.LENGTH_LONG);
        } else if (result == NEGATIVE_RESPONSE) {
            Log.d("okkkkk", "ok");
            myUtils.myToastHelper.showText(getContext(), "有时间冲突 创建失败", Toast.LENGTH_LONG);
        } else if (result == NETWORK_UNAVAILABLE) {
            myUtils.myToastHelper.showText(getContext(), "无法连接服务器 请检查网络", Toast.LENGTH_LONG);
        } else if (result == UNKNOWN_RESPONSE) {
            myUtils.myToastHelper.showText(getContext(), "未知错误 请重试", Toast.LENGTH_LONG);
        } else if (result == EXCEPTION) {
            myUtils.myToastHelper.showText(getContext(), "出现异常 请重试", Toast.LENGTH_LONG);
        } else if (result == ERROR_RESPONSE) {
            myUtils.myToastHelper.showText(getContext(), "系统异常 请重试", Toast.LENGTH_LONG);
        }
        FragTaskList.refreshTaskList();
    }

    private void deleteTask() {
        // myUtils.myToastHelper.showText(getContext(), "删除任务中", Toast.LENGTH_SHORT);
        int result = GetData.attemptDeleteTask(getContext(), taskId);
        if (result == POSITIVE_RESPONSE) {
            myUtils.myToastHelper.showText(getContext(), "删除成功", Toast.LENGTH_LONG);
        } else if (result == NETWORK_UNAVAILABLE) {
            myUtils.myToastHelper.showText(getContext(), "无法连接服务器 请检查网络", Toast.LENGTH_LONG);
        } else if (result == UNKNOWN_RESPONSE) {
            myUtils.myToastHelper.showText(getContext(), "未知错误 请重试", Toast.LENGTH_LONG);
        } else if (result == EXCEPTION) {
            myUtils.myToastHelper.showText(getContext(), "出现异常 请重试", Toast.LENGTH_LONG);
        } else if (result == ERROR_RESPONSE) {
            myUtils.myToastHelper.showText(getContext(), "系统异常 请重试", Toast.LENGTH_LONG);
        }
        FragTaskList.refreshTaskList();
    }

    private void cancelTask() {
        myUtils.myToastHelper.showText(getContext(), "取消任务中", Toast.LENGTH_SHORT);
        int result = GetData.attemptCancelTask(getContext(), taskId);
        if (result == POSITIVE_RESPONSE) {
            myUtils.myToastHelper.showText(getContext(), "取消成功", Toast.LENGTH_LONG);
        } else if (result == NETWORK_UNAVAILABLE) {
            myUtils.myToastHelper.showText(getContext(), "无法连接服务器 请检查网络", Toast.LENGTH_LONG);
        } else if (result == UNKNOWN_RESPONSE) {
            myUtils.myToastHelper.showText(getContext(), "未知错误 请重试", Toast.LENGTH_LONG);
        } else if (result == EXCEPTION) {
            myUtils.myToastHelper.showText(getContext(), "出现异常 请重试", Toast.LENGTH_LONG);
        } else if (result == ERROR_RESPONSE) {
            myUtils.myToastHelper.showText(getContext(), "系统异常 请重试", Toast.LENGTH_LONG);
        }
        FragTaskList.refreshTaskList();
    }
}
