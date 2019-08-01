package cn.tklvyou.mediaconvergence.ui.service;

import cn.tklvyou.mediaconvergence.base.BaseContract;
import cn.tklvyou.mediaconvergence.model.BasePageModel;
import cn.tklvyou.mediaconvergence.model.PointModel;


public interface GoodsDetailContract {
    interface View extends BaseContract.BaseView {
        void setGoodsDetail(PointModel model);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void getGoodsDetails(int id);
        void exchangeGoods(int id);
    }
}
