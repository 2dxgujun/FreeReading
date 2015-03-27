package me.gujun.mybook.db.table;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Bookmark table.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-23 8:47:14
 */
public interface BookmarkTable extends BaseColumns {
    String AUTHORITY = "me.gujun.mybook.bookmark";
    Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/bookmark");
    Uri CONTENT_ID_URI_BASE = Uri.parse("content://" + AUTHORITY + "/bookmark/");

    String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.bookmark";
    String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.bookmark";

    String TABLE_NAME = "bookmark";

    //region Columns
    String BOOK_ID = "book_id";
    String BEGIN = "begin";
    String DATE = "date";
    //endregion
}