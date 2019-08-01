package cn.tklvyou.mediaconvergence.ui.service;

import cn.tklvyou.mediaconvergence.base.BaseContract;
import cn.tklvyou.mediaconvergence.model.BasePageModel;
import cn.tklvyou.mediaconvergence.model.PointModel;
import cn.tklvyou.mediaconvergence.model.PointRuleModel;
import cn.tklvyou.mediaconvergence.model.User;


public interface PointRuleContract {
    interface View extends BaseContract.BaseView {
        void setPointRule(PointRuleModel model);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void getPointRule();
    }
}
