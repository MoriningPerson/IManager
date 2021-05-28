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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import org.w3c.dom.Text;

public class BaseFullBottomSheetFragment extends BottomSheetDialogFragment {

    private Context mContext;
    private View view;
    private TextView tv_today;
    private TextView tv_tomorrow;
    private TextView tv_select;
    private RelativeLayout rl_today;
    private RelativeLayout rl_tomorrow;
    private RelativeLayout rl_select;
    private int position;
    private CalendarDialog calendarDialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //返回BottomSheetDialog的实例
        return new BottomSheetDialog(this.getContext());
    }
    private void initButton(RelativeLayout rl,TextView tv){
        GradientDrawable gradientDrawable1 = (GradientDrawable) rl.getBackground();
        gradientDrawable1 .setColor(getResources().getColor(R.color.grey));
        tv.setTextColor(getResources().getColor(R.color.dimgrey));
    }
    private void clickButton(RelativeLayout rl,TextView tv){
        GradientDrawable gradientDrawable2 = (GradientDrawable) rl.getBackground();
        gradientDrawable2 .setColor(getResources().getColor(R.color.myThemeDeep));
        tv.setTextColor(getResources().getColor(R.color.white));
    }
    private void  init(){
        position=0;
        tv_today=view.findViewById(R.id.today);
        rl_today=view.findViewById(R.id.today_button);
        tv_tomorrow=view.findViewById(R.id.tomorrow);
        rl_tomorrow=view.findViewById(R.id.tomorrow_button);
        tv_select=view.findViewById(R.id.select);
        rl_select=view.findViewById(R.id.select_button);

        initButton(rl_today,tv_today);
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
                if(position!=0&&position!=1)
                {
                    initButton(rl_tomorrow,tv_tomorrow);
                    initButton(rl_select,tv_select);
                    clickButton(rl_today,tv_today);
                }
                else if(position==1)
                    initButton(rl_today,tv_today);
                else
                clickButton(rl_today,tv_today);

                position=1;

            });

            rl_tomorrow.setOnClickListener(v -> {
                if(position!=0&&position!=2)
                {
                    initButton(rl_today,tv_today);
                    initButton(rl_select,tv_select);
                    clickButton(rl_tomorrow,tv_tomorrow);
                }
                else if(position==2)
                    initButton(rl_tomorrow,tv_tomorrow);
                else
                clickButton(rl_tomorrow,tv_tomorrow);

                position=2;

            });

            rl_select.setOnClickListener(v -> {
                if(position!=0&&position!=3)
                {
                    initButton(rl_today,tv_today);
                    initButton(rl_tomorrow,tv_tomorrow);
                    clickButton(rl_select,tv_select);
                    calendarDialog = new CalendarDialog(mContext);
                    calendarDialog.setCancelable(false);
                    calendarDialog.show();
                }
                else if(position==2)
                    initButton(rl_select,tv_select);
                else
                {
                    clickButton(rl_select,tv_select);
                    calendarDialog = new CalendarDialog(mContext);
                    calendarDialog.setCancelable(false);
                    calendarDialog.show();
                }


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
        int peekHeight = getResources().getDisplayMetrics().heightPixels;
        //设置弹窗高度为屏幕高度的3/4
        return peekHeight - peekHeight / 3;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        view = inflater.inflate(R.layout.frag_bottomsheet, container, false);
        return view;
    }





}