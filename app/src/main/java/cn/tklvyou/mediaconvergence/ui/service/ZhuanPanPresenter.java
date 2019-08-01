package cn.tklvyou.mediaconvergence.ui.service;

import com.blankj.utilcode.util.ToastUtils;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;
import cn.tklvyou.mediaconvergence.model.Entry;

public class ZhuanPanPresenter extends BasePresenter<ZhuanPanContract.View> implements ZhuanPanContract.Presenter {

    @Override
    public void getLotteryModel() {
        RetrofitHelper.getInstance().getServer()
                .getLotteryModel()
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    if (result.getCode() == 1) {
                        mView.setLotteryModel(result.getData());
                    } else {
                        ToastUtils.showShort(result.getMsg());
                    }
                }, throwable -> throwable.printStackTrace());
    }

    @Override
    public void startLottery() {
        RetrofitHelper.getInstance().getServer()
                .startLottery()
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    if (result.getCode() == 1) {
                        mView.setLotteryResult(result.getData());
                    } else {
                        mView.setLotteryResult(null);
                        ToastUtils.showShort(result.getMsg());
                    }

                }, throwable -> {
                    throwable.printStackTrace();
                    mView.setLotteryResult(null);
                });
    }
}
