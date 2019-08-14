package cn.tklvyou.mediaconvergence.ui.main;

import cn.tklvyou.mediaconvergence.base.BaseContract;
import cn.tklvyou.mediaconvergence.model.SystemConfigModel;
import cn.tklvyou.mediaconvergence.model.User;

public interface MainContract {
    interface View extends BaseContract.BaseView{
        void setSystemConfig(SystemConfigModel model);
    }
    interface Presenter extends BaseContract.BasePresenter<View>{
        void getSystemConfig();
    }
}
