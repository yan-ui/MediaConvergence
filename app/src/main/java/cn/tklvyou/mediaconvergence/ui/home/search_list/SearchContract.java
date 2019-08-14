package cn.tklvyou.mediaconvergence.ui.home.search_list;

import cn.tklvyou.mediaconvergence.base.BaseContract;
import cn.tklvyou.mediaconvergence.model.BasePageModel;
import cn.tklvyou.mediaconvergence.model.NewsBean;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月02日15:21
 * @Email: 971613168@qq.com
 */
public interface SearchContract {


    interface View extends BaseContract.BaseView {
        void setNewList(int p, BasePageModel<NewsBean> model);
    }

    interface Presenter extends BaseContract.BasePresenter<SearchContract.View> {
        void searchNewList(String module, String name, int p);
    }
}
