package cn.tklvyou.mediaconvergence.ui.home;


import com.blankj.utilcode.util.ToastUtils;

import java.util.List;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;
import cn.tklvyou.mediaconvergence.base.BaseResult;
import io.reactivex.functions.Consumer;


public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter{

    @Override
    public void getHomeChannel() {
        RetrofitHelper.getInstance().getServer()
               .getHomeChannel()
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    if(result.getCode() ==1){
                        mView.setHomeChannel(result.getData());
                    }else {
                        ToastUtils.showShort(result.getMsg());
                    }
                }, throwable -> throwable.printStackTrace());
    }
}
