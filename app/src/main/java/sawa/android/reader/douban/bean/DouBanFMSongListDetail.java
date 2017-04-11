package sawa.android.reader.douban.bean;

import java.io.Serializable;
import java.util.List;

import sawa.android.reader.main.bean.DouBanFMSongList;

/**
 * Created by mc100 on 2017/4/11.
 */

public class DouBanFMSongListDetail extends DouBanFMSongList implements Serializable {
    private List<Song> songs;

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public static class Song implements Serializable {
        private String picture;
        private String url;
        private String title;
        private String artist;
        private List<Singer> singers;

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<Singer> getSingers() {
            return singers;
        }

        public void setSingers(List<Singer> singers) {
            this.singers = singers;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }
    }

    public static class Singer implements Serializable {
        private String name;
        private String id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
