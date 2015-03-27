package me.gujun.mybook.db.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import me.gujun.mybook.db.table.BookshelfTable;

/**
 * Book database model.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-23 10:59:20
 */
public class Book implements Parcelable {
    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
    private int id;
    private String title;
    private String author;
    private String tag;
    private String coverUrl;
    private String contentUrl;
    private String intro;

    public Book() {
    }

    public Book(me.gujun.mybook.api.entity.Book book) {
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.tag = book.getTag();
        this.coverUrl = book.getCoverUrl();
        this.contentUrl = book.getContentUrl();
        this.intro = book.getIntro();
    }

    public Book(String title, String author, String tag, String coverUrl,
                String contentUrl, String intro) {
        this.title = title;
        this.author = author;
        this.tag = tag;
        this.coverUrl = coverUrl;
        this.contentUrl = contentUrl;
        this.intro = intro;
    }

    private Book(Parcel in) {
        id = in.readInt();
        title = in.readString();
        author = in.readString();
        tag = in.readString();
        coverUrl = in.readString();
        contentUrl = in.readString();
        intro = in.readString();
    }

    public static Book resolve(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(BookshelfTable._ID));
        String title = cursor.getString(cursor.getColumnIndex(BookshelfTable.TITLE));
        String author = cursor.getString(cursor.getColumnIndex(BookshelfTable.AUTHOR));
        String tag = cursor.getString(cursor.getColumnIndex(BookshelfTable.TAG));
        String coverUrl = cursor.getString(cursor.getColumnIndex(BookshelfTable.COVER_URL));
        String contentUrl = cursor.getString(cursor.getColumnIndex(BookshelfTable.CONTENT_URL));
        String intro = cursor.getString(cursor.getColumnIndex(BookshelfTable.INTRO));

        Book book = new Book();
        book.setId(id)
                .setTitle(title)
                .setAuthor(author)
                .setTag(tag)
                .setCoverUrl(coverUrl)
                .setContentUrl(contentUrl)
                .setIntro(intro);

        return book;
    }

    public ContentValues assemble() {
        ContentValues values = new ContentValues();
        values.put(BookshelfTable.TITLE, title);
        values.put(BookshelfTable.AUTHOR, author);
        values.put(BookshelfTable.TAG, tag);
        values.put(BookshelfTable.COVER_URL, coverUrl);
        values.put(BookshelfTable.CONTENT_URL, contentUrl);
        values.put(BookshelfTable.INTRO, intro);

        return values;
    }

    public int getId() {
        return id;
    }

    public Book setId(int id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Book setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public Book setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public Book setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public Book setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
        return this;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public Book setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
        return this;
    }

    public String getIntro() {
        return intro;
    }

    public Book setIntro(String intro) {
        this.intro = intro;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(tag);
        dest.writeString(coverUrl);
        dest.writeString(contentUrl);
        dest.writeString(intro);
    }
}