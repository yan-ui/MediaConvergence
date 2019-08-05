package cn.tklvyou.mediaconvergence.ui.home.tv_news_detail;


import com.blankj.utilcode.util.ToastUtils;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;
import cn.tklvyou.mediaconvergence.ui.home.news_detail.NewsDetailContract;


public class TVNewsDetailPresenter extends BasePresenter<TVNewsDetailContract.View> implements TVNewsDetailContract.Presenter {

    @Override
    public void getDetailsById(int id) {
        RetrofitHelper.getInstance().getServer()
                .getArticleDetail(id)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    if (result.getCode() == 1) {
                        mView.setDetails(result.getData());
                    } else {
                        ToastUtils.showShort(result.getMsg());
                    }
                }, throwable -> throwable.printStackTrace());
    }

    @Override
    public void addLikeNews(int id) {
        RetrofitHelper.getInstance().getServer()
                .addLikeNews(id)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    if (result.getCode() == 1) {
                        mView.updateLikeStatus(true);
                    } else {
                        ToastUtils.showShort(result.getMsg());
                    }
                }, throwable -> throwable.printStackTrace());
    }

    @Override
    public void cancelLikeNews(int id) {
        RetrofitHelper.getInstance().getServer()
                .cancelLikeNews(id)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    if (result.getCode() == 1) {
                        mView.updateLikeStatus(false);
                    } else {
                        ToastUtils.showShort(result.getMsg());
                    }
                }, throwable -> throwable.printStackTrace());
    }

    @Override
    public void addComment(int id, String detail) {
        RetrofitHelper.getInstance().getServer()
                .addComment(id,detail)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    ToastUtils.showShort(result.getMsg());
                    if (result.getCode() == 1) {
                        mView.addCommentSuccess();
                    }
                }, throwable -> throwable.printStackTrace());
    }

    @Override
    public void setCollectStatus(int id,boolean isCollect) {
        if(isCollect){
            RetrofitHelper.getInstance().getServer()
                    .addCollect(id)
                    .compose(RxSchedulers.applySchedulers())
                    .compose(mView.bindToLife())
                    .subscribe(result -> {
                        if (result.getCode() == 1) {
                            ToastUtils.showShort("收藏成功");
                            mView.setCollectStatusSuccess(true);
                        }else {
                            ToastUtils.showShort(result.getMsg());
                        }
                    }, throwable -> throwable.printStackTrace());
        }else {
            RetrofitHelper.getInstance().getServer()
                    .cancelCollect(id)
                    .compose(RxSchedulers.applySchedulers())
                    .compose(mView.bindToLife())
                    .subscribe(result -> {
                        if (result.getCode() == 1) {
                            ToastUtils.showShort("取消成功");
                            mView.setCollectStatusSuccess(false);
                        }else {
                            ToastUtils.showShort(result.getMsg());
                        }
                    }, throwable -> throwable.printStackTrace());
        }

    }

}
