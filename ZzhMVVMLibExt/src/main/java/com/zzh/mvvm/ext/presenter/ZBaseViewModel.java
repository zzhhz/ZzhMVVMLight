package com.zzh.mvvm.ext.presenter;

import com.zzh.mvvm.base.ZBaseActivity;

/**
 * Created by user.
 *
 * @date: 2018/4/16
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: ZzhMVVMLibs
 * @since 1.0
 */
public abstract class ZBaseViewModel {
    protected ZBaseActivity mActivity;

    public ZBaseViewModel(ZBaseActivity atx) {
        this.mActivity = atx;
    }

}
