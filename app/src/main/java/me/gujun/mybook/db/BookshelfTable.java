package me.gujun.mybook.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Bookshelf table.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-16 8:56:17
 */
public interface BookshelfTable extends BaseColumns {
    Uri CONTENT_URI = Uri.parse("content://" + BookshelfProvider.AUTHORITY + "/bookshelf");
    Uri CONTENT_ID_URI_BASE = Uri.parse("content://" + BookshelfProvider.AUTHORITY + "/bookshelf/");

    String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.bookshelf";
    String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.bookshelf";

    String TABLE_NAME = "bookshelf";

    //region Columns
    String TITLE = "title";
    String AUTHOR = "author";
    String TAG = "tag";
    String COVER_URL = "cover_url";
    String CONTENT_URL = "content_url";
    String INTRO = "intro";
    //endregion
}