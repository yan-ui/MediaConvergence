package cn.tklvyou.mediaconvergence.ui.service;

import cn.tklvyou.mediaconvergence.base.BaseContract;
import cn.tklvyou.mediaconvergence.model.Entry;
import cn.tklvyou.mediaconvergence.model.LotteryModel;
import cn.tklvyou.mediaconvergence.model.LotteryResultModel;
import cn.tklvyou.mediaconvergence.model.PointModel;


public interface ZhuanPanContract {
    interface View extends BaseContract.BaseView {
        void setLotteryModel(LotteryModel model);
        void setLotteryResult(LotteryResultModel model);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void getLotteryModel();

        void startLottery();

    }

}
