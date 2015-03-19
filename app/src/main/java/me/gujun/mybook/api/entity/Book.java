package me.gujun.mybook.api.entity;

import android.content.ContentValues;

import me.gujun.mybook.db.BookshelfTable;

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
    private String cover_url;
    private String content_url;
    private String intro;

    public Book(String title, String author, String tag, String cover_url, String content_url, String intro) {
        this.title = title;
        this.author = author;
        this.tag = tag;
        this.cover_url = cover_url;
        this.content_url = content_url;
        this.intro = intro;
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(BookshelfTable.TITLE, title);
        values.put(BookshelfTable.AUTHOR, author);
        values.put(BookshelfTable.TAG, tag);
        values.put(BookshelfTable.COVER_URL, cover_url);
        values.put(BookshelfTable.CONTENT_URL, content_url);
        values.put(BookshelfTable.INTRO, intro);

        return values;
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
        return cover_url;
    }

    public String getContentUrl() {
        return content_url;
    }

    public String getIntro() {
        return intro;
    }
}