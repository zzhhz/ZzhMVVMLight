package com.zzh.mvvm.ui.main.presenter;

import android.view.View;

import com.zzh.mvvm.MainActivity;
import com.zzh.mvvm.base.BaseActivity;
import com.zzh.mvvm.base.BaseViewModel;

public class MainViewModel extends BaseViewModel {

    public MainActivity mAtx;

    public MainViewModel(BaseActivity atx) {
        super(atx);
        mAtx = (MainActivity) atx;
    }

    public void onClickView(View v, int position) {
        mAtx.bindSelect(position);
        mAtx.mBindView.include.viewPager.setCurrentItem(position);
    }

}
