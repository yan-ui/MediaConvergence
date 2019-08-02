package cn.tklvyou.mediaconvergence.ui.mine.browse;

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
public interface BrowseContract {


    interface View extends BaseContract.BaseView {
        void setBrowseList(int page, BasePageModel<NewsBean> pageModel);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void getBrowsePageList(int page);
    }
}
