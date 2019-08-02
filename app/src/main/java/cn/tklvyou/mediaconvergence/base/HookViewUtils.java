package cn.tklvyou.mediaconvergence.base;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Hook utils
 * 利用Hook技术
 * hook View的onClickListener
 * 然后通过代理onClickListener
 * 避免了重复点击的问题
 */
public class HookViewUtils {
    public static void hook(Activity mAct) {
        try {
            Class viewClazz = Class.forName("android.view.View");
            //事件监听器都是这个实例保存的
            Method listenerInfoMethod = viewClazz.getDeclaredMethod("getListenerInfo");
            if (!listenerInfoMethod.isAccessible()) {
                listenerInfoMethod.setAccessible(true);
            }

            List<View> list = getAllChildViews(mAct.getWindow().getDecorView());

            for (View view : list) {
                Object listenerInfoObj = listenerInfoMethod.invoke(view);

                Class listenerInfoClazz = Class.forName("android.view.View$ListenerInfo");

                Field onClickListenerField = listenerInfoClazz.getDeclaredField("mOnClickListener");

                if (!onClickListenerField.isAccessible()) {
                    onClickListenerField.setAccessible(true);
                }
                View.OnClickListener mOnClickListener = (View.OnClickListener) onClickListenerField.get(listenerInfoObj);
                //自定义代理事件监听器
                View.OnClickListener onClickListenerProxy = new OnClickListenerProxy(mOnClickListener);
                //更换
                onClickListenerField.set(listenerInfoObj, onClickListenerProxy);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static List<View> getAllChildViews(View view) {
        List<View> allchildren = new ArrayList<View>();
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View viewchild = vp.getChildAt(i);
                allchildren.add(viewchild);
                //再次 调用本身（递归）
                allchildren.addAll(getAllChildViews(viewchild));
            }
        }
        return allchildren;
    }

}