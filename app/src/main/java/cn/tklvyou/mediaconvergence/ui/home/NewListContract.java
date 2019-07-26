package cn.tklvyou.mediaconvergence.ui.home;

import java.util.List;

import cn.tklvyou.mediaconvergence.base.BaseContract;
import cn.tklvyou.mediaconvergence.model.BannerModel;
import cn.tklvyou.mediaconvergence.model.NewListModel;

public interface NewListContract {
    interface View extends BaseContract.BaseView{
        void setNewList(int p,NewListModel model);
        void setBanner(List<BannerModel> bannerModelList);
    }
    interface Presenter extends BaseContract.BasePresenter<View>{
        void getNewList(String module,int p);
        void getBanner(String module);
    }
}
