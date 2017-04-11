package sawa.android.reader.main.bean;

import java.io.Serializable;

/**
 * Created by mc100 on 2017/4/11.
 */
public class DouBanFMSongList implements Serializable {
    private String cover;
    private String description;
    private Long id;
    private String title;
    private Long collected_count;
    private Integer songs_count;
    private Creator creator;

    public static class Creator implements Serializable {
        private Long id;
        private String name;
        private String picture;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCollected_count() {
        return collected_count;
    }

    public void setCollected_count(Long collected_count) {
        this.collected_count = collected_count;
    }

    public Integer getSongs_count() {
        return songs_count;
    }

    public void setSongs_count(Integer songs_count) {
        this.songs_count = songs_count;
    }
}
