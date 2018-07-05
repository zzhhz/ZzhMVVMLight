package com.zzh.mvvm.swipe.activity;


import com.zzh.mvvm.swipe.SwipeBackLayout;
/**
 * @date: 2018/4/16 下午2:21
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: SwipeBackActivityBase.java
 * @version 1
 */
public interface SwipeBackActivityBase {
    /**
     * @return the SwipeBackLayout associated with this activity.
     */
    public abstract SwipeBackLayout getSwipeBackLayout();

    public abstract void setSwipeBackEnable(boolean enable);

    /**
     * Scroll out contentView and finish the activity
     */
    public abstract void scrollToFinishActivity();

}
