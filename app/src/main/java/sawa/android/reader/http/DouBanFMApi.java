package sawa.android.reader.http;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import sawa.android.reader.main.bean.DouBanFMChannel;

/**
 * Created by hasee on 2017/3/26.
 */
public class DouBanFMApi {

    public static final String HOST = "https://api.douban.com/v2/fm/";

    public static void channelList(Observer<List<DouBanFMChannel>> subscriber) {
        final List<DouBanFMChannel> channels = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        ChannelListService channelListService = retrofit.create(ChannelListService.class);
        channelListService.channelList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                /*.map(new Func1<DouBanFMChannel, List<DouBanFMChannel.Channel>>() {
                    @Override
                    public List<DouBanFMChannel.Channel> call(DouBanFMChannel response) {
                        for (DouBanFMChannel.Channel channel : response.getChls()) {
                            channel.setGroup_name(response.getGroup_name());
                        }
                        channels.addAll(response.getChls());
                        return channels;
                    }
                });*/
                .map(new Func1<DouBanFMChannelsResponse, List<DouBanFMChannel>>() {
                    @Override
                    public List<DouBanFMChannel> call(DouBanFMChannelsResponse response) {
                        channels.addAll(response.getGroups());
                        return channels;
                    }
                })
                .subscribe(subscriber);
    }

    public interface ChannelListService {
        @GET("app_channels")
        Observable<DouBanFMChannelsResponse> channelList();
    }

    public static class DouBanFMChannelsResponse implements Serializable {
        private List<DouBanFMChannel> groups;

        public List<DouBanFMChannel> getGroups() {
            return groups;
        }

        public void setGroups(List<DouBanFMChannel> groups) {
            this.groups = groups;
        }
    }
}
