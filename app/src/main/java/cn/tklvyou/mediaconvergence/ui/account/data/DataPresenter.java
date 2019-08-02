package cn.tklvyou.mediaconvergence.ui.account.data;


import android.annotation.SuppressLint;

import com.blankj.utilcode.util.ToastUtils;

import java.io.File;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;
import cn.tklvyou.mediaconvergence.common.RequestConstant;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年07月31日17:20
 * @Email: 971613168@qq.com
 */
@SuppressLint("CheckResult")
public class DataPresenter extends BasePresenter<IDataContract.DataView> implements IDataContract.IDataPresenter {


    @Override
    public void editUserInfo(String avatar, String newNickName, String userName, String bio) {
        mView.showLoading();
        RetrofitHelper.getInstance().getServer()
                .editUserInfo(avatar, userName, newNickName, bio)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    mView.hideLoading();
                    if (result.getCode() == RequestConstant.CODE_REQUEST_SUCCESS) {
                        mView.editSuccess();
                    } else {
                        ToastUtils.showShort(result.getMsg());
                    }
                }, throwable -> mView.hideLoading());
    }

    @Override
    public void doUploadImage(File file) {
        final RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RetrofitHelper.getInstance().getServer()
                .upload(body)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    if (result.getCode() == RequestConstant.CODE_REQUEST_SUCCESS) {
                        mView.uploadSuccess(result.getData().getUrl());
                    } else {
                        ToastUtils.showShort(result.getMsg());
                    }
                }, throwable -> {
                    mView.hideLoading();
                    throwable.printStackTrace();
                });
    }
}
