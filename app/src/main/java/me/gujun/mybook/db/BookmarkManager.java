package me.gujun.mybook.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import me.gujun.mybook.db.model.Book;
import me.gujun.mybook.db.model.Bookmark;
import me.gujun.mybook.db.table.BookmarkTable;

/**
 * Bookmark database manager.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-23 13:39:21
 */
public class BookmarkManager {
    private static BookmarkManager INSTANCE;

    private ContentResolver mResolver;

    private BookmarkManager(Context context) {
        this(context.getContentResolver());
    }

    private BookmarkManager(ContentResolver resolver) {
        mResolver = resolver;
    }

    public static BookmarkManager get(Context context) {
        if (INSTANCE == null) {
            synchronized (BookmarkManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BookmarkManager(context);
                }
            }
        }
        return INSTANCE;
    }

    public static BookmarkManager get(ContentResolver resolver) {
        if (INSTANCE == null) {
            INSTANCE = new BookmarkManager(resolver);
        }
        return INSTANCE;
    }

    public Uri add(Bookmark bookmark) {
        return mResolver.insert(BookmarkTable.CONTENT_URI, bookmark.assemble());
    }

    public List<Bookmark> list() {
        Cursor cursor = mResolver.query(BookmarkTable.CONTENT_URI, null, null, null, null);

        List<Bookmark> bookmarkList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Bookmark bookmark = Bookmark.resolve(cursor);
            bookmarkList.add(bookmark);
        }
        cursor.close();

        return bookmarkList;
    }

    public List<Bookmark> listByBookId(Book book) {
        Cursor cursor = mResolver.query(BookmarkTable.CONTENT_URI, null, BookmarkTable.BOOK_ID + "=?",
                new String[]{Integer.toString(book.getId())}, null);

        List<Bookmark> bookmarkList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Bookmark bookmark = Bookmark.resolve(cursor);
            bookmarkList.add(bookmark);
        }
        cursor.close();

        return bookmarkList;
    }

    public int deleteByBookId(Book book) {
        return mResolver.delete(BookmarkTable.CONTENT_URI, BookmarkTable.BOOK_ID + "=?",
                new String[]{Integer.toString(book.getId())});
    }

    public int deleteById(Bookmark bookmark) {
        return mResolver.delete(ContentUris.withAppendedId(BookmarkTable.CONTENT_ID_URI_BASE,
                bookmark.getId()), null, null);
    }

    public int update(Bookmark bookmark) {
        return mResolver.update(BookmarkTable.CONTENT_URI, bookmark.assemble(), BookmarkTable._ID + "=?",
                new String[]{Integer.toString(bookmark.getId())});
    }
}