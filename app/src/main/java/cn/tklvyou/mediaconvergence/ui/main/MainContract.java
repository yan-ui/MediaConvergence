package cn.tklvyou.mediaconvergence.ui.main;

import cn.tklvyou.mediaconvergence.base.BaseContract;
import cn.tklvyou.mediaconvergence.widget.FrameLayout4Loading;

/**
 * 主页配置约定
 */

public interface MainContract {
    interface View extends BaseContract.BaseView{
        void loginSuccess(String msg);
        void loginError(String msg);

    }
    interface Presenter extends BaseContract.BasePresenter<View>{
        void login(FrameLayout4Loading frameLayout4Loading, String name, String password);
    }
}
