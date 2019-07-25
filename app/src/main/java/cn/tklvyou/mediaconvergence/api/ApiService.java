package cn.tklvyou.mediaconvergence.api;


import java.util.List;

import cn.tklvyou.mediaconvergence.base.BaseResult;
import cn.tklvyou.mediaconvergence.model.BookModel;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
 
    /**
     * 广告banner
     * @return
     */
    @GET("/ace-app/bannerInfo/{id}")
    Observable<BaseResult<List<String>>> getBannerInfo(@Path("id") String id);
    /**
     * 测试数据接口
     * @param q
     * @param tag
     * @param start
     * @param end
     * @return
     */
    @GET("book/search")
    Observable<BookModel> getBooks(@Query("q") String q,
                                   @Query("tag") String tag,
                                   @Query("start") String start,
                                   @Query("end") String end);
 
 
}