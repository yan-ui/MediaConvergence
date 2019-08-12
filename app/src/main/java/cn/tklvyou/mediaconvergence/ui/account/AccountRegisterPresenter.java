package cn.tklvyou.mediaconvergence.ui.account;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;
import cn.tklvyou.mediaconvergence.base.BaseResult;
import cn.tklvyou.mediaconvergence.helper.AccountHelper;
import cn.tklvyou.mediaconvergence.model.User;
import cn.tklvyou.mediaconvergence.widget.FrameLayout4Loading;
import io.reactivex.functions.Consumer;


/**
 * 描述
 */

public class AccountRegisterPresenter extends BasePresenter<AccountContract.RegisterView> implements AccountContract.RegisterPresenter {

    @Override
    public void getCaptcha(String mobile, String event) {
        RetrofitHelper.getInstance().getServer()
              .sendSms(mobile,event)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    ToastUtils.showShort(result.getMsg());
                    if(result.getCode() ==1){
                        mView.getCaptchaSuccess();
                    }
                }, throwable -> {
                });
    }

    @Override
    public void register(String mobile, String password, String captcha) {
        mView.showLoading();
        RetrofitHelper.getInstance().getServer()
                .register(mobile, password, captcha)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                    mView.hideLoading();
                    ToastUtils.showShort(result.getMsg());
                    if(result.getCode() == 1){
                        mView.registerSuccess();
                        AccountHelper.getInstance().setUserInfo(result.getData().getUserinfo());
                        SPUtils.getInstance().put("token",result.getData().getUserinfo().getToken());
                        SPUtils.getInstance().put("login",true);
                    }
                }, throwable -> mView.hideLoading());
    }
}
