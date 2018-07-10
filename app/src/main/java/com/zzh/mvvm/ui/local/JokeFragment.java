package com.zzh.mvvm.ui.local;

import android.os.Message;
import android.view.View;

import com.zzh.mvvm.R;
import com.zzh.mvvm.base.BaseFragment;
import com.zzh.mvvm.databinding.FragmentBookBinding;
import com.zzh.mvvm.databinding.FragmentJokeBinding;

public class JokeFragment extends BaseFragment<FragmentJokeBinding> {
    @Override
    protected int setLayoutResId() {
        return R.layout.fragment_joke;
    }

    @Override
    protected void initViewAndData(View fragment) {

    }

    @Override
    public void setViewListener() {

    }

    @Override
    protected void handlerMessage(Message msg) {

    }
}
