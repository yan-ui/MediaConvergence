package cn.tklvyou.mediaconvergence.ui.mine.collection;

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
public interface CollectContract {


    interface View extends BaseContract.BaseView {
        void setCollectList(int page, BasePageModel<NewsBean> pageModel);
    }

    interface Presenter extends BaseContract.BasePresenter<CollectContract.View> {
        void getCollectPageList(int page);
    }
}
