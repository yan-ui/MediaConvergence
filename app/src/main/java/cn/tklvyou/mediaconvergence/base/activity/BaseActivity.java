package cn.tklvyou.mediaconvergence.base.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.CustomVersionDialogListener;
import com.allenliu.versionchecklib.v2.callback.ForceUpdateListener;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import java.util.ArrayList;
import java.util.List;

import cn.tklvyou.mediaconvergence.R;
import cn.tklvyou.mediaconvergence.base.BaseContract;
import cn.tklvyou.mediaconvergence.base.ConnectionLiveData;
import cn.tklvyou.mediaconvergence.manager.ThreadManager;
import cn.tklvyou.mediaconvergence.widget.BaseDialog;
import cn.tklvyou.mediaconvergence.widget.FrameLayout4Loading;


/**
 * 类描述
 */

public abstract class BaseActivity<P extends BaseContract.BasePresenter> extends RxAppCompatActivity implements BaseContract.BaseView {

    protected FragmentManager fragmentManager;
    protected Activity mContext;
    protected P mPresenter;

    public FrameLayout4Loading mFrameLayout4Loading;
    private CommonTitleBar baseTitleBar;

    private boolean isAlive =false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.basehead_layout);

        //解决华为虚拟键盘关闭导致的Activity被重新创建闪退桌面的问题
        Configuration configuration = getResources().getConfiguration();
        configuration.screenLayout = 0x0100;
        getResources().updateConfiguration(configuration,getResources().getDisplayMetrics());

        isAlive = true;
        threadNameList = new ArrayList<String>();

        View.inflate(this, getActivityLayoutID(), (ViewGroup) findViewById(R.id.base_content));
        initBaseView();
        attachView();
        initView();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ThreadManager.getInstance().destroyThread(threadNameList);
        isAlive = false;
        fragmentManager = null;
        threadNameList = null;
        detachView();

    }

    private void initBaseView() {
        baseTitleBar = findViewById(R.id.baseTitleBar);
        mFrameLayout4Loading = findViewById(R.id.base_content);
        baseTitleBar.setBackgroundResource(R.drawable.shape_gradient_common_titlebar);

        fragmentManager = getSupportFragmentManager();
        mPresenter = initPresenter();
        mContext = this;

        ConnectionLiveData connectionLiveData = new ConnectionLiveData(this);
        connectionLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean) {
                    showNoNet();
                } else {
                    mFrameLayout4Loading.hideAllMask();
                }
            }
        });


        mFrameLayout4Loading.setRefreashClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkUtils.openWirelessSettings();
            }
        });
    }

    /**
     * 获取标题栏对象
     */
    public CommonTitleBar getCommonTitleBar() {
        return baseTitleBar;
    }


    /**
     * 设置标题栏中间文字内容
     *
     * @param title
     */
    public void setTitle(String title) {
        baseTitleBar.getCenterTextView().setText(title);
    }


    /**
     * 设置标题栏 中间文字 与 中间副标题文字内容
     *
     * @param title
     * @param subTitle
     */
    public void setTitle(String title, String subTitle) {
        baseTitleBar.getCenterTextView().setText(title);
        baseTitleBar.getCenterSubTextView().setText(subTitle);
    }


    public void setNavigationText(String leftText) {
        baseTitleBar.setLeftContent(CommonTitleBar.TYPE_LEFT_TEXTVIEW, leftText, 0, 0, 0);
    }

    public void setNavigationText(String leftText, int leftDrawable) {
        baseTitleBar.setLeftContent(CommonTitleBar.TYPE_LEFT_TEXTVIEW, leftText, leftDrawable, 0, 0);
    }

    public void setNavigationImage() {
        baseTitleBar.setLeftContent(CommonTitleBar.TYPE_LEFT_IMAGEBUTTON, "", 0, 0, 0);
    }

    public void setNavigationImage(int leftImageResource) {
        baseTitleBar.setLeftContent(CommonTitleBar.TYPE_LEFT_IMAGEBUTTON, "", 0, leftImageResource, 0);
    }

    public void setNavigationCustom(int leftCustomViewRes) {
        baseTitleBar.setLeftContent(CommonTitleBar.TYPE_LEFT_CUSTOM_VIEW, "", 0, 0, leftCustomViewRes);
    }


    public void setNavigationOnClickListener(CommonTitleBar.OnNavigationListener listener) {
        baseTitleBar.setNavigationListener(listener);
    }


    public void setPositiveText(String rightText) {
        baseTitleBar.setRightContent(CommonTitleBar.TYPE_RIGHT_TEXTVIEW, rightText, 0, 0);
    }

    public void setPositiveImage(int rightImageResource) {
        baseTitleBar.setRightContent(CommonTitleBar.TYPE_RIGHT_IMAGEBUTTON, "", rightImageResource, 0);
    }

    public void setPositiveCustom(int rightCustomViewRes) {
        baseTitleBar.setRightContent(CommonTitleBar.TYPE_RIGHT_CUSTOM_VIEW, "", 0, rightCustomViewRes);
    }

    public void setPositiveOnClickListener(CommonTitleBar.OnPositiveListener listener) {
        baseTitleBar.setPositiveListener(listener);
    }


    /**
     * ToolBar 隐藏与显示 *
     */
    public void showTitleBar() {
        baseTitleBar.setVisibility(View.VISIBLE);
    }

    public void hideTitleBar() {
        baseTitleBar.setVisibility(View.GONE);
    }


    public void updateVersion(Context context, String downloadUrl, String content, boolean isForce) {
        DownloadBuilder builder = AllenVersionChecker.getInstance()
                .downloadOnly(
                        UIData.create().setDownloadUrl(downloadUrl).setContent(content)
                );

        if (isForce) {
            //强制更新 取消回调
            builder.setForceUpdateListener(new ForceUpdateListener() {
                @Override
                public void onShouldForceUpdate() {
                    ActivityUtils.finishAllActivities();
                }
            });
        }

        //静默下载
        builder.setSilentDownload(false);
        //如果本地有安装包缓存也会重新下载apk
        builder.setForceRedownload(true);
        //更新界面选择
        builder.setCustomVersionDialogListener(createCustomDialogOne());
        //自定义下载路径
        builder.setDownloadAPKPath(Environment.getExternalStorageDirectory() + "/CarNations/download/");
        builder.executeMission(context);
    }

    /**
     * 务必用库传回来的context 实例化你的dialog
     * 自定义的dialog UI参数展示，使用versionBundle
     *
     * @return
     */
    private CustomVersionDialogListener createCustomDialogOne() {
        CustomVersionDialogListener listener = new CustomVersionDialogListener() {
            @Override
            public Dialog getCustomVersionDialog(Context context, UIData versionBundle) {
                BaseDialog baseDialog = new BaseDialog(context, R.style.BaseDialog, R.layout.custom_dialog_one_layout);
                TextView textView = baseDialog.findViewById(R.id.tv_msg);
                textView.setText(versionBundle.getContent());
                return baseDialog;
            }
        };
        return listener;
    }


    /**
     * 挂载view
     */
    private void attachView() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }


    /**
     * 卸载view
     */
    private void detachView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 在子View中初始化Presenter
     *
     * @return
     */
    protected abstract P initPresenter();

    /**
     * 设置Activity的布局ID
     *
     * @return
     */
    protected abstract int getActivityLayoutID();

    @Override
    public void showLoading() {
        mFrameLayout4Loading.showLoadingView();
    }

    @Override
    public void hideLoading() {
        mFrameLayout4Loading.hideLoadingView();
    }


    @Override
    public void showSuccess(String message) {
        ToastUtils.showShort(message);
    }

    @Override
    public void showFailed(String message) {
        ToastUtils.showShort(message);
    }

    @Override
    public void showNoNet() {
        mFrameLayout4Loading.showNoNetView();
        ToastUtils.showShort("暂无网络");
    }

    @Override
    public void showNoData() {
        mFrameLayout4Loading.doShowNoData();
        ToastUtils.showShort("暂无数据");
    }


    @Override
    public void onRetry() {

    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }


    /**
     * 显示软件盘
     */
    public void showSoftInput(EditText view) {
        if (view != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (im != null) {
                im.showSoftInput(view,0);
            }
        }
    }

    /**
     * 隐藏软件盘
     */
    public void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (im != null) {
                im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    //运行线程 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**在UI线程中运行，建议用这个方法代替runOnUiThread
     * @param action
     */
    public final void runUiThread(Runnable action) {
        if (!isAlive) {
            LogUtils.w("runUiThread  isAlive() == false >> return;");
            return;
        }
        runOnUiThread(action);
    }
    /**
     * 线程名列表
     */
    protected List<String> threadNameList;
    /**运行线程
     * @param name
     * @param runnable
     * @return
     */
    public final Handler runThread(String name, Runnable runnable) {
        if (!isAlive) {
            LogUtils.w("runThread  isAlive() == false >> return null;");
            return null;
        }
        Handler handler = ThreadManager.getInstance().runThread(name, runnable);
        if (handler == null) {
            LogUtils.e("runThread handler == null >> return null;");
            return null;
        }

        if (threadNameList.contains(name) == false) {
            threadNameList.add(name);
        }
        return handler;
    }

    //运行线程 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


}
