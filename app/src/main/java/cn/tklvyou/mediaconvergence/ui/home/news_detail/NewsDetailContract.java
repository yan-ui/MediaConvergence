package cn.tklvyou.mediaconvergence.ui.home.news_detail;

import java.util.List;

import cn.tklvyou.mediaconvergence.base.BaseContract;
import cn.tklvyou.mediaconvergence.model.NewsBean;


public interface NewsDetailContract {
    interface View extends BaseContract.BaseView{
        void setDetails(NewsBean bean);
        void updateLikeStatus(boolean isLike);
        void addCommentSuccess();
    }
    interface Presenter extends BaseContract.BasePresenter<View>{
        void getDetailsById(int id);
        void addLikeNews(int id);
        void cancelLikeNews(int id);
        void addComment(int id,String detail);
    }
}
