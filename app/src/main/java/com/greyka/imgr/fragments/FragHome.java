package com.greyka.imgr.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.greyka.imgr.R;
import com.greyka.imgr.activities.Timer;
import com.greyka.imgr.utilities.myUtils;

public class FragHome extends Fragment {
    private TextView home_notice_board_title;
    private CardView timer;
    private CardView button2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_home, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        home_notice_board_title = view.findViewById(R.id.home_notice_board_title);
        home_notice_board_title.setText(myUtils.myCalenderHelper.getChineseTotal());
        timer = view.findViewById(R.id.home_timer_button);
        timer.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), Timer.class);
            startActivity(intent);
        });
        /*button2 = view.findViewById(R.id.home_button2);
        button2.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MapDisplayActivity.class);
            startActivity(intent);
        });*/
    }
}
