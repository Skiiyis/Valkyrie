package sawa.android.reader.http;

import java.io.Serializable;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.http.GET;
import retrofit2.http.Query;
import sawa.android.reader.common.BaseApi;
import sawa.android.reader.main.bean.LiveListItem;
import sawa.android.reader.maxjia.bean.LiveDetail;

/**
 * Created by mc100 on 2017/4/19.
 */

public class MaxJiaLiveApi extends BaseApi {

    protected static String HOST = "http://api.maxjia.com/api/";

    public static Observable<List<LiveListItem>> liveList(int offset, int limit, String gameType) {
        return retrofit(HOST)
                .create(LiveService.class)
                .liveList(offset, limit, gameType)
                .map(new Function<LiveListItemResponse, List<LiveListItem>>() {
                    @Override
                    public List<LiveListItem> apply(LiveListItemResponse response) throws Exception {
                        return response.getResult();
                    }
                });
    }

    public static Observable<List<LiveListItem>> liveList(int offset, int limit, String gameType, String liveType) {
        return retrofit(HOST)
                .create(LiveService.class)
                .liveList(offset, limit, gameType, liveType)
                .map(new Function<LiveListItemResponse, List<LiveListItem>>() {
                    @Override
                    public List<LiveListItem> apply(LiveListItemResponse response) throws Exception {
                        return response.getResult();
                    }
                });
    }

    public interface LiveService {

        /**
         * 直播列表
         *
         * @param offset
         * @param limit
         * @return
         */
        @GET("live/list/")
        Observable<LiveListItemResponse> liveList(
                @Query("offset") int offset,
                @Query("limit") int limit,
                @Query("game_type") String gameType
        );

        @GET("live/list/")
        Observable<LiveListItemResponse> liveList(
                @Query("offset") int offset,
                @Query("limit") int limit,
                @Query("game_type") String gameType,
                @Query("live_type") String liveType
        );

        /**
         * 直播间详情
         *
         * @param live_type
         * @param live_id
         * @param game_type
         * @return
         */
        @GET("list/detail/")
        Observable<LiveDetail> liveDetail(
                @Query("live_type") String live_type,
                @Query("live_id") String live_id,
                @Query("game_type") String game_type
        );
    }

    /**
     * 响应结果
     */
    private static class LiveListItemResponse implements Serializable{

        private String msg;
        private String status;
        private List<LiveListItem> result;

        public List<LiveListItem> getResult() {
            return result;
        }

        public void setResult(List<LiveListItem> result) {
            this.result = result;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
