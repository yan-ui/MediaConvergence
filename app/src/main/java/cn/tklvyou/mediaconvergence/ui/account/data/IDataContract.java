package cn.tklvyou.mediaconvergence.ui.account.data;

import cn.tklvyou.mediaconvergence.base.BaseContract;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年07月31日16:57
 * @Email: 971613168@qq.com
 */
public interface IDataContract {

    interface DataView extends BaseContract.BaseView {
        void showAvatar();
        void editSuccess();
        void editFailed();
    }


    interface IDataPresenter extends BaseContract.BasePresenter<DataView> {

    }




    interface editData extends BaseContract.BasePresenter<DataView> {
        void requestEdit(String avatar, String username, String nickname, String bio);

    }
}
