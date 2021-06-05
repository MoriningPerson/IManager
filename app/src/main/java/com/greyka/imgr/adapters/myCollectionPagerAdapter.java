package com.greyka.imgr.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.greyka.imgr.fragments.FragPlanList;
import com.greyka.imgr.fragments.FragTaskList;
import com.greyka.imgr.utilities.myUtils;


// 划了直线的代码就不要用  直接替换   这些代码工具类已经被弃用了。会有风险。
public class myCollectionPagerAdapter extends FragmentStatePagerAdapter {
    FragPlanList fragPlanList;
    FragTaskList fragTaskList;
    private Context context;
    private View mCurrentView;
    public myCollectionPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        fragPlanList = new FragPlanList(context);
        fragTaskList = new FragTaskList(context);
    }
    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return fragPlanList;
        } else {
            return fragTaskList;
        }
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

    public void refreshData() {
        fragTaskList.refreshTaskList();
        fragPlanList.refreshPlanList();
    }
}

