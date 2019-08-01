package cn.tklvyou.mediaconvergence.ui.setting.edit_pass;

import android.annotation.SuppressLint;

import com.blankj.utilcode.util.ToastUtils;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;

import static cn.tklvyou.mediaconvergence.common.RequestConstant.CODE_REQUEST_SUCCESS;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年07月30日10:15
 * @Email: 971613168@qq.com
 */
@SuppressLint("CheckResult")
public class EditPassPresenter extends BasePresenter<EditPassContract.EditView> implements EditPassContract.EditPassPresenter {


    @Override
    public void getCaptcha(String mobile, String event) {
        RetrofitHelper.getInstance().getServer()
                .sendSms(mobile, event)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    ToastUtils.showShort(result.getMsg());
                    if (result.getCode() == CODE_REQUEST_SUCCESS) {
                        mView.getCaptchaSuccess();
                    }
                }, throwable -> {
                });
    }

    @Override
    public void edit(String mobile, String newPass, String vCode) {
        RetrofitHelper.getInstance().getServer()
                .resetPass(mobile, newPass, vCode)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    ToastUtils.showShort(result.getMsg());
                    if (result.getCode() == CODE_REQUEST_SUCCESS) {
                        mView.editSuccess();
                    } else {
                        mView.editFailed();
                    }
                }, throwable -> {
                });
    }


}
