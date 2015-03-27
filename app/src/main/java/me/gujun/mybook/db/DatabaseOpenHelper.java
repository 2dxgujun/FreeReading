package me.gujun.mybook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import me.gujun.mybook.db.table.BookmarkTable;
import me.gujun.mybook.db.table.BookshelfTable;
import me.gujun.mybook.db.table.BrowseRecordTable;

/**
 * Database open helper for book table.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-16 9:00:57
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "mybook.db";

    public DatabaseOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + BookshelfTable.TABLE_NAME
                + "("
                + BookshelfTable._ID + " INTEGER PRIMARY KEY,"
                + BookshelfTable.TITLE + " TEXT,"
                + BookshelfTable.AUTHOR + " TEXT,"
                + BookshelfTable.TAG + " TEXT,"
                + BookshelfTable.COVER_URL + " TEXT,"
                + BookshelfTable.CONTENT_URL + " TEXT,"
                + BookshelfTable.INTRO + " TEXT,"
                + "FOREIGN KEY" + "(" + BookshelfTable._ID + ")" + " REFERENCES " + BookmarkTable.TABLE_NAME + "(" + BookmarkTable.BOOK_ID + ")"
                + ");");

        db.execSQL("CREATE TABLE " + BookmarkTable.TABLE_NAME
                + "("
                + BookmarkTable._ID + " INTEGER PRIMARY KEY,"
                + BookmarkTable.BOOK_ID + " INTEGER NOT NULL,"
                + BookmarkTable.BEGIN + " INTEGER,"
                + BookmarkTable.DATE + " TEXT"
                + ");");

        db.execSQL("CREATE TRIGGER fk_bmbs_bookid BEFORE INSERT ON " + BookmarkTable.TABLE_NAME + " "
                + "FOR EACH ROW "
                + "BEGIN "
                + "SELECT CASE WHEN ((SELECT " + BookshelfTable._ID + " FROM " + BookshelfTable.TABLE_NAME + " WHERE " + BookshelfTable._ID + "=new." + BookmarkTable.BOOK_ID + ") IS NULL)"
                + "THEN RAISE (ABORT, 'Foreign Key Violation')"
                + "END;"
                + "END;");

        db.execSQL("CREATE TABLE " + BrowseRecordTable.TABLE_NAME
                + "("
                + BrowseRecordTable._ID + " INTEGER PRIMARY KEY,"
                + BrowseRecordTable.FILE_NAME + " TEXT,"
                + BrowseRecordTable.FILE_PATH + " TEXT,"
                + BrowseRecordTable.DATE + " TEXT"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BookshelfTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BookmarkTable.TABLE_NAME);
        db.execSQL("DROP TRIGGER IF EXISTS fk_bmbs_bookid");
        db.execSQL("DROP TABLE IF EXISTS " + BrowseRecordTable.TABLE_NAME);
        onCreate(db);
    }
}