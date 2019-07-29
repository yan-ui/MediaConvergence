package cn.tklvyou.mediaconvergence.ui.home;

import java.io.File;
import java.util.List;

import cn.tklvyou.mediaconvergence.base.BaseContract;


public interface PublishNewsContract {
    interface View extends BaseContract.BaseView{
        void uploadVideoSuccess(String url);
    }
    interface Presenter extends BaseContract.BasePresenter<View>{
        void uploadVideo(File file);
        void uploadMultiImage(File...files);
        void publishVShi(String name,String video,String image,String time);
        void publishSuiShouPai(String name,List<String> images);
    }
}
