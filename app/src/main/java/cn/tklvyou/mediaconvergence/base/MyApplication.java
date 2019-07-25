package cn.tklvyou.mediaconvergence.base;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import androidx.multidex.MultiDexApplication;

import com.blankj.utilcode.util.Utils;

import cn.tklvyou.mediaconvergence.BuildConfig;

/**
 * Created by Administrator on 2019/2/27.
 */

public class MyApplication extends MultiDexApplication {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        //初始化工具包
        Utils.init(this);

    }


    public static Context getAppContext() {
        return mContext;
    }

}
