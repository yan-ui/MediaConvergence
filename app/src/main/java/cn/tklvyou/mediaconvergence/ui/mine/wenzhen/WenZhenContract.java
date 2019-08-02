package cn.tklvyou.mediaconvergence.ui.mine.wenzhen;

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
public interface WenZhenContract {


    interface View extends BaseContract.BaseView {
        void setDataList(int page, BasePageModel<NewsBean> pageModel);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void getDataPageList(int page);
    }
}
