package com.greyka.imgr.adapters;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.greyka.imgr.fragments.myDialogFragment;
import com.greyka.imgr.fragments.myObjectFragment;


// 划了直线的代码就不要用  直接替换   这些代码工具类已经被弃用了。会有风险。
public class myCollectionPagerAdapter extends FragmentStatePagerAdapter {
    public myCollectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new myDialogFragment();
        Bundle args = new Bundle();
        // Our object is just an integer :-P
        args.putInt(myObjectFragment.ARG_OBJECT, i + 1);
        fragment.setArguments(args);
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

