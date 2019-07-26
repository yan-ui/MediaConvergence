package cn.tklvyou.mediaconvergence.ui.account;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;
import cn.tklvyou.mediaconvergence.base.BaseResult;
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
    public void register(String mobile, String password, String captcha) {
        mView.showLoading();
        RetrofitHelper.getInstance().getServer()
                .register(mobile, password, captcha)
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(new Consumer<BaseResult<User>>() {
                    @Override
                    public void accept(BaseResult<User> result) throws Exception {
                        mView.hideLoading();
                        ToastUtils.showShort(result.getMsg());
                        if(result.getCode() == 1){
                            mView.registerSuccess();
                            SPUtils.getInstance().put("token",result.getData().getUserinfo().getToken());
                            SPUtils.getInstance().put("login",true);
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
