package cn.tklvyou.mediaconvergence.base;

import android.app.Application;
import android.content.Context;
import android.content.MutableContextWrapper;
import android.os.StrictMode;
import android.util.Log;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDexApplication;

import com.billy.android.loading.Gloading;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
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
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.smtt.sdk.QbSdk;

import cn.tklvyou.mediaconvergence.R;
import cn.tklvyou.mediaconvergence.common.Contacts;
import cn.tklvyou.mediaconvergence.crash.CrashManager;
import cn.tklvyou.mediaconvergence.manager.FrameLifecycleCallbacks;

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
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header（new BezierCircleHeader(context)）
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

    private static Application mContext;
    public static boolean showSplash = true;

    private IWXAPI iwxapi;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                //初始化工具包
                Utils.init(mContext);

                //初始化全局防重复点击
                GlobalClickCallbacks.init(mContext);

                //初始化微信相关配置
                initWx();
                //初始化微博配置
                initWb();

                // android 7.0系统解决拍照的问题
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                builder.detectFileUriExposure();


                //设置为true时Logcat会输出日志
                Gloading.debug(false);
                //初始化状态管理
                Gloading.initDefault(new GlobalAdapter());

                //异常处理初始化
                CrashManager.init(mContext);

                initTencentTBS();
            }
        }).start();

    }

    private void initTencentTBS() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);
    }

    private void initWb() {
        WbSdk.install(this, new AuthInfo(this, Contacts.WB_APP_KEY, Contacts.WB_REDIRECT_URL,
                Contacts.WB_SCOPE));
    }


    private void initWx() {
        iwxapi = WXAPIFactory.createWXAPI(this, Contacts.WX_APPID, true);
        iwxapi.registerApp(Contacts.WX_APPID);
    }

    public static Context getAppContext() {
        return mContext;
    }


    public static Application getInstance() {
        return mContext;
    }

}
