package cn.tklvyou.mediaconvergence.ui.service;

import com.blankj.utilcode.util.ToastUtils;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;

public class GoodsDetailPresenter extends BasePresenter<GoodsDetailContract.View> implements GoodsDetailContract.Presenter{

    @Override
    public void getGoodsDetails(int id) {
        mView.showPageLoading();
        RetrofitHelper.getInstance().getServer()
                .getGoodsDetail(id)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    mView.showSuccess(result.getMsg());
                    if(result.getCode() ==1){
                        mView.setGoodsDetail(result.getData());
                    }
                }, throwable -> mView.showFailed(""));
    }

    @Override
    public void exchangeGoods(int id) {
        RetrofitHelper.getInstance().getServer()
                .exchangeGoods(id)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    ToastUtils.showShort(result.getMsg());
                    if(result.getCode() == 1){
                        mView.exchangeSuccess();
                    }

                }, throwable -> throwable.printStackTrace());
    }
}
