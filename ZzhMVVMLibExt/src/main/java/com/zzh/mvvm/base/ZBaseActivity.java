package com.zzh.mvvm.base;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.zzh.mvvm.swipe.activity.SwipeBackActivity;

/**
 * Created by user.
 *
 * @date: 2018/06/21
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: ZzhMVVMLibs
 * @since 1.0
 */
public abstract class ZBaseActivity<SV extends ViewDataBinding> extends SwipeBackActivity implements View.OnClickListener {
    protected static String TAG;
    protected Context mContext;
    protected BaseHandler mHandler;
    protected Toast mToast;
    public SV mBindView;
    /**
     * 权限
     */
    protected static final int REQUEST_CODE_READ_PERMISSION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getName();
        mContext = this;
        if (mHandler == null) {
            mHandler = new BaseHandler();
        }
        int layoutResId = setLayoutId();
        mBindView = DataBindingUtil.setContentView(this, layoutResId);
        init();
    }

    /**
     * 设置布局id
     *
     * @return
     */
    protected abstract int setLayoutId();

    protected void init() {
        initViewAndData();
        initSetListener();
    }

    /**
     * 初始化控件使用和数据
     */
    protected abstract void initViewAndData();

    /**
     * 设置监听事件
     */
    protected abstract void initSetListener();

    /**
     * 在子Activity中处理handler
     *
     * @param msg 发送过来的msg
     */
    protected abstract void handlerMessage(Message msg);

    public class BaseHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            handlerMessage(msg);
        }
    }

    /**
     * 显示的文字提示信息
     *
     * @param str 显示文字
     */
    protected void showMessage(String str) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, str, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(str);
        }
        mToast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * 6.0处理申请权限，大于Build.VERSION_CODES.M，则申请权限，否则可以直接使用权限
     *
     * @param permission 申请的权限
     * @param code
     */
    protected void requestPermission(String[] permission, int code) {
        if (permission == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, permission, code);
        } else {
            notifyPermission(code, true);
        }
    }

    protected void requestReadStoragePermission() {
        String[] permission = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        boolean result = true;//默认已经都授权了
        outer:
        for (String per : permission) {
            result = verifyGrantPermission(per);
            if (!result) {
                break outer;
            }
        }

        if (!result) {
            requestPermission(permission, REQUEST_CODE_READ_PERMISSION);
        } else {
            notifyPermission(REQUEST_CODE_READ_PERMISSION, true);
        }
    }

    //判断是否授予了权限
    protected boolean verifyGrantPermission(String permission) {
        boolean result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            result = (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        } else {
            result = PermissionChecker.checkSelfPermission(this, permission)
                    == PermissionChecker.PERMISSION_GRANTED;
        }
        return result;
    }

    //

    /**
     * 验证是否申请了权限
     *
     * @param grantResults
     * @return
     */
    public static boolean verifyPermissions(int[] grantResults) {
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

    /**
     * 申请到权限
     *
     * @param code
     * @param flag
     */
    protected void notifyPermission(int code, boolean flag) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (verifyPermissions(grantResults)) {
            notifyPermission(requestCode, true);
        } else {
            notifyPermission(requestCode, false);
        }
    }
}
