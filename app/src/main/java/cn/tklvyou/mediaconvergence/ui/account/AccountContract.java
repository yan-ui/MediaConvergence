package cn.tklvyou.mediaconvergence.ui.account;

import cn.tklvyou.mediaconvergence.base.BaseContract;
import cn.tklvyou.mediaconvergence.model.SystemConfigModel;
import cn.tklvyou.mediaconvergence.widget.FrameLayout4Loading;

/**
 * 账号管理：登录注册配置约定
 */

public interface AccountContract {
    interface LoginView extends BaseContract.BaseView{
        void loginSuccess();
        void loginError();
        void bindMobile(int third_id);
    }
    interface LoginPresenter extends BaseContract.BasePresenter<LoginView>{
        void login(String name, String password);
        void thirdLogin(String platform,String code);
    }

    interface RegisterView extends BaseContract.BaseView{
        void registerSuccess();
        void getCaptchaSuccess();
    }


    interface RegisterPresenter extends BaseContract.BasePresenter<RegisterView>{
        void getCaptcha(String mobile,String event);
        void register(String mobile,String password,String captcha);
    }

    interface ForgetView extends BaseContract.BaseView{
        void getCaptchaSuccess();
        void resetpwdSuccess();
    }
    interface ForgetPresenter extends BaseContract.BasePresenter<ForgetView>{
        void getCaptcha(String mobile,String event);
        void resetpwd(String mobile, String newpassword,String captcha);
    }

    interface AgreenmentView extends BaseContract.BaseView{
        void setSystemConfig(SystemConfigModel model);
    }
    interface AgreenmentPresenter extends BaseContract.BasePresenter<AgreenmentView>{
        void getSystemConfig();
    }

}
