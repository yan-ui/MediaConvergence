package cn.tklvyou.mediaconvergence.ui.mine.my_article;

import cn.tklvyou.mediaconvergence.base.BaseContract;
import cn.tklvyou.mediaconvergence.model.BasePageModel;
import cn.tklvyou.mediaconvergence.model.NewsBean;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月05日13:54
 * @Email: 971613168@qq.com
 */
public interface MyArticleContract {

    interface View extends BaseContract.BaseView {
        void setNewList(int p, BasePageModel<NewsBean> model);
        void deleteSuccess(int position);
    }

    interface Presenter extends BaseContract.BasePresenter<MyArticleContract.View> {
        void getNewList(String module,  int p);
        void deleteArticle(int id,int position);
        void deleteArticles(int id,int position);
    }
}
