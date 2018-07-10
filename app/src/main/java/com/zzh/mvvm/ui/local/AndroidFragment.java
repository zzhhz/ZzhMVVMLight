package com.zzh.mvvm.ui.local;

import android.os.Message;
import android.view.View;

import com.zzh.mvvm.R;
import com.zzh.mvvm.base.BaseFragment;
import com.zzh.mvvm.databinding.FragmentAndroidBinding;
import com.zzh.mvvm.model.TestModel;

public class AndroidFragment extends BaseFragment<FragmentAndroidBinding> {
    @Override
    protected int setLayoutResId() {
        return R.layout.fragment_android;
    }

    @Override
    protected void initViewAndData(View fragment) {
        TestModel model = new TestModel();
        model.setTest("http://fd.topitme.com/d/a8/1d/11315383988791da8do.jpg");
        mBindView.setTest(model);

    }

    @Override
    public void setViewListener() {

    }

    @Override
    protected void handlerMessage(Message msg) {

    }
}
