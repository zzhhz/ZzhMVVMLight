package com.zzh.mvvm;

import android.databinding.DataBindingUtil;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;

import com.zzh.mvvm.base.BaseActivity;
import com.zzh.mvvm.base.ZBaseFragment;
import com.zzh.mvvm.databinding.ActivityMainBinding;
import com.zzh.mvvm.databinding.NavHeaderViewBinding;
import com.zzh.mvvm.ui.main.adapter.MainTabAdapter;
import com.zzh.mvvm.ui.main.fragment.FriendsFragment;
import com.zzh.mvvm.ui.main.fragment.LocalMusicFragment;
import com.zzh.mvvm.ui.main.fragment.NetMusicFragment;
import com.zzh.mvvm.ui.main.presenter.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements ViewPager.OnPageChangeListener {
    NavHeaderViewBinding bind;

    @Override
    protected int setLayoutId() {
        setSwipeBackEnable(false);
        return R.layout.activity_main;
    }

    @Override
    protected void initViewAndData() {
        initDrawLayout();
        List<ZBaseFragment> list = new ArrayList<>();
        list.add(new LocalMusicFragment());
        list.add(new NetMusicFragment());
        list.add(new FriendsFragment());
        MainTabAdapter mainTabAdapter = new MainTabAdapter(getSupportFragmentManager(), list);
        mBindView.include.viewPager.setAdapter(mainTabAdapter);
        mBindView.include.setViewModel(new MainViewModel(this));
        bindSelect(1);
        mBindView.include.viewPager.setCurrentItem(1);
    }

    @Override
    protected void initSetListener() {
        mBindView.include.viewPager.addOnPageChangeListener(this);


    }

    @Override
    protected void handlerMessage(Message msg) {

    }

    private void initDrawLayout() {
        mBindView.navView.inflateHeaderView(R.layout.nav_header_view);
        View headerView = mBindView.navView.getHeaderView(0);
        bind = DataBindingUtil.bind(headerView);
        mBindView.include.ivDrawLayout.setOnClickListener(v -> mBindView.drawerLayout.openDrawer(Gravity.LEFT));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_local_music:
                bindSelect(0);
                break;
            case R.id.iv_net_music:
                bindSelect(1);
                break;
            case R.id.iv_friends:
                bindSelect(2);
                break;
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        bindSelect(position);
    }

    public void bindSelect(int position) {
        switch (position) {
            case 0:
                mBindView.include.ivLocalMusic.setImageResource(R.mipmap.titlebar_music_selected);
                mBindView.include.ivNetMusic.setImageResource(R.mipmap.titlebar_discover_normal);
                mBindView.include.ivFriends.setImageResource(R.mipmap.titlebar_friends_normal);
                break;
            case 1:
                mBindView.include.ivLocalMusic.setImageResource(R.mipmap.titlebar_music_normal);
                mBindView.include.ivNetMusic.setImageResource(R.mipmap.titlebar_discover_selected);
                mBindView.include.ivFriends.setImageResource(R.mipmap.titlebar_friends_normal);
                break;
            case 2:
                mBindView.include.ivLocalMusic.setImageResource(R.mipmap.titlebar_music_normal);
                mBindView.include.ivNetMusic.setImageResource(R.mipmap.titlebar_discover_normal);
                mBindView.include.ivFriends.setImageResource(R.mipmap.titlebar_friends_selected);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
