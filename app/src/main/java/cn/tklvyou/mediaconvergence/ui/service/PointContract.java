package cn.tklvyou.mediaconvergence.ui.service;

import java.util.List;

import cn.tklvyou.mediaconvergence.base.BaseContract;
import cn.tklvyou.mediaconvergence.model.BasePageModel;
import cn.tklvyou.mediaconvergence.model.PointModel;
import cn.tklvyou.mediaconvergence.model.User;


public interface PointContract {
    interface View extends BaseContract.BaseView {
        void setGoods(int page,BasePageModel<PointModel> pageModel);
        void setUser(User.UserinfoBean bean);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void getGoodsPageList(int page);
        void getUser();
    }
}
