package me.gujun.mybook.db;

import android.content.ContentUris;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;

import junit.framework.Assert;

import java.util.List;

import me.gujun.mybook.db.model.Bookmark;
import me.gujun.mybook.db.provider.BookmarkProvider;
import me.gujun.mybook.db.table.BookmarkTable;
import me.gujun.mybook.util.TimeUtils;

import static me.gujun.mybook.db.table.BookmarkTable.AUTHORITY;

/**
 * Bookmark provider test case.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-23 14:03:44
 */
public class BookmarkManagerTest extends ProviderTestCase2<BookmarkProvider> {
    private static final String TAG = BookmarkManager.class.getSimpleName();
    private MockContentResolver mMockResolver;
    private BookmarkManager mBookmarkManager;

    public BookmarkManagerTest() {
        super(BookmarkProvider.class, AUTHORITY);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mMockResolver = getMockContentResolver();
        Assert.assertNotNull(mMockResolver);

        mBookmarkManager = BookmarkManager.get(mMockResolver);
        Assert.assertNotNull(mBookmarkManager);
    }

    @MediumTest
    public void testUriAndGetType() throws Exception {
        String mimeType = mMockResolver.getType(BookmarkTable.CONTENT_URI);
        Assert.assertEquals(BookmarkTable.CONTENT_TYPE, mimeType);

        Uri itemUri = ContentUris.withAppendedId(BookmarkTable.CONTENT_ID_URI_BASE, 1);
        mimeType = mMockResolver.getType(itemUri);
        Assert.assertEquals(BookmarkTable.CONTENT_ITEM_TYPE, mimeType);
    }

    public void listBookmarks() {
        List<Bookmark> bookmarkList = mBookmarkManager.list();
        Assert.assertNotNull(bookmarkList);
        Log.d(TAG, "List from bookmark.db: " + bookmarkList.toString());
    }

    @MediumTest
    public void testInsert() {
        Uri uri;
        uri = mBookmarkManager.add(new Bookmark(1, 0, TimeUtils.getCurrentTimeFormated()));
        Assert.assertNotNull(uri);
        uri = mBookmarkManager.add(new Bookmark(2, 0, TimeUtils.getCurrentTimeFormated()));
        Assert.assertNotNull(uri);

        listBookmarks();
    }

    /*@MediumTest
    public void testTrigger() {
        Uri uri;
        uri = mBookmarkManager.add(new Bookmark(15, 0, TimeUtils.getCurrentTimeFormated()));
        Assert.assertNotNull(uri);
        uri = mBookmarkManager.add(new Bookmark(100, 0, TimeUtils.getCurrentTimeFormated()));
        Assert.assertNotNull(uri);
    }

    @MediumTest
    public void testUpdate() {
        List<Bookmark> bookmarkList = mBookmarkManager.list();
        Bookmark bookmark = bookmarkList.get(0);
        bookmark.setBegin(200);
        int row = mBookmarkManager.update(bookmark);
        Assert.assertEquals(row, 1);
    }

    @MediumTest
    public void testDelete() {
        List<Bookmark> bookmarkList = mBookmarkManager.list();
        Bookmark bookmark = bookmarkList.get(1);
        int row = mBookmarkManager.deleteById(bookmark);
        Assert.assertEquals(row, 1);
    }*/
}