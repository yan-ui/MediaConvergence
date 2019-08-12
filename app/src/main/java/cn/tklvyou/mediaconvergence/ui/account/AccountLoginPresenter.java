package cn.tklvyou.mediaconvergence.ui.account;

import android.os.Handler;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;
import cn.tklvyou.mediaconvergence.base.BaseResult;
import cn.tklvyou.mediaconvergence.helper.AccountHelper;
import cn.tklvyou.mediaconvergence.model.User;
import io.reactivex.functions.Consumer;


/**
 * 描述
 */

public class AccountLoginPresenter extends BasePresenter<AccountContract.LoginView> implements AccountContract.LoginPresenter {

    @Override
    public void login(final String name, final String password) {
        mView.showLoading();
        RetrofitHelper.getInstance().getServer()
                .login(name, password)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    mView.hideLoading();
                    ToastUtils.showShort(result.getMsg());
                    if (result.getCode() == 1) {
                        mView.loginSuccess();
                        AccountHelper.getInstance().setUserInfo(result.getData().getUserinfo());
                        SPUtils.getInstance().put("token", result.getData().getUserinfo().getToken());
                        SPUtils.getInstance().put("login", true);
                    } else {
                        mView.loginError();
                    }
                }, throwable -> mView.hideLoading());
    }

    @Override
    public void thirdLogin(String platform, String code) {
        mView.showLoading();
        RetrofitHelper.getInstance().getServer()
                .thirdLogin(platform, code)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    LogUtils.e(new Gson().toJson(result));
                            mView.hideLoading();
                            if (result.getCode() == 1) {
                                ToastUtils.showShort(result.getMsg());
                                mView.loginSuccess();
                                AccountHelper.getInstance().setUserInfo(result.getData().getUserinfo());
                                SPUtils.getInstance().put("token", result.getData().getUserinfo().getToken());
                                SPUtils.getInstance().put("login", true);
                            } else if (result.getCode() == 5) {
                                mView.bindMobile(result.getData().getThird_id());
                            } else {
                                ToastUtils.showShort(result.getMsg());
                                mView.loginError();
                            }
                        }, throwable -> {
                            mView.hideLoading();
                            throwable.printStackTrace();
                        }
                );
    }

}
