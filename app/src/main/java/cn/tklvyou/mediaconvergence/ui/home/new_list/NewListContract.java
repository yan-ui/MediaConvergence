package cn.tklvyou.mediaconvergence.ui.home.new_list;

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

        void deleteSuccess(int position);

        void setJuZhengHeader(List<NewsBean> beans);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void getNewList(String module, String module_second, int p, boolean showLoading);

        void getHaveSecondModuleNews(int p, String module, boolean showLoading);

        void getBanner(String module);

        void getDetailsById(int id);

        void deleteArticle(int id, int position);

        void getJuZhengHeader(String module);

    }
}
