package me.gujun.mybook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        db.execSQL("CREATE " + BookshelfTable.TABLE_NAME
                + "("
                + BookshelfTable._ID + " INTEGER PRIMARY KEY,"
                + BookshelfTable.TITLE + " TEXT,"
                + BookshelfTable.AUTHOR + " TEXT,"
                + BookshelfTable.TAG + " TEXT,"
                + BookshelfTable.COVER_URL + " TEXT,"
                + BookshelfTable.CONTENT_URL + " TEXT,"
                + BookshelfTable.INTRO + " TEXT"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BookshelfTable.TABLE_NAME);
        onCreate(db);
    }
}