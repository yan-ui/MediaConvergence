package cn.tklvyou.mediaconvergence.ui.setting;

import com.blankj.utilcode.util.ToastUtils;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月02日14:14
 * @Email: 971613168@qq.com
 */
public class AboutUsPresenter extends BasePresenter<AboutUsContract.View> implements AboutUsContract.AboutPresenter {

    @Override
    public void getSystemConfig() {
        RetrofitHelper.getInstance().getServer()
                .getSystemConfig()
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                            if(result.getCode() == 1){
                                mView.setSystemConfig(result.getData());
                            }else {
                                ToastUtils.showShort(result.getMsg());
                            }
                        }, throwable -> {
                            throwable.printStackTrace();
                        }

                );
    }
}
