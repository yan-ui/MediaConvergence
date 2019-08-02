package cn.tklvyou.mediaconvergence.ui.home;

import java.util.List;

import cn.tklvyou.mediaconvergence.base.BaseContract;
import cn.tklvyou.mediaconvergence.model.BannerModel;
import cn.tklvyou.mediaconvergence.model.BasePageModel;
import cn.tklvyou.mediaconvergence.model.NewsBean;
import cn.tklvyou.mediaconvergence.model.HaveSecondModuleNewsModel;

public interface NewListContract {
    interface View extends BaseContract.BaseView {
        void setNewList(int p, BasePageModel<NewsBean> model);

        void setHaveSecondModuleNews(int p, List<HaveSecondModuleNewsModel> datas);

        void setBanner(List<BannerModel> bannerModelList);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void getNewList(String module, int p);

        void getHaveSecondModuleNews(int p, String module, String module_second);

        void getBanner(String module);
    }
}
