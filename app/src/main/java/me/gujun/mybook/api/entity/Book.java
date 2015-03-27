package me.gujun.mybook.api.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Book entity.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-16 9:27:09
 */
public class Book {
    private String title;
    private String author;
    private String tag;
    @SerializedName("cover_url")
    private String coverUrl;
    @SerializedName("content_url")
    private String contentUrl;
    private String intro;

    public Book(String title, String author, String tag, String coverUrl, String contentUrl, String intro) {
        this.title = title;
        this.author = author;
        this.tag = tag;
        this.coverUrl = coverUrl;
        this.contentUrl = contentUrl;
        this.intro = intro;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTag() {
        return tag;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public String getIntro() {
        return intro;
    }
}