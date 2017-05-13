package com.tobi_ace.popularmovies.models;

/**
 * Created by abdulgafar on 5/1/17.
 */

public class Trailer {

    static final String YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%1$s";
    static final String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%1$s/hqdefault.jpg";
    private static final String YOUTUBE = "Youtube";
    private String id;
    private String name;
    private String site;
    private String key;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public String getKey() {
        return key;
    }

    public Trailer(String id, String name, String site, String key) {
        this.id = id;
        this.name = name;
        this.site = site;
        this.key = key;
    }

    public String getUrl() {
        if (this.getSite().equalsIgnoreCase(YOUTUBE)) {
            return String.format(YOUTUBE_VIDEO_URL, this.getKey());

        }
        return null;
    }

    public String getThumbnailUrl() {
        if (this.getSite().equalsIgnoreCase(YOUTUBE)) {
            return String.format(YOUTUBE_THUMBNAIL_URL, this.getKey());

        }
        return null;
    }


}
