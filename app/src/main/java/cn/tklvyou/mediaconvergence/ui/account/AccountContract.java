package cn.tklvyou.mediaconvergence.ui.account;

import cn.tklvyou.mediaconvergence.base.BaseContract;
import cn.tklvyou.mediaconvergence.widget.FrameLayout4Loading;

/**
 * 账号管理：登录注册配置约定
 */

public interface AccountContract {
    interface LoginView extends BaseContract.BaseView{
        void loginSuccess(String msg);
        void loginError(String msg);
    }
    interface LoginPresenter extends BaseContract.BasePresenter<LoginView>{
        void login(String name, String password);
    }

    interface RegisterView extends BaseContract.BaseView{
        void registerSuccess(String msg);
        void registerError(String msg);
    }

    interface RegisterPresenter extends BaseContract.BasePresenter<RegisterView>{
        void register(FrameLayout4Loading frameLayout4Loading,String name,String password);
    }
}
