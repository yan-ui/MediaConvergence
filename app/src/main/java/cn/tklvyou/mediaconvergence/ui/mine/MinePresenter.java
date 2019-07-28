package cn.tklvyou.mediaconvergence.ui.mine;


import com.blankj.utilcode.util.ToastUtils;

import java.util.List;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;
import cn.tklvyou.mediaconvergence.base.BaseResult;
import cn.tklvyou.mediaconvergence.ui.home.HomeContract;
import io.reactivex.functions.Consumer;


public class MinePresenter extends BasePresenter<MineContract.View> implements MineContract.Presenter {

    @Override
    public void getUser() {
        RetrofitHelper.getInstance().getServer()
                .getUser()
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    if (result.getCode() == 1) {
                        mView.setUser(result.getData());
                    } else {
                        ToastUtils.showShort(result.getMsg());
                    }

                }, throwable -> {
                    throwable.printStackTrace();
                });
    }
}
