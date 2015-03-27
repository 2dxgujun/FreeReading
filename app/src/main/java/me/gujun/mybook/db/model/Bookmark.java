package me.gujun.mybook.db.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import me.gujun.mybook.db.table.BookmarkTable;

/**
 * Bookmark database model.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-23 8:54:26
 */
public class Bookmark implements Parcelable {
    public static final Parcelable.Creator<Bookmark> CREATOR = new Parcelable.Creator<Bookmark>() {
        @Override
        public Bookmark createFromParcel(Parcel source) {
            return new Bookmark(source);
        }

        @Override
        public Bookmark[] newArray(int size) {
            return new Bookmark[size];
        }
    };

    private int id;
    private int bookId;
    private int begin;
    private String date;

    public Bookmark() {
    }

    private Bookmark(Parcel in) {
        id = in.readInt();
        bookId = in.readInt();
        begin = in.readInt();
        date = in.readString();
    }

    public Bookmark(int bookId, int begin, String date) {
        this.bookId = bookId;
        this.begin = begin;
        this.date = date;
    }

    public static Bookmark resolve(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(BookmarkTable._ID));
        int bookId = cursor.getInt(cursor.getColumnIndex(BookmarkTable.BOOK_ID));
        int begin = cursor.getInt(cursor.getColumnIndex(BookmarkTable.BEGIN));
        String date = cursor.getString(cursor.getColumnIndex(BookmarkTable.DATE));

        Bookmark bookmark = new Bookmark();
        bookmark.setId(id)
                .setBookId(bookId)
                .setBegin(begin)
                .setDate(date);

        return bookmark;
    }

    public ContentValues assemble() {
        ContentValues values = new ContentValues();
        values.put(BookmarkTable.BOOK_ID, bookId);
        values.put(BookmarkTable.BEGIN, begin);
        values.put(BookmarkTable.DATE, date);

        return values;
    }

    public Bookmark setId(int id) {
        this.id = id;
        return this;
    }

    public Bookmark setBookId(int bookId) {
        this.bookId = bookId;
        return this;
    }

    public Bookmark setBegin(int begin) {
        this.begin = begin;
        return this;
    }

    public Bookmark setDate(String date) {
        this.date = date;
        return this;
    }

    public int getId() {
        return id;
    }

    public int getBookId() {
        return bookId;
    }

    public int getBegin() {
        return begin;
    }

    public String getDate() {
        return date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(bookId);
        dest.writeInt(begin);
        dest.writeString(date);
    }
}