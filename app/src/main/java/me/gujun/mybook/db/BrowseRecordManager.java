package me.gujun.mybook.db;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import me.gujun.mybook.db.model.BrowseRecord;
import me.gujun.mybook.db.table.BrowseRecordTable;

/**
 * Browse record manager.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-26 13:19:03
 */
public class BrowseRecordManager {
    private static BrowseRecordManager INSTANCE;

    private ContentResolver mResolver;

    private BrowseRecordManager(Context context) {
        this(context.getContentResolver());
    }

    private BrowseRecordManager(ContentResolver resolver) {
        mResolver = resolver;
    }

    public static BrowseRecordManager get(Context context) {
        if (INSTANCE == null) {
            synchronized (BrowseRecordManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BrowseRecordManager(context);
                }
            }
        }
        return INSTANCE;
    }

    public Uri add(BrowseRecord record) {
        return mResolver.insert(BrowseRecordTable.CONTENT_URI, record.assemble());
    }

    public List<BrowseRecord> list() {
        Cursor cursor = mResolver.query(BrowseRecordTable.CONTENT_URI, null, null, null, null);

        List<BrowseRecord> recordList = new ArrayList<>();
        while (cursor.moveToNext()) {
            BrowseRecord record = BrowseRecord.resolve(cursor);
            recordList.add(record);
        }
        cursor.close();

        return recordList;
    }

    public int deleteById(BrowseRecord record) {
        return mResolver.delete(BrowseRecordTable.CONTENT_URI, BrowseRecordTable._ID + "=?",
                new String[]{Integer.toString(record.getId())});
    }
}