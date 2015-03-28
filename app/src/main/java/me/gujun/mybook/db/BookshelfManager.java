package me.gujun.mybook.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import me.gujun.mybook.db.model.Book;
import me.gujun.mybook.db.table.BookshelfTable;

/**
 * Bookshelf database manager.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-23 13:39:30
 */
public class BookshelfManager {
    private static BookshelfManager INSTANCE;

    private ContentResolver mResolver;

    private BookshelfManager(Context context) {
        mResolver = context.getContentResolver();
    }

    public BookshelfManager(ContentResolver resolver) {
        this.mResolver = resolver;
    }

    public static BookshelfManager get(Context context) {
        if (INSTANCE == null) {
            synchronized (BookshelfManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BookshelfManager(context);
                }
            }
        }
        return INSTANCE;
    }

    public static BookshelfManager get(ContentResolver resolver) {
        if (INSTANCE == null) {
            INSTANCE = new BookshelfManager(resolver);
        }
        return INSTANCE;
    }

    public Uri add(Book book) {
        return mResolver.insert(BookshelfTable.CONTENT_URI, book.assemble());
    }

    public List<Book> list() {
        Cursor cursor = mResolver.query(BookshelfTable.CONTENT_URI, null, null, null, null);

        List<Book> bookList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Book book = Book.resolve(cursor);
            bookList.add(book);
        }
        cursor.close();

        return bookList;
    }

    public Book getBookById(int id) {
        Cursor cursor = mResolver.query(ContentUris.withAppendedId(BookshelfTable.CONTENT_URI, id),
                null, null, null, null);
        if (cursor.moveToNext()) {
            return Book.resolve(cursor);
        } else {
            return null;
        }
    }

    public Book getBookByTitle(String title) {
        Cursor cursor = mResolver.query(BookshelfTable.CONTENT_URI,
                null, BookshelfTable.TITLE + "=?", new String[]{title}, null);
        if (cursor.moveToNext()) {
            return Book.resolve(cursor);
        } else {
            return null;
        }
    }

    public int deleteById(Book book) {
        return mResolver.delete(BookshelfTable.CONTENT_ID_URI_BASE, BookshelfTable._ID + "=?",
                new String[]{Integer.toString(book.getId())});
    }
}