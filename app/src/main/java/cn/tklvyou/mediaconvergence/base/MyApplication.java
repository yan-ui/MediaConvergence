package cn.tklvyou.mediaconvergence.base;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDexApplication;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.netease.neliveplayer.playerkit.common.log.LogUtil;
import com.netease.neliveplayer.playerkit.sdk.PlayerManager;
import com.netease.neliveplayer.playerkit.sdk.model.SDKInfo;
import com.netease.neliveplayer.playerkit.sdk.model.SDKOptions;
import com.netease.neliveplayer.proxy.config.NEPlayerConfig;
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
import cn.tklvyou.mediaconvergence.crash.CrashManager;

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
        //异常处理初始化
        CrashManager.init(mContext);
        //初始化微信相关配置
        initWx();

        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        //初始化工具包
        Utils.init(this);

        //初始化网易云播放器
        initPlayerSDK();

//        GlobalClickCallbacks.init(this);
    }


    private void initWx() {
        iwxapi = WXAPIFactory.createWXAPI(this, Contacts.WX_APPID, true);
        iwxapi.registerApp(Contacts.WX_APPID);
    }


    private void initPlayerSDK() {
        SDKOptions config = new SDKOptions();
        //是否开启动态加载功能，默认关闭
        //        config.dynamicLoadingConfig = new NEDynamicLoadingConfig();
        //        config.dynamicLoadingConfig.isDynamicLoading = true;
        //        config.dynamicLoadingConfig.isArmeabiv7a = true;
        //        config.dynamicLoadingConfig.armeabiv7aUrl = "your_url";
        //        config.dynamicLoadingConfig.onDynamicLoadingListener = mOnDynamicLoadingListener;
        // SDK将内部的网络请求以回调的方式开给上层，如果需要上层自己进行网络请求请实现config.dataUploadListener，
        // 如果上层不需要自己进行网络请求而是让SDK进行网络请求，这里就不需要操作config.dataUploadListener
//        config.dataUploadListener = mOnDataUploadListener;
        //是否支持H265解码回调
//        config.supportDecodeListener = mOnSupportDecodeListener;
        //这里可以绑定客户的账号系统或device_id，方便出问题时双方联调
        //        config.thirdUserId = "your_id";
        config.privateConfig = new NEPlayerConfig();
        PlayerManager.init(this, config);
        SDKInfo sdkInfo = PlayerManager.getSDKInfo(this);
        LogUtils.i("NESDKInfo:version" + sdkInfo.version + ",deviceId:" + sdkInfo.deviceId);
    }


    public static Context getAppContext() {
        return mContext;
    }

}
