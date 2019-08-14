package cn.tklvyou.mediaconvergence.ui.home.comment;

import cn.tklvyou.mediaconvergence.base.BaseContract;
import cn.tklvyou.mediaconvergence.model.BasePageModel;
import cn.tklvyou.mediaconvergence.model.CommentModel;
import cn.tklvyou.mediaconvergence.model.NewsBean;

public interface CommentContract {


    interface View extends BaseContract.BaseView {
        void setCommentList(int p, BasePageModel<CommentModel> model);
    }

    interface Presenter extends BaseContract.BasePresenter<CommentContract.View> {
        void getCommentList(int article_id, int p);
    }
}
