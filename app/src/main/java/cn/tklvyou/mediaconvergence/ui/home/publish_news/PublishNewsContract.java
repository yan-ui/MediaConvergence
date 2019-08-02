package cn.tklvyou.mediaconvergence.ui.home.publish_news;

import java.io.File;
import java.util.List;

import cn.tklvyou.mediaconvergence.base.BaseContract;


public interface PublishNewsContract {
    interface View extends BaseContract.BaseView{
        void publishSuccess();
        void uploadImageSuccess(String url);
        void uploadVideoSuccess(String url);
        void uploadImagesSuccess(List<String> urls);
    }
    interface Presenter extends BaseContract.BasePresenter<View>{
        void uploadFile(File file,boolean isVideo);
        void uploadMultiImage(List<File> files);
        void publishVShi(String name,String video,String image,String time);
        void publishSuiShouPai(String name,String images,String video,String image,String time);
    }
}
