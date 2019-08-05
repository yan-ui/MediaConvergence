package cn.tklvyou.mediaconvergence.ui.home.publish_wenzheng;

import com.blankj.utilcode.util.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;
import cn.tklvyou.mediaconvergence.ui.home.publish_news.PublishNewsContract;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class PublishWenzhengPresenter extends BasePresenter<PublishWenzhengContract.View> implements PublishWenzhengContract.Presenter {

    @Override
    public void uploadMultiImage(List<File> files) {

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
                        mView.uploadImagesSuccess(result.getData());
                    } else {
                        ToastUtils.showShort(result.getMsg());
                    }
                    mView.hideLoading();
                }, throwable -> {
                    mView.hideLoading();
                    ToastUtils.showShort("上传失败");
                    throwable.printStackTrace();
                });

    }

    @Override
    public void publishWenZheng(String module_second, String name, String content, String images) {
        RetrofitHelper.getInstance().getServer()
                .publishWenZheng(module_second, name, content, images)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    ToastUtils.showShort(result.getMsg());
                    if (result.getCode() == 1) {
                        mView.publishSuccess();
                    }else {
                        mView.hideLoading();
                    }
                }, throwable -> {
                    mView.hideLoading();
                    throwable.printStackTrace();
                });
    }

}
