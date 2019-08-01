package cn.tklvyou.mediaconvergence.ui.account.data;

import java.io.File;

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
        void showInputDialog(String content);
        void editSuccess();
        void uploadSuccess(String url);
    }


    interface IDataPresenter extends BaseContract.BasePresenter<DataView> {
        void editUserInfo(String avatar,String newNickName,String userName,String bio);

        void doUploadImage(File file);
    }





}
