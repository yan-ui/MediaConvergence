package cn.tklvyou.mediaconvergence.ui.home;


import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;
import cn.tklvyou.mediaconvergence.base.BaseResult;
import io.reactivex.functions.Consumer;


public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter {

    @Override
    public void getHomeChannel() {
        RetrofitHelper.getInstance().getServer()
                .getHomeChannel()
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                            if (result.getCode() == 1) {
                                mView.setHomeChannel(result.getData());
                                SPUtils.getInstance().put("channel", new HashSet<String>(result.getData()));
                            } else {
                                mView.setHomeChannel(new ArrayList<>(SPUtils.getInstance().getStringSet("channel")));
                                ToastUtils.showShort(result.getMsg());
                            }
                        }, throwable -> {
                            throwable.printStackTrace();
                            mView.setHomeChannel(new ArrayList<>(SPUtils.getInstance().getStringSet("channel")));
                        }

                );
    }
}
