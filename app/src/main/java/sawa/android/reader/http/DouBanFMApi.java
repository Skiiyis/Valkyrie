package sawa.android.reader.http;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import sawa.android.reader.douban.bean.DouBanFMSongListDetail;
import sawa.android.reader.main.bean.DouBanFMChannel;
import sawa.android.reader.main.bean.DouBanFMSongList;

/**
 * Created by hasee on 2017/3/26.
 */
public class DouBanFMApi {

    public static final String HOST = "https://api.douban.com/v2/fm/";

    private static Retrofit retrofit() {
        return new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static void channelList(Observer<List<DouBanFMChannel>> subscriber) {
        final List<DouBanFMChannel> channels = new ArrayList<>();
        ChannelListService channelListService = retrofit().create(ChannelListService.class);
        channelListService.channelList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<DouBanFMChannelsResponse, List<DouBanFMChannel>>() {
                    @Override
                    public List<DouBanFMChannel> call(DouBanFMChannelsResponse response) {
                        channels.addAll(response.getGroups());
                        return channels;
                    }
                })
                .subscribe(subscriber);
    }

    public static void songList(Observer<List<DouBanFMSongList>> subscriber) {
        final List<DouBanFMSongList> songLists = new ArrayList<>();
        SongListService songListService = retrofit().create(SongListService.class);
        songListService.songList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<DouBanFMSongListResponse, List<DouBanFMSongList>>() {
                    @Override
                    public List<DouBanFMSongList> call(DouBanFMSongListResponse response) {
                        for (DouBanFMSongListResponse.SongList songList : response.getSonglists()) {
                            songLists.addAll(songList.getProgrammes());
                        }
                        return songLists;
                    }
                })
                .subscribe(subscriber);
    }

    public static void songListDetail(String songListId, Observer<DouBanFMSongListDetail> subscriber) {
        SongListDetailService songListDetailService = retrofit().create(SongListDetailService.class);
        songListDetailService.songListDetail(songListId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 豆瓣FM歌单详情
     */
    public interface SongListDetailService {
        @GET("songlist/{id}/detail")
        Observable<DouBanFMSongListDetail> songListDetail(@Path("id") String songListId);
    }

    /**
     * 豆瓣FM歌单列表
     */
    public interface SongListService {
        @GET("songlist/selections_v2")
        Observable<DouBanFMSongListResponse> songList();
    }

    /**
     * 豆瓣FM频道列表
     */
    public interface ChannelListService {
        @GET("app_channels")
        Observable<DouBanFMChannelsResponse> channelList();
    }


    public static class DouBanFMSongListResponse implements Serializable {

        private List<SongList> songlists;

        public List<SongList> getSonglists() {
            return songlists;
        }

        public void setSonglists(List<SongList> songlists) {
            this.songlists = songlists;
        }

        public static class SongList implements Serializable {
            private Long group_id;
            private String group_name;
            private List<DouBanFMSongList> programmes;

            public Long getGroup_id() {
                return group_id;
            }

            public void setGroup_id(Long group_id) {
                this.group_id = group_id;
            }

            public String getGroup_name() {
                return group_name;
            }

            public void setGroup_name(String group_name) {
                this.group_name = group_name;
            }

            public List<DouBanFMSongList> getProgrammes() {
                return programmes;
            }

            public void setProgrammes(List<DouBanFMSongList> programmes) {
                this.programmes = programmes;
            }
        }
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
