package cn.tklvyou.mediaconvergence.ui.home.publish_wenzheng;

import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;

import org.apache.commons.lang.RandomStringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;
import cn.tklvyou.mediaconvergence.ui.home.publish_news.PublishNewsContract;
import cn.tklvyou.mediaconvergence.utils.QiniuUploadManager;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class PublishWenzhengPresenter extends BasePresenter<PublishWenzhengContract.View> implements PublishWenzhengContract.Presenter {

    private static final String TAG = "PublishWenzhengPresente";

    @Override
    public void getQiniuToken() {
        RetrofitHelper.getInstance().getServer()
                .getQiniuToken()
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    if (result.getCode() == 1) {
                        mView.setQiniuToken(result.getData().toString());
                    } else {
                        ToastUtils.showShort(result.getMsg());
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                });
    }

    @Override
    public void getJuZhengHeader(String module) {
        RetrofitHelper.getInstance().getServer()
                .getJuZhengHeader(module)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    if(result.getCode() ==1){
                        mView.setJuZhengHeader(result.getData());
                    }else {
                        ToastUtils.showShort(result.getMsg());
                    }
                }, throwable -> throwable.printStackTrace());
    }


    @Override
    public void qiniuUploadMultiImage(List<File> files, String token, String uid, QiniuUploadManager manager) {
        List<QiniuUploadManager.QiniuUploadFile> bodys = new ArrayList<>();
        List<String> keys = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {

            String currentTim = String.valueOf(System.currentTimeMillis());
            String key = "upload/" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "/" + uid + "_" + currentTim + "_" + RandomStringUtils.randomAlphanumeric(6) + ".jpg";
            String mimeType = "image/jpeg";

            QiniuUploadManager.QiniuUploadFile param = new QiniuUploadManager.QiniuUploadFile(files.get(i).getAbsolutePath(), key, mimeType, token);
            bodys.add(param);
            keys.add(key);
        }

        manager.upload(bodys, new QiniuUploadManager.OnUploadListener() {
            @Override
            public void onStartUpload() {
                Log.e(TAG, "onStartUpload");
            }

            @Override
            public void onUploadProgress(String key, double percent) {
            }

            @Override
            public void onUploadFailed(String key, String err) {
                Log.e(TAG, "onUploadFailed:" + err);
                mView.hideLoading();
            }

            @Override
            public void onUploadBlockComplete(String key) {
                Log.e(TAG, "onUploadBlockComplete");
            }

            @Override
            public void onUploadCompleted() {
                mView.hideLoading();
                mView.uploadImagesSuccess(keys);
            }

            @Override
            public void onUploadCancel() {
                Log.e(TAG, "onUploadCancel");
            }
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
