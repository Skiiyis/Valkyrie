package sawa.android.reader.main.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hasee on 2017/3/11.
 */
public class ZhiHuNewsLatestResponse implements Serializable {
    private String date;
    private List<Story> stories;
    private List<Story> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    public List<Story> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<Story> top_stories) {
        this.top_stories = top_stories;
    }

    public static class Story {
        private String ga_prefix;
        private Long id;
        private String title;
        private Long type;
        private List<String> images;
        private String image;

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
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

        public Long getType() {
            return type;
        }

        public void setType(Long type) {
            this.type = type;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
