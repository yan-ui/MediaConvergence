package cn.tklvyou.mediaconvergence.ui.home.news_detail;


import com.blankj.utilcode.util.ToastUtils;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;


public class NewsDetailPresenter extends BasePresenter<NewsDetailContract.View> implements NewsDetailContract.Presenter {

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
                    if (result.getCode() == 1) {
                        ToastUtils.showShort("评论成功");
                        mView.addCommentSuccess();
                    } else {
                        ToastUtils.showShort(result.getMsg());
                    }
                }, throwable -> throwable.printStackTrace());
    }

}
