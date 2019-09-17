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

public class AccountLoginPresenter extends BasePresenter<AccountContract.LoginView> implements AccountContract.LoginPresenter {

    @Override
    public void thirdLogin(String platform, String code) {
        mView.showLoading();
        RetrofitHelper.getInstance().getServer()
                .thirdLogin(platform, code)
                .compose(RxSchedulers.applySchedulers())
                .subscribe(result -> {
                            mView.showSuccess(result.getMsg());
                            if (result.getCode() == 1) {
                                ToastUtils.showShort(result.getMsg());
                                mView.loginSuccess();
                                AccountHelper.getInstance().setUserInfo(result.getData().getUserinfo());
                                SPUtils.getInstance().put("token", result.getData().getUserinfo().getToken());
                                SPUtils.getInstance().put("login", true);
                                SPUtils.getInstance().put("addv", result.getData().getUserinfo().isAddv());
                                SPUtils.getInstance().put("delv", result.getData().getUserinfo().isDelv());
                                SPUtils.getInstance().put("dels", result.getData().getUserinfo().isDels());
                            } else if (result.getCode() == 5) {
                                mView.bindMobile(result.getData().getThird_id());
                            }
                        }, throwable -> {
                            mView.showSuccess("");
                            mView.loginError();
                        }
                );
    }

}
