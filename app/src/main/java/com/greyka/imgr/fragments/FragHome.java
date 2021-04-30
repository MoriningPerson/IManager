package com.greyka.imgr.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.*;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.greyka.imgr.R;
import com.greyka.imgr.utilities.myUtils;

public class FragHome extends Fragment {
    TextView textview,home_notice_board_title;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_home,container,false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textview=(TextView)view.findViewById(R.id.welcome);   //  使用viewBinding or dataBinding  去除大量的findviewbyID
        home_notice_board_title=(TextView)view.findViewById(R.id.home_notice_board_title);
        home_notice_board_title.setText(myUtils.myCalenderHelper.getChineseTotal());
    }
}
