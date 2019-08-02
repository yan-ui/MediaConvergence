package cn.tklvyou.mediaconvergence.base;

import android.view.View;

import java.util.Calendar;

/**
 * 代理onClickListener
 * 用于Hook View
 * 防止重复点击的问题
 */

public class OnClickListenerProxy implements View.OnClickListener {

    private static final int MIN_CLICK_DELAY_TIME = 500;
    private long lastClickTime = 0;

    public View.OnClickListener target;

    public OnClickListenerProxy(View.OnClickListener target) {
        this.target = target;
    }

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            if (target != null) target.onClick(v);
        }
    }
}