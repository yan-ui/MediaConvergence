package cn.tklvyou.mediaconvergence.ui.home;

import java.util.List;

import cn.tklvyou.mediaconvergence.base.BaseContract;


public interface HomeContract {
    interface View extends BaseContract.BaseView{
        void setHomeChannel(List<String> channelList);
    }
    interface Presenter extends BaseContract.BasePresenter<View>{
        void getHomeChannel();
    }
}
