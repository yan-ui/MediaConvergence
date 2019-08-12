package cn.tklvyou.mediaconvergence.ui.account;

import android.annotation.SuppressLint;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;
import cn.tklvyou.mediaconvergence.helper.AccountHelper;
import cn.tklvyou.mediaconvergence.ui.setting.edit_phone.EditPhoneContract;

import static cn.tklvyou.mediaconvergence.common.RequestConstant.CODE_REQUEST_SUCCESS;

/**
 * @author :JenkinsZhou
 * @description :修改手机号
 * @company :途酷科技
 * @date 2019年07月30日10:15
 * @Email: 971613168@qq.com
 */
@SuppressLint("CheckResult")
public class BindPhonePresenter extends BasePresenter<BindPhoneContract.View> implements BindPhoneContract.BindPhonePresenter {


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
    public void bindMobile(int third_id, String mobile, String captcha) {
        RetrofitHelper.getInstance().getServer()
                .bindMobile(third_id, mobile, captcha)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    ToastUtils.showShort(result.getMsg());
                    if (result.getCode() == 1) {
                        mView.bindSuccess();
                        AccountHelper.getInstance().setUserInfo(result.getData().getUserinfo());
                        SPUtils.getInstance().put("token", result.getData().getUserinfo().getToken());
                        SPUtils.getInstance().put("login", true);
                        SPUtils.getInstance().put("groupId", result.getData().getUserinfo().getGroup_id());
                    }
                }, throwable -> {

                });
    }


}
