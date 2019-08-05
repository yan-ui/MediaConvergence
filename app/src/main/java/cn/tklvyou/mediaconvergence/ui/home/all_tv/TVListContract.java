package cn.tklvyou.mediaconvergence.ui.home.all_tv;

import java.util.List;

import cn.tklvyou.mediaconvergence.base.BaseContract;
import cn.tklvyou.mediaconvergence.model.BannerModel;
import cn.tklvyou.mediaconvergence.model.BasePageModel;
import cn.tklvyou.mediaconvergence.model.HaveSecondModuleNewsModel;
import cn.tklvyou.mediaconvergence.model.NewsBean;

public interface TVListContract {
    interface View extends BaseContract.BaseView {
        void setNewList(int p, BasePageModel<NewsBean> model);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void getNewList(String module, String module_second, int p);
    }
}
