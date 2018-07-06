package com.zzh.mvvm.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ZZH on 18/06/21
 *
 * @Date: 16/12/21 14:16
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description: 封装基本的工具
 */
public abstract class ZBaseFragment<SV extends ViewDataBinding> extends Fragment {
    /**
     * 访问读写权限
     */
    protected static final int WRITE_EXTERNAL_STORAGE = 10001;

    protected BaseHandler mHandler;
    protected Context mContext;
    protected static String TAG;
    protected View mContainer;
    protected SV mBindView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        TAG = this.getClass().getName();
        mHandler = new BaseHandler();
        mContext = getActivity();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int contentId = setLayoutResId();
        if (mContainer == null) {
            mBindView = DataBindingUtil.inflate(inflater, contentId, container, false);
            mContainer = mBindView.getRoot();
            init(mContainer);
        }
        return mContainer;
    }

    public void init(View view) {
        initViewAndData(view);
        setViewListener();
    }

    /**
     * 设置布局id
     *
     * @return
     */
    protected abstract int setLayoutResId();

    /**
     * @param fragment
     */
    protected abstract void initViewAndData(View fragment);

    /**
     *
     */
    public abstract void setViewListener();

    /**
     *
     */
    protected class BaseHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            handlerMessage(msg);
        }
    }

    /**
     * 处理消息
     *
     * @param msg 发送消息
     */
    protected abstract void handlerMessage(Message msg);

    /**
     * 6.0之上的权限管理
     *
     * @param permission
     * @param requestCode
     */
    protected void checkPermission(String permission, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int per = getActivity().checkSelfPermission(permission);
            if (PackageManager.PERMISSION_GRANTED != per) {
                boolean flag = getActivity().shouldShowRequestPermissionRationale(permission);
                if (flag) {
                    //弹窗设置
                    new AlertDialog.Builder(getActivity())
                            .setMessage("app需要开启权限才能使用此功能")
                            .setPositiveButton("设置", (dialogInterface, i) -> {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                                getActivity().startActivity(intent);
                            })
                            .setNegativeButton("取消", null)
                            .create()
                            .show();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
                }

            } else {
                doNextPermission(requestCode);
            }
        } else {
            doNextPermission(requestCode);
        }
    }

    /**
     * 授予权限，或者已经取得权限都会走这个方法
     *
     * @param requestCode
     */
    protected void doNextPermission(int requestCode) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (verifyPermissions(grantResults)) {
            doNextPermission(requestCode);
        } else {

        }
    }

    /**
     * 验证是否授予权限
     *
     * @param grantResults
     * @return
     */
    public boolean verifyPermissions(int[] grantResults) {
        if (grantResults.length < 1) {
            return false;
        }

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
