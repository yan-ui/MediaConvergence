package cn.tklvyou.mediaconvergence.ui.home.publish_wenzheng;

import java.io.File;
import java.util.List;

import cn.tklvyou.mediaconvergence.base.BaseContract;


public interface PublishWenzhengContract {
    interface View extends BaseContract.BaseView{
        void publishSuccess();
        void uploadImagesSuccess(List<String> urls);
    }
    interface Presenter extends BaseContract.BasePresenter<View>{
        void uploadMultiImage(List<File> files);
        void publishWenZheng(String module_second, String name, String content, String images);
    }
}
