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
    TextView textview;
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
        String h = String.valueOf(myUtils.myCalenderHelper.getHour());
        String min = String.valueOf(myUtils.myCalenderHelper.getMinute());
        String d = String.valueOf(myUtils.myCalenderHelper.getDay());
        String m = String.valueOf(myUtils.myCalenderHelper.getMonth());
        String y = String.valueOf(myUtils.myCalenderHelper.getYear());
        String str = "欢迎！\n"+"现在是: "+y+"年"+m+"月"+d+"日"+h+"时"+min+"分";
        Spannable info = new SpannableString(str);
        info.setSpan(new AbsoluteSizeSpan(100),0,3, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        info.setSpan(new AbsoluteSizeSpan(50),3,info.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        textview.setText(info);
    }
}
