package me.gujun.mybook.db.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import me.gujun.mybook.db.DatabaseOpenHelper;
import me.gujun.mybook.db.table.BookshelfTable;

import static me.gujun.mybook.db.table.BookshelfTable.AUTHORITY;

/**
 * Provider for bookshelf.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-16 10:51:23
 */
public class BookshelfProvider extends ContentProvider {
    private static final int BOOK = 1; // match code represent the book table
    private static final int BOOK_ID = 2; // match code represent a single record in book table
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, BookshelfTable.TABLE_NAME, BOOK);
        URI_MATCHER.addURI(AUTHORITY, BookshelfTable.TABLE_NAME + "/#", BOOK_ID);
    }

    private DatabaseOpenHelper mDBOpenHelper;

    @Override
    public boolean onCreate() {
        mDBOpenHelper = new DatabaseOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(BookshelfTable.TABLE_NAME);

        if (URI_MATCHER.match(uri) == BOOK) {
        } else if (URI_MATCHER.match(uri) == BOOK_ID) {
            qb.appendWhere(BookshelfTable._ID + "=?");
            selectionArgs = DatabaseUtils.appendSelectionArgs(selectionArgs,
                    new String[]{uri.getLastPathSegment()});
        } else {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mDBOpenHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, null);
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public String getType(Uri uri) {
        if (URI_MATCHER.match(uri) == BOOK) {
            return BookshelfTable.CONTENT_TYPE;
        } else if (URI_MATCHER.match(uri) == BOOK_ID) {
            return BookshelfTable.CONTENT_ITEM_TYPE;
        } else {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (URI_MATCHER.match(uri) != BOOK) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        long rowId = db.insert(BookshelfTable.TABLE_NAME, null, values);

        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(BookshelfTable.CONTENT_ID_URI_BASE, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        int count;

        if (URI_MATCHER.match(uri) == BOOK) {
            count = db.delete(BookshelfTable.TABLE_NAME, selection, selectionArgs);
        } else if (URI_MATCHER.match(uri) == BOOK_ID) {
            String finalWhere = DatabaseUtils.concatenateWhere(BookshelfTable._ID
                    + " = " + ContentUris.parseId(uri), selection);
            count = db.delete(BookshelfTable.TABLE_NAME, finalWhere, selectionArgs);
        } else {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        int count;

        if (URI_MATCHER.match(uri) == BOOK) {
            count = db.update(BookshelfTable.TABLE_NAME, values, selection, selectionArgs);
        } else if (URI_MATCHER.match(uri) == BOOK_ID) {
            String finalWhere = DatabaseUtils.concatenateWhere(BookshelfTable._ID
                    + " = " + ContentUris.parseId(uri), selection);
            count = db.update(BookshelfTable.TABLE_NAME, values, finalWhere, selectionArgs);
        } else {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
}