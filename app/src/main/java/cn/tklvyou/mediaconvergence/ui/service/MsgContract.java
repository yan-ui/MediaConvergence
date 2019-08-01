package cn.tklvyou.mediaconvergence.ui.service;

import cn.tklvyou.mediaconvergence.base.BaseContract;
import cn.tklvyou.mediaconvergence.model.BasePageModel;
import cn.tklvyou.mediaconvergence.model.MessageModel;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月01日19:08
 * @Email: 971613168@qq.com
 */
public interface MsgContract {

    interface View extends BaseContract.BaseView {
        void setMessageList(int page, BasePageModel<MessageModel> pageModel);
    }

    interface Presenter extends BaseContract.BasePresenter<MsgContract.View> {
        void getMsgPageList(int page);
    }
}