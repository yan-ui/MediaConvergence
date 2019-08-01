package cn.tklvyou.mediaconvergence.ui.service;

import com.blankj.utilcode.util.ToastUtils;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;

public class GoodsDetailPresenter extends BasePresenter<GoodsDetailContract.View> implements GoodsDetailContract.Presenter{

    @Override
    public void getGoodsDetails(int id) {
        RetrofitHelper.getInstance().getServer()
                .getGoodsDetail(id)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    if(result.getCode() ==1){
                        mView.setGoodsDetail(result.getData());
                    }else {
                        ToastUtils.showShort(result.getMsg());
                    }
                }, throwable -> throwable.printStackTrace());
    }

    @Override
    public void exchangeGoods(int id) {
        RetrofitHelper.getInstance().getServer()
                .exchangeGoods(id)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    ToastUtils.showShort(result.getMsg());

                }, throwable -> throwable.printStackTrace());
    }
}
