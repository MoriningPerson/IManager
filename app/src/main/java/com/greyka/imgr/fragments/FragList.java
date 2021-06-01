package com.greyka.imgr.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.greyka.imgr.R;
import com.greyka.imgr.adapters.myCollectionPagerAdapter;

public class FragList extends Fragment {
    myCollectionPagerAdapter demoCollectionPagerAdapter;
    ViewPager viewPager;
    ImageButton refresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        demoCollectionPagerAdapter = new myCollectionPagerAdapter(getChildFragmentManager(),getActivity());
        viewPager = view.findViewById(R.id.pager);
        refresh = view.findViewById(R.id.refresh_list);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onclick","ref");
                demoCollectionPagerAdapter.refreshData();
            }
        });
        viewPager.setAdapter(demoCollectionPagerAdapter);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }
}
