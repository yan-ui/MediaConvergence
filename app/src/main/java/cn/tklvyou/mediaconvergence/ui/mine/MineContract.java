package cn.tklvyou.mediaconvergence.ui.mine;

import java.util.List;

import cn.tklvyou.mediaconvergence.base.BaseContract;
import cn.tklvyou.mediaconvergence.model.User;

public interface MineContract {
    interface View extends BaseContract.BaseView{
        void setUser(User.UserinfoBean user);
    }
    interface Presenter extends BaseContract.BasePresenter<View>{
        void getUser();
    }
}
