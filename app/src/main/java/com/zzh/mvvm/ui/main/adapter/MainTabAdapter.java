package com.zzh.mvvm.ui.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zzh.mvvm.base.BaseFragment;
import com.zzh.mvvm.base.ZBaseFragment;

import java.util.List;

public class MainTabAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> list;

    public MainTabAdapter(FragmentManager fm, List<BaseFragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }


}
