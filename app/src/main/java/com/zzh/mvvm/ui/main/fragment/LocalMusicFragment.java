package com.zzh.mvvm.ui.main.fragment;

import android.os.Message;
import android.view.View;

import com.zzh.mvvm.R;
import com.zzh.mvvm.base.BaseFragment;
import com.zzh.mvvm.databinding.FragmentMusicLocalBinding;
import com.zzh.mvvm.ui.local.AndroidFragment;
import com.zzh.mvvm.ui.local.BookFragment;
import com.zzh.mvvm.ui.local.JokeFragment;
import com.zzh.mvvm.ui.main.adapter.MainTabAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class LocalMusicFragment extends BaseFragment<FragmentMusicLocalBinding> {
    @Override
    protected int setLayoutResId() {
        return R.layout.fragment_music_local;
    }

    @Override
    protected void initViewAndData(View fragment) {
        List<BaseFragment> list = new ArrayList<>();
        list.add(new AndroidFragment());
        list.add(new BookFragment());
        list.add(new JokeFragment());
        MainTabAdapter adapter = new MainTabAdapter(getChildFragmentManager(), list);
        mBindView.viewPager.setAdapter(adapter);
        mBindView.tabLayout.setupWithViewPager(mBindView.viewPager);
        mBindView.tabLayout.getTabAt(0).setText("玩安卓");
        mBindView.tabLayout.getTabAt(1).setText("书籍");
        mBindView.tabLayout.getTabAt(2).setText("段子");
    }

    @Override
    public void setViewListener() {

    }

    @Override
    protected void handlerMessage(Message msg) {

    }
}
