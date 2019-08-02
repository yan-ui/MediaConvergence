package cn.tklvyou.mediaconvergence.ui.setting;

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
    public void setAppIcon(int appIcon) {
        mView.showAppIcon(appIcon);
    }

    @Override
    public void setAppVersionName(String name) {
        mView.showAppVersionName(name);
    }


   /* @Override
    public void showAppIcon(int appIcon) {
        mView.setAppIcon(appIcon);
    }

    @Override
    public void showAppVersionName(String name) {
        mView.setAppVersionName(name);
    }*/
}
