package sawa.android.reader.main.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hasee on 2017/3/26.
 */
public class DouBanFMChannel implements Serializable {

    private List<Channel> chls;
    private Integer group_id;
    private String group_name;

    public static class Channel implements Serializable {
        private String collected;
        private String cover;
        private Integer id;
        private String intro;
        private String name;
        private Integer song_num;
        private String channel_type;

        private transient Integer group_id;
        private transient String group_name;

        public Integer getGroup_id() {
            return group_id;
        }

        public void setGroup_id(Integer group_id) {
            this.group_id = group_id;
        }

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public String getChannel_type() {
            return channel_type;
        }

        public void setChannel_type(String channel_type) {
            this.channel_type = channel_type;
        }

        public String getCollected() {
            return collected;
        }

        public void setCollected(String collected) {
            this.collected = collected;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getSong_num() {
            return song_num;
        }

        public void setSong_num(Integer song_num) {
            this.song_num = song_num;
        }
    }

    public List<Channel> getChls() {
        return chls;
    }

    public void setChls(List<Channel> chls) {
        this.chls = chls;
    }

    public Integer getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Integer group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }
}
