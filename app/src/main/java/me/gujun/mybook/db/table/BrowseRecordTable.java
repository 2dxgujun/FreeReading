package me.gujun.mybook.db.table;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Browse record table.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-26 12:54:49
 */
public interface BrowseRecordTable extends BaseColumns {
    String AUTHORITY = "me.gujun.mybook.browse_record";
    Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/browse_record");
    Uri CONTENT_ID_URI_BASE = Uri.parse("content://" + AUTHORITY + "/browse_record/");

    String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.browse_record";
    String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.browse_record";

    String TABLE_NAME = "browse_record";

    //region Fields
    String FILE_NAME = "file_name";
    String FILE_PATH = "file_path";
    String DATE = "date";
    //endregion
}