package cn.tklvyou.mediaconvergence.ui.home.comment;

import android.annotation.SuppressLint;

import com.blankj.utilcode.util.ToastUtils;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;
import cn.tklvyou.mediaconvergence.ui.home.search_list.SearchContract;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月02日15:25
 * @Email: 971613168@qq.com
 */
@SuppressLint("CheckResult")
public class CommentPresenter extends BasePresenter<CommentContract.View> implements CommentContract.Presenter {

    @Override
    public void getCommentList(int article_id, int p) {
        RetrofitHelper.getInstance().getServer()
                .getCommentList(article_id, p)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                            if (result.getCode() == 1) {
                                mView.setCommentList(p, result.getData());
                            } else {
                                ToastUtils.showShort(result.getMsg());
                            }
                        }, throwable -> {
                            mView.showFailed("");
                        }
                );
    }
}
