package cn.tklvyou.mediaconvergence.ui.home.tv_news_detail;

import java.util.List;

import cn.tklvyou.mediaconvergence.base.BaseContract;
import cn.tklvyou.mediaconvergence.model.NewsBean;


public interface TVNewsDetailContract {
    interface View extends BaseContract.BaseView{
        void setDetails(NewsBean bean);
        void updateLikeStatus(boolean isLike);
        void addCommentSuccess();
        void setCollectStatusSuccess(boolean isCollect);
    }
    interface Presenter extends BaseContract.BasePresenter<View>{
        void getDetailsById(int id);
        void addLikeNews(int id);
        void cancelLikeNews(int id);
        void addComment(int id, String detail);
        void setCollectStatus(int id, boolean isCollect);
        void getScoreByRead(int id);
    }
}
