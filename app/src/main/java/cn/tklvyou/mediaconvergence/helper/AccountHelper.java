package cn.tklvyou.mediaconvergence.helper;


import cn.tklvyou.mediaconvergence.model.User;
import cn.tklvyou.mediaconvergence.utils.CommonUtil;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年07月30日16:05
 * @Email: 971613168@qq.com
 */
public class AccountHelper {
    private User.UserinfoBean userInfo;

    private static class SingletonInstance {
        private static final AccountHelper INSTANCE = new AccountHelper();
    }

    public static AccountHelper getInstance() {
        return SingletonInstance.INSTANCE;
    }


    public User.UserinfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User.UserinfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public boolean isLogin() {
        return userInfo != null;
    }

    public String getPhone() {
        if (userInfo != null) {
            return CommonUtil.getNotNullValue(userInfo.getMobile());
        }
        return "";
    }
}
