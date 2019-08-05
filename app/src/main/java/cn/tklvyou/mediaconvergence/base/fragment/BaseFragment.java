package cn.tklvyou.mediaconvergence.base.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.trello.rxlifecycle3.components.support.RxFragment;

import cn.tklvyou.mediaconvergence.base.BaseContract;
import cn.tklvyou.mediaconvergence.base.activity.BaseActivity;
import cn.tklvyou.mediaconvergence.base.interfaces.LazyFragmentControl;

/**
 * 懒加载Fragment基类
 *
 * @param <T>
 */
public abstract class BaseFragment<T extends BaseContract.BasePresenter> extends RxFragment implements BaseContract.BaseView, LazyFragmentControl {
    protected T mPresenter;
    protected BaseActivity mActivity;//activity的上下文对象
    protected Bundle mBundle;

    private static final long DEFAULT_TIME_INTERVAL = 5 * 1000;//默认间隔时间30秒

    private boolean isAlive =false;
    private boolean isPrepared;
    /**
     * 第一次onResume中的调用onUserVisible避免操作与onFirstUserVisible操作重复
     */
    private boolean isFirstResume = true;
    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;

    private long mLastVisibleTime = System.currentTimeMillis();//上一次显示的时间
    private long mTimeInterval = DEFAULT_TIME_INTERVAL;


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mBundle != null) {
            outState.putBundle("bundle", mBundle);
        }
    }

    /**
     * 绑定activity
     *
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (BaseActivity) activity;
    }


    /**
     * 运行在onAttach之后
     * 可以接受别人传递过来的参数,实例化对象.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取bundle,并保存起来
        if (savedInstanceState != null) {
            mBundle = savedInstanceState.getBundle("bundle");
        } else {
            mBundle = getArguments() == null ? new Bundle() : getArguments();
        }
        //创建presenter
        mPresenter = initPresenter();
    }

    /**
     * 运行在onCreate之后
     * 生成view视图
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isAlive = true;
        return inflater.inflate(getFragmentLayoutID(), container, false);
    }

    /**
     * 运行在onCreateView之后
     * 加载数据
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //由于fragment生命周期比较复杂,所以Presenter在onCreateView创建视图之后再进行绑定,不然会报空指针异常
        attachView();
        initView();
        initPrepare();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
            onUserVisible();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                checkLastTime();
                onUserVisible();
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false;
                onFirstUserInvisible();
            } else {
                //界面不显示 设置上次显示时间
                mLastVisibleTime = System.currentTimeMillis();
                onUserInvisible();
            }
        }
    }


    @Override
    public void onDetach() {
        isAlive = false;
        detachView();
        super.onDetach();
    }


    @Override
    public synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
            lazyData();
        } else {
            isPrepared = true;
        }
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


    //运行线程<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**在UI线程中运行，建议用这个方法代替runOnUiThread
     * @param action
     */
    public final void runUiThread(Runnable action) {
        if (isAlive == false) {
            LogUtils.w( "runUiThread  isAlive() == false >> return;");
            return;
        }
        mActivity.runUiThread(action);
    }
    /**运行线程
     * @param name
     * @param runnable
     * @return
     */
    public final Handler runThread(String name, Runnable runnable) {
        if (isAlive == false) {
            LogUtils.w("runThread  isAlive() == false >> return null;");
            return null;
        }
        return mActivity.runThread(name + hashCode(), runnable);//name, runnable);同一Activity出现多个同名Fragment可能会出错
    }

    //运行线程>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 在子View中初始化Presenter
     *
     * @return
     */
    protected abstract T initPresenter();

    /**
     * 设置Fragment的布局ID
     *
     * @return
     */
    protected abstract int getFragmentLayoutID();


    /**
     * 类似Activity的OnBackgress
     * fragment进行回退
     */
    public void onBack() {
        getFragmentManager().popBackStack();
    }



    @Override
    public void showLoading() {
        ((BaseActivity)mActivity).showLoading();
    }

    @Override
    public void hideLoading() {
        ((BaseActivity)mActivity).hideLoading();
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
        ((BaseActivity)mActivity).showNoNet();
    }

    @Override
    public void showNoData() {
        ((BaseActivity)mActivity).showNoData();
    }

    @Override
    public void onRetry() {

    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }




    @Override
    public void checkLastTime() {
        if (System.currentTimeMillis() - mLastVisibleTime > mTimeInterval) {
            onAutoRefresh();
        }
    }

    @Override
    public void onFirstUserVisible() {
        LogUtils.i("onFirstUserVisible:mLastVisibleTime" + mLastVisibleTime);
    }

    @Override
    public void onUserVisible() {
        LogUtils.i("onUserVisible:mLastVisibleTime" + mLastVisibleTime);
    }

    @Override
    public void onFirstUserInvisible() {
        LogUtils.i("onFirstUserInvisible:mLastVisibleTime" + mLastVisibleTime);
    }


    @Override
    public void onUserInvisible() {
        LogUtils.i("onUserInvisible:mLastVisibleTime" + mLastVisibleTime);
    }

    @Override
    public void onAutoRefresh() {
        LogUtils.i("onAutoRefresh:mLastVisibleTime" + mLastVisibleTime);
    }

    @Override
    public void setTimeInterval(long mTimeInterval) {
        this.mTimeInterval = mTimeInterval;
    }

}
