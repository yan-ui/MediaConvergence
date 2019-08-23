package cn.tklvyou.mediaconvergence.ui.service;


import com.blankj.utilcode.util.ToastUtils;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;
import cn.tklvyou.mediaconvergence.helper.AccountHelper;
import cn.tklvyou.mediaconvergence.ui.home.HomeContract;


public class PointPresenter extends BasePresenter<PointContract.View> implements PointContract.Presenter{


    @Override
    public void getGoodsPageList(int page) {
        RetrofitHelper.getInstance().getServer()
                .getGoodsPageList(page)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    if(result.getCode() ==1){
                        mView.setGoods(page,result.getData());
                    }else {
                        ToastUtils.showShort(result.getMsg());
                    }
                }, throwable -> mView.showFailed(""));
    }

    @Override
    public void getUser() {
        RetrofitHelper.getInstance().getServer()
                .getUser()
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    if (result.getCode() == 1) {
                        mView.setUser(result.getData());
                        AccountHelper.getInstance().setUserInfo(result.getData());
                    } else {
                        ToastUtils.showShort(result.getMsg());
                    }

                }, throwable -> {
                    throwable.printStackTrace();
                });
    }
}
