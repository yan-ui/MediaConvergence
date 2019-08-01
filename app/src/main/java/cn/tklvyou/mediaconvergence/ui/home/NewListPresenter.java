package cn.tklvyou.mediaconvergence.ui.home;


import com.blankj.utilcode.util.ToastUtils;

import java.util.List;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;
import cn.tklvyou.mediaconvergence.base.BaseResult;
import cn.tklvyou.mediaconvergence.model.BannerModel;
import cn.tklvyou.mediaconvergence.model.BasePageModel;
import cn.tklvyou.mediaconvergence.model.NewsBean;
import io.reactivex.functions.Consumer;


public class NewListPresenter extends BasePresenter<NewListContract.View> implements NewListContract.Presenter {

    @Override
    public void getNewList(String module, int p) {
        RetrofitHelper.getInstance().getServer()
                .getNewList(module, p)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                            if (result.getCode() == 1) {
                                mView.setNewList(p, result.getData());
                            } else {
                                ToastUtils.showShort(result.getMsg());
                            }
                        }, throwable -> {
                            throwable.printStackTrace();
                            mView.setNewList(p, null);
                        }
                );
    }

    @Override
    public void getSuixiTVNews(int p) {
        RetrofitHelper.getInstance().getServer()
                .getSuixiTvNews("濉溪TV", p)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                            if (result.getCode() == 1) {
                                mView.setSuixiTVNews(p, result.getData());
                            } else {
                                ToastUtils.showShort(result.getMsg());
                            }
                        }, throwable -> {
                            throwable.printStackTrace();
                            mView.setSuixiTVNews(p, null);
                        }
                );
    }

    @Override
    public void getBanner(String module) {
        RetrofitHelper.getInstance().getServer()
                .getBanner(module)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    if (result.getCode() == 1) {
                        mView.setBanner(result.getData());
                    } else {
                        ToastUtils.showShort(result.getMsg());
                    }
                }, throwable -> throwable.printStackTrace());
    }
}
