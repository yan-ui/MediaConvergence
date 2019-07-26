package cn.tklvyou.mediaconvergence.base;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDexApplication;

import com.blankj.utilcode.util.Utils;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.tklvyou.mediaconvergence.BuildConfig;
import cn.tklvyou.mediaconvergence.R;
import cn.tklvyou.mediaconvergence.common.Contacts;

/**
 * Created by Administrator on 2019/2/27.
 */

public class MyApplication extends MultiDexApplication {

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new BezierCircleHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    private static Context mContext;

    private IWXAPI iwxapi;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        //初始化微信相关配置
        initWx();

        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        //初始化工具包
        Utils.init(this);

    }

    private void initWx() {
        iwxapi = WXAPIFactory.createWXAPI(this, Contacts.WX_APPID, true);
        iwxapi.registerApp(Contacts.WX_APPID);
    }


    public static Context getAppContext() {
        return mContext;
    }

}
