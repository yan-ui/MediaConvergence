package cn.tklvyou.mediaconvergence.ui.setting;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashSet;

import cn.tklvyou.mediaconvergence.api.RetrofitHelper;
import cn.tklvyou.mediaconvergence.api.RxSchedulers;
import cn.tklvyou.mediaconvergence.base.BasePresenter;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年07月30日17:25
 * @Email: 971613168@qq.com
 */
public class SettingPresenter extends BasePresenter<SettingContract.LogoutView>implements SettingContract.LogoutPresenter  {

    @Override
    public void logout() {
        RetrofitHelper.getInstance().getServer()
                .logout()
                .compose(RxSchedulers.applySchedulers())
                .compose(mView.bindToLife())
                .subscribe(result -> {
                         if(result.getCode() == 1){
                             mView.logoutSuccess();
                         }else {
                             ToastUtils.showShort(result.getMsg());
                         }
                        }, throwable -> {
                            throwable.printStackTrace();
                        }

                );
    }
}
