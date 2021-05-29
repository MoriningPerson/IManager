package com.greyka.imgr.fragments;

import com.greyka.imgr.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.greyka.imgr.dialogs.CalendarDialog;
import com.greyka.imgr.dialogs.TaskListSelector;
import com.greyka.imgr.utilities.myUtils;

import org.w3c.dom.Text;

public class BaseFullBottomSheetFragment extends BottomSheetDialogFragment {

    private Context mContext;
    private View view;
    private ImageView detail;
    private TextView tv_today;
    private TextView tv_tomorrow;
    private TextView tv_select;
    private RelativeLayout rl_today;
    private RelativeLayout rl_tomorrow;
    private RelativeLayout rl_select;
    private RadioButton signUp;
    private int position = 1;
    private CalendarDialog calendarDialog;
    TextView date_selected;


    private int Year, Month, Day;
    private boolean Signup = false;
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
        signUp = view.findViewById(R.id.sign_up);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp.setChecked(!Signup);
                Signup = !Signup;
            }
        });
        date_selected = view.findViewById(R.id.date_selected);
        tv_today=view.findViewById(R.id.today);
        rl_today=view.findViewById(R.id.today_button);
        tv_tomorrow=view.findViewById(R.id.tomorrow);
        rl_tomorrow=view.findViewById(R.id.tomorrow_button);
        tv_select=view.findViewById(R.id.select);
        rl_select=view.findViewById(R.id.select_button);
        detail = view.findViewById(R.id.date_detail);

        clickButton(rl_today,tv_today);
        initButton(rl_tomorrow,tv_tomorrow);
        initButton(rl_select,tv_select);


    }


    @Override
    public void onStart() {
        super.onStart();
        init();
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

            final BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
            //peekHeight即弹窗的最大高度
            behavior.setPeekHeight(getPeekHeight());
            // 初始为展开状态
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            TextView mReBack = view.findViewById(R.id.submit);
            //设置监听
            mReBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //关闭弹窗
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            });

            rl_today.setOnClickListener(v -> {
                if(position!=1)
                {
                    initButton(rl_tomorrow,tv_tomorrow);
                    initButton(rl_select,tv_select);
                    clickButton(rl_today,tv_today);
                }
                position=1;

            });

            rl_tomorrow.setOnClickListener(v -> {
                if(position!=2)
                {
                    initButton(rl_today,tv_today);
                    initButton(rl_select,tv_select);
                    clickButton(rl_tomorrow,tv_tomorrow);
                }
                position=2;

            });

            rl_select.setOnClickListener(v -> {
                if(position!=3)
                {
                    initButton(rl_today,tv_today);
                    initButton(rl_tomorrow,tv_tomorrow);
                    clickButton(rl_select,tv_select);
                }
                calendarDialog = new CalendarDialog(mContext);
                calendarDialog.setCancelable(false);
                calendarDialog.setDateSetter(new CalendarDialog.dateSetter() {
                    @Override
                    public void setDate(int year, int month, int day) {
                        Year = year;
                        Month = month;
                        Day = day;
                        date_selected.setText(Year+"年"+Month+"月"+Day+"日");
                    }
                });
                calendarDialog.show();
                position=3;

            });
        }

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        view = inflater.inflate(R.layout.frag_bottomsheet, container, false);
        return view;
    }





}