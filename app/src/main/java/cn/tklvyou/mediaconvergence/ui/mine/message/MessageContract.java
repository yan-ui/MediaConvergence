package cn.tklvyou.mediaconvergence.ui.mine.message;

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
public interface MessageContract {

    interface View extends BaseContract.BaseView {
        void setMessageList(int page, BasePageModel<MessageModel> pageModel);
        void clearSuccess();
    }

    interface Presenter extends BaseContract.BasePresenter<MessageContract.View> {
        void getMsgPageList(int page);
        void clearMessage();
    }
}
