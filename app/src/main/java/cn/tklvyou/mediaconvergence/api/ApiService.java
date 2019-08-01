package cn.tklvyou.mediaconvergence.api;

import java.util.List;

import cn.tklvyou.mediaconvergence.base.BaseResult;
import cn.tklvyou.mediaconvergence.model.BannerModel;
import cn.tklvyou.mediaconvergence.model.BasePageModel;
import cn.tklvyou.mediaconvergence.model.LotteryResultModel;
import cn.tklvyou.mediaconvergence.model.LotteryModel;
import cn.tklvyou.mediaconvergence.model.NewsBean;
import cn.tklvyou.mediaconvergence.model.PointModel;
import cn.tklvyou.mediaconvergence.model.PointRuleModel;
import cn.tklvyou.mediaconvergence.model.SuixiTvModel;
import cn.tklvyou.mediaconvergence.model.UploadModel;
import cn.tklvyou.mediaconvergence.model.User;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    /**
     * 单文件上传接口
     */
    @Multipart
    @POST("api/common/upload")
    Observable<BaseResult<UploadModel>> upload(@Part MultipartBody.Part file);

    /**
     * 多文件上传接口
     */
    @Multipart
    @POST("api/common/uploads")
    Observable<BaseResult<List<String>>> uploadFiles(@Part List<MultipartBody.Part> files);


    /**
     * 会员中心
     */
    @POST("api/user/index")
    Observable<BaseResult<User.UserinfoBean>> getUser();

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
     * 修改密码
     */
    @POST("api/user/resetpwd")
    Observable<BaseResult<Object>> resetPass(@Query("mobile") String mobile, @Query("newpassword") String password, @Query("captcha") String captcha);

    /**
     * 第三方登录
     *
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
    Observable<BaseResult<BasePageModel<NewsBean>>> getNewList(@Query("module") String module, @Query("p") int p);


    /**
     * 濉溪TV
     */
    @POST("api/article/index")
    Observable<BaseResult<List<SuixiTvModel>>> getSuixiTvNews(@Query("module") String module, @Query("p") int p);


    /**
     * 顶部banner
     */
    @POST("api/banner/index")
    Observable<BaseResult<List<BannerModel>>> getBanner(@Query("module") String module);

    /**
     * 文章详情
     */
    @POST("api/article/detail")
    Observable<BaseResult<NewsBean>> getArticleDetail(@Query("id") int id);

    /**
     * 点赞
     */
    @POST("api/like/add")
    Observable<BaseResult<Object>> addLikeNews(@Query("article_id") int article_id);

    /**
     * 取消点赞
     */
    @POST("api/like/cancel")
    Observable<BaseResult<Object>> cancelLikeNews(@Query("article_id") int article_id);

    /**
     * 发表评论
     */
    @POST("api/comment/add")
    Observable<BaseResult<Object>> addComment(@Query("article_id") int article_id, @Query("detail") String detail);


    /**
     * 发布V视
     */
    @POST("api/article/addv")
    Observable<BaseResult<Object>> publishVShi(@Query("name") String name, @Query("video") String video,
                                               @Query("image") String image, @Query("time") String time);


    /**
     * 发布随手拍
     */
    @POST("api/article/adds")
    Observable<BaseResult<Object>> publishSuiShouPai(@Query("name") String name, @Query("images") String images,
                                                     @Query("video") String video, @Query("image") String image, @Query("time") String time
    );


    /**
     * 商品列表
     */
    @POST("api/goods/index")
    Observable<BaseResult<BasePageModel<PointModel>>> getGoodsPageList(@Query("p") int p);


    /**
     * 商品详情
     */
    @POST("api/goods/detail")
    Observable<BaseResult<PointModel>> getGoodsDetail(@Query("id") int id);

    /**
     * 积分规则
     */
    @POST("api/score/rule")
    Observable<BaseResult<PointRuleModel>> getScoreRule();

    /**
     * 积分抽奖奖品列表
     */
    @POST("api/award/index")
    Observable<BaseResult<LotteryModel>> getLotteryModel();

    /**
     * 抽奖
     */
    @POST("api/award/draw")
    Observable<BaseResult<LotteryResultModel>> startLottery();

    /**
     * 积分商品兑换
     */
    @POST("api/goods/exchange")
    Observable<BaseResult<Object>> exchangeGoods(@Query("id") int id);


    @POST("api/user/profile")
    Observable<BaseResult<Object>> editUserInfo(@Query("avatar") String avatar, @Query("username") String username,
                                                @Query("nickname") String nickname, @Query("bio") String bio
    );
}