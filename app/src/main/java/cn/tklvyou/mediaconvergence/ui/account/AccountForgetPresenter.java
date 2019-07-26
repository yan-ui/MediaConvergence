package cn.tklvyou.mediaconvergence.ui.account;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;
import cn.tklvyou.mediaconvergence.base.BaseResult;
import cn.tklvyou.mediaconvergence.model.User;
import io.reactivex.functions.Consumer;


/**
 * 描述
 */

public class AccountForgetPresenter extends BasePresenter<AccountContract.ForgetView> implements AccountContract.ForgetPresenter {

    @Override
    public void getCaptcha(String mobile, String event) {
        RetrofitHelper.getInstance().getServer()
                .sendSms(mobile,event)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(new Consumer<BaseResult<Object>>() {
                    @Override
                    public void accept(BaseResult<Object> result) throws Exception {
                        ToastUtils.showShort(result.getMsg());
                        if(result.getCode() ==1){
                            mView.getCaptchaSuccess();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });
    }

    @Override
    public void resetpwd(String mobile, String newpassword, String captcha) {
        mView.showLoading();
        RetrofitHelper.getInstance().getServer()
                .resetpwd(mobile, newpassword, captcha)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(new Consumer<BaseResult<Object>>() {
                    @Override
                    public void accept(BaseResult<Object> result) throws Exception {
                        mView.hideLoading();
                        ToastUtils.showShort(result.getMsg());
                        if(result.getCode() == 1){
                            mView.resetpwdSuccess();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.hideLoading();
                    }
                });
    }
}
