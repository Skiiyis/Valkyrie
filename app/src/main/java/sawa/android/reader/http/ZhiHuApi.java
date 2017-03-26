package sawa.android.reader.http;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sawa.android.reader.main.bean.ZhiHuNewsLatestResponse;
import sawa.android.reader.zhihu.bean.ZhiHuNewsDetailResponse;

/**
 * Created by hasee on 2017/3/11.
 */
public class ZhiHuApi {

    public static final String HOST = "http://news-at.zhihu.com/api/4/news/";

    public static void newsLatest(Observer<ZhiHuNewsLatestResponse> subscriber) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        NewsLatestService newsLatestService = retrofit.create(NewsLatestService.class);
        newsLatestService.getNewsLatest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public static void newsDetail(Observer<ZhiHuNewsDetailResponse> subscriber, String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        NewsDetailService newsLatestService = retrofit.create(NewsDetailService.class);
        newsLatestService.getNewsLatest(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //http://news-at.zhihu.com/api/4/news/3892357
    public interface NewsDetailService {
        @GET("{id}")
        Observable<ZhiHuNewsDetailResponse> getNewsLatest(@Path("id") String id);
    }

    public interface NewsLatestService {
        @GET("latest")
        Observable<ZhiHuNewsLatestResponse> getNewsLatest();
    }
}
