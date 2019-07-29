package cn.tklvyou.mediaconvergence.ui.home;

import com.blankj.utilcode.util.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;
import cn.tklvyou.mediaconvergence.base.BaseResult;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class PublishNewsPresenter extends BasePresenter<PublishNewsContract.View> implements PublishNewsContract.Presenter {

    @Override
    public void uploadVideo(File file) {

        final RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        RetrofitHelper.getInstance().getServer()
                .upload(body)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    if (result.getCode() == 1) {
                        mView.uploadVideoSuccess(result.getData().getUrl());
                    } else {
                        ToastUtils.showShort(result.getMsg());
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                });
    }

    @Override
    public void uploadMultiImage(File... files) {

        List<MultipartBody.Part> bodys = new ArrayList();
        for (File file : files) {
            final RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("images[]", file.getName(), requestFile);
            bodys.add(body);
        }


        RetrofitHelper.getInstance().getServer()
                .uploadFiles(bodys)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    if (result.getCode() == 1) {
                        mView.uploadVideoSuccess(result.getData().getUrl());
                    } else {
                        ToastUtils.showShort(result.getMsg());
                    }
                    mView.hideLoading();
                }, throwable -> {
                    mView.hideLoading();
                    throwable.printStackTrace();
                });

    }


    @Override
    public void publishVShi(String name, String video, String image, String time) {
        RetrofitHelper.getInstance().getServer()
                .publishVShi(name, video, image, time)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    if (result.getCode() == 1) {

                    } else {
                        ToastUtils.showShort(result.getMsg());
                    }
                }, throwable -> throwable.printStackTrace());
    }

    @Override
    public void publishSuiShouPai(String name, List<String> images) {
        RetrofitHelper.getInstance().getServer()
                .publishSuiShouPai(name, images)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    if (result.getCode() == 1) {

                    } else {
                        ToastUtils.showShort(result.getMsg());
                    }
                }, throwable -> throwable.printStackTrace());
    }

}
