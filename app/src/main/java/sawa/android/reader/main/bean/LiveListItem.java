package sawa.android.reader.main.bean;

import java.io.Serializable;

/**
 * Created by mc100 on 2017/4/19.
 */

public class LiveListItem implements Serializable {
    private String game_type;
    private String live_type;
    private String live_userimg;

    private String live_id;
    private String live_img;
    private String live_title;
    private String live_nickname;
    private String live_online;
    private UrlInfo url_info;

    public static class UrlInfo implements Serializable {
        private String Referer;
        private String User_Agent;
        private String url;

        public String getReferer() {
            return Referer;
        }

        public String getUser_Agent() {
            return User_Agent;
        }

        public String getUrl() {
            return url;
        }

        public void setReferer(String referer) {
            Referer = referer;
        }

        public void setUser_Agent(String user_Agent) {
            User_Agent = user_Agent;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public void setGame_type(String game_type) {
        this.game_type = game_type;
    }

    public void setLive_type(String live_type) {
        this.live_type = live_type;
    }

    public void setLive_userimg(String live_userimg) {
        this.live_userimg = live_userimg;
    }

    public void setLive_id(String live_id) {
        this.live_id = live_id;
    }

    public void setLive_img(String live_img) {
        this.live_img = live_img;
    }

    public void setLive_title(String live_title) {
        this.live_title = live_title;
    }

    public void setLive_nickname(String live_nickname) {
        this.live_nickname = live_nickname;
    }

    public void setLive_online(String live_online) {
        this.live_online = live_online;
    }

    public void setUrl_info(UrlInfo url_info) {
        this.url_info = url_info;
    }

    public String getGame_type() {
        return game_type;
    }

    public String getLive_type() {
        return live_type;
    }

    public String getLive_id() {
        return live_id;
    }

    public String getLive_img() {
        return live_img;
    }

    public String getLive_title() {
        return live_title;
    }

    public String getLive_nickname() {
        return live_nickname;
    }

    public String getLive_online() {
        return live_online;
    }

    public UrlInfo getUrl_info() {
        return url_info;
    }

    public String getLive_userimg() {
        return live_userimg;
    }
}
