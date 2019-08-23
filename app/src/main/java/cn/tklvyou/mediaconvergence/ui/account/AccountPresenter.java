package cn.tklvyou.mediaconvergence.ui.account;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;
import cn.tklvyou.mediaconvergence.helper.AccountHelper;


/**
 * 描述
 */

public class AccountPresenter extends BasePresenter<AccountContract.View> implements AccountContract.Presenter {

    @Override
    public void getCaptcha(String mobile, String event) {
        RetrofitHelper.getInstance().getServer()
                .sendSms(mobile, event)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    ToastUtils.showShort(result.getMsg());
                    if (result.getCode() == 1) {
                        mView.getCaptchaSuccess();
                    }
                }, throwable -> {
                });
    }


    @Override
    public void login(final String name, final String password) {
        mView.showLoading();
        RetrofitHelper.getInstance().getServer()
                .login(name, password)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    mView.showSuccess(result.getMsg());
                    if (result.getCode() == 1) {
                        mView.loginSuccess();
                        AccountHelper.getInstance().setUserInfo(result.getData().getUserinfo());
                        SPUtils.getInstance().put("token", result.getData().getUserinfo().getToken());
                        SPUtils.getInstance().put("login", true);
                        SPUtils.getInstance().put("groupId", result.getData().getUserinfo().getGroup_id());
                    }
                }, throwable -> {
                    mView.showSuccess("");
                    mView.loginError();
                });
    }

    @Override
    public void codeLogin(String mobile, String code) {
        mView.showLoading();
        RetrofitHelper.getInstance().getServer()
                .codeLogin(mobile, code)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    mView.showSuccess(result.getMsg());
                    if (result.getCode() == 1) {
                        mView.loginSuccess();
                        AccountHelper.getInstance().setUserInfo(result.getData().getUserinfo());
                        SPUtils.getInstance().put("token", result.getData().getUserinfo().getToken());
                        SPUtils.getInstance().put("login", true);
                        SPUtils.getInstance().put("groupId", result.getData().getUserinfo().getGroup_id());
                    }
                }, throwable -> {
                    mView.showSuccess("");
                    mView.loginError();
                });
    }

}
