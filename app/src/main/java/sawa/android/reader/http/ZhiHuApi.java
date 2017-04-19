package sawa.android.reader.http;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import sawa.android.reader.common.BaseApi;
import sawa.android.reader.main.bean.ZhiHuNewsLatestResponse;
import sawa.android.reader.zhihu.bean.ZhiHuNewsDetailResponse;

/**
 * Created by hasee on 2017/3/11.
 */
public class ZhiHuApi extends BaseApi {

    protected static String HOST = "http://news-at.zhihu.com/api/4/news/";

    public static Observable<ZhiHuNewsLatestResponse> newsLatest() {
        return retrofit(HOST)
                .create(NewsService.class)
                .newsLatest();
    }

    public static Observable<ZhiHuNewsDetailResponse> newsDetail(String id) {
        return retrofit(HOST)
                .create(NewsService.class)
                .newsDetail(id);
    }

    //http://news-at.zhihu.com/api/4/news/3892357
    public interface NewsService {

        /**
         * 知乎最新新闻
         * @return
         */
        @GET("latest")
        Observable<ZhiHuNewsLatestResponse> newsLatest();

        /**
         * 详情
         * @param id
         * @return
         */
        @GET("{id}")
        Observable<ZhiHuNewsDetailResponse> newsDetail(@Path("id") String id);
    }
}
