package com.greyka.imgr.adapters;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.greyka.imgr.data.Data;
import com.greyka.imgr.fragments.FragPlanList;
import com.greyka.imgr.fragments.FragTaskList;
import com.greyka.imgr.fragments.myPermissionDialogFragment;
import com.greyka.imgr.fragments.myObjectFragment;

import java.util.List;


// 划了直线的代码就不要用  直接替换   这些代码工具类已经被弃用了。会有风险。
public class myCollectionPagerAdapter extends FragmentStatePagerAdapter {
    private Context context;
    public myCollectionPagerAdapter(FragmentManager fm, Context context) {

        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment;
        if(i==0){
            fragment = new FragPlanList(this.context);
        }
        else{
            fragment = new FragTaskList(this.context);
        }
        return fragment;


    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "我的计划";
        }
        if (position == 1) {
            return "我的任务";
        }
        return "null";
    }
}

