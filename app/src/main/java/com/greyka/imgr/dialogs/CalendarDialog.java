package com.greyka.imgr.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.CalendarView;

import com.greyka.imgr.R;

public class CalendarDialog extends Dialog {
    private Context context;

    private dateSetter ds;

    public CalendarDialog(Context context) {
        super(context);
        this.context = context;
    }

    public void setDateSetter(dateSetter ds) {
        this.ds = ds;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_calendar);
        this.setCanceledOnTouchOutside(true); // 点击外部会消失
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> ds.setDate(year, month + 1, dayOfMonth));
    }

    public interface dateSetter {
        void setDate(int year, int month, int day);
    }

}
