package com.zzh.mvvm;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;

import com.zzh.mvvm.databinding.ActivityMainBinding;
import com.zzh.mvvm.databinding.NavHeaderViewBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding dataBinding;
    NavHeaderViewBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        initDrawLayout();
        initToolbar();
    }

    private void initToolbar() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void initDrawLayout() {
        dataBinding.navView.inflateHeaderView(R.layout.nav_header_view);
        View headerView = dataBinding.navView.getHeaderView(0);
        bind = DataBindingUtil.bind(headerView);

        dataBinding.include.ivDrawLayout.setOnClickListener(v -> {
            dataBinding.drawerLayout.openDrawer(Gravity.LEFT);
        });
    }
}
