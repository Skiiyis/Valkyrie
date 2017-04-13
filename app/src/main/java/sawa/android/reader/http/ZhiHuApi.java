package sawa.android.reader.http;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import sawa.android.reader.main.bean.ZhiHuNewsLatestResponse;
import sawa.android.reader.zhihu.bean.ZhiHuNewsDetailResponse;

/**
 * Created by hasee on 2017/3/11.
 */
public class ZhiHuApi {

    public static final String HOST = "http://news-at.zhihu.com/api/4/news/";

    private static Retrofit retrofit() {
        return new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static Observable<ZhiHuNewsLatestResponse> newsLatest() {
        return retrofit().create(NewsLatestService.class).newsLatest();
    }

    public static Observable<ZhiHuNewsDetailResponse> newsDetail(String id) {
        return retrofit().create(NewsDetailService.class).newsDetail(id);
    }

    //http://news-at.zhihu.com/api/4/news/3892357
    public interface NewsDetailService {
        @GET("{id}")
        Observable<ZhiHuNewsDetailResponse> newsDetail(@Path("id") String id);
    }

    public interface NewsLatestService {
        @GET("latest")
        Observable<ZhiHuNewsLatestResponse> newsLatest();
    }
}
