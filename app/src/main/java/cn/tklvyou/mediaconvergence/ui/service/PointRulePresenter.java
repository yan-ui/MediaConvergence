package cn.tklvyou.mediaconvergence.ui.service;


import com.blankj.utilcode.util.ToastUtils;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;
import cn.tklvyou.mediaconvergence.helper.AccountHelper;


public class PointRulePresenter extends BasePresenter<PointRuleContract.View> implements PointRuleContract.Presenter{

    @Override
    public void getPointRule() {
        RetrofitHelper.getInstance().getServer()
                .getScoreRule()
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    if (result.getCode() == 1) {
                        mView.setPointRule(result.getData());
                    } else {
                        ToastUtils.showShort(result.getMsg());
                    }

                }, throwable -> {
                    throwable.printStackTrace();
                });
    }
}
