package cn.tklvyou.mediaconvergence.api;

import java.util.List;

import cn.tklvyou.mediaconvergence.base.BaseResult;
import cn.tklvyou.mediaconvergence.model.BannerModel;
import cn.tklvyou.mediaconvergence.model.NewListModel;
import cn.tklvyou.mediaconvergence.model.User;
import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    /**
     * 注册会员
     */
    @POST("api/user/register")
    Observable<BaseResult<User>> register(@Query("mobile") String mobile, @Query("password") String password, @Query("captcha") String captcha);

    /**
     * 会员登录
     */
    @POST("api/user/login")
    Observable<BaseResult<User>> login(@Query("account") String account, @Query("password") String password);

    /**
     * 第三方登录
     * @param platform wechat
     */
    @POST("api/user/third")
    Observable<BaseResult<User>> thirdLogin(@Query("platform") String platform, @Query("code") String code);


    /**
     * 注销登录
     */
    @POST("api/user/logout")
    Observable<BaseResult<Object>> logout();


    /**
     * 重置密码
     */
    @POST("api/user/resetpwd")
    Observable<BaseResult<Object>> resetpwd(@Query("mobile") String mobile, @Query("newpassword") String newpassword, @Query("captcha") String captcha);

    /**
     * 发送验证码
     */
    @POST("api/sms/send")
    Observable<BaseResult<Object>> sendSms(@Query("mobile") String mobile, @Query("event") String event);

    /**
     * 首页频道
     */
    @POST("api/module/index")
    Observable<BaseResult<List<String>>> getHomeChannel();


    /**
     * 内容列表
     */
    @POST("api/article/index")
    Observable<BaseResult<NewListModel>> getNewList(@Query("module") String module, @Query("p") int p);

    /**
     * 顶部banner
     */
    @POST("api/banner/index")
    Observable<BaseResult<List<BannerModel>>> getBanner(@Query("module") String module);


}