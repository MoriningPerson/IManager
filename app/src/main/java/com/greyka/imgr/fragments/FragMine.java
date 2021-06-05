package com.greyka.imgr.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.greyka.imgr.R;
import com.greyka.imgr.activities.LoginActivity;
import com.greyka.imgr.utilities.myUtils;

import static com.greyka.imgr.utilities.Constants.POSITIVE_RESPONSE;
import static com.greyka.imgr.utilities.GetData.attemptLogout;

public class FragMine extends Fragment {
    TextView username;
    ImageView logout;
    SharedPreferences sp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_mine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        initViews();

    }

    private void bindViews(View view) {
        username = view.findViewById(R.id.username);
        logout = view.findViewById(R.id.ic_logout);
    }

    private void initViews() {
        sp = this.getActivity().getSharedPreferences("UserPassword", Context.MODE_PRIVATE);
        String userName = sp.getString("username", "获取用户名失败");
        username.setText(userName);
        logout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                userLogout();
                return true;
            }
        });
    }

    private void userLogout() {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("username");
        editor.remove("password");
        editor.commit();
        int result = attemptLogout(getContext());
        if (result == POSITIVE_RESPONSE) {
            myUtils.myToastHelper.showText(getContext(), "成功登出", Toast.LENGTH_SHORT);
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            myUtils.myToastHelper.showText(getContext(), "登出出错 请重试", Toast.LENGTH_SHORT);
        }
    }
}
