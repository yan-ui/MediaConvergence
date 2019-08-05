package cn.tklvyou.mediaconvergence.ui.mine.my_article;


import android.annotation.SuppressLint;

import com.blankj.utilcode.util.ToastUtils;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;


@SuppressLint("CheckResult")
public class MyArticleListPresenter extends BasePresenter<MyArticleContract.View> implements MyArticleContract.Presenter {

    @Override
    public void getNewList(String module, int p) {
        RetrofitHelper.getInstance().getServer()
                .getMyArticleList(p, module)
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



}
