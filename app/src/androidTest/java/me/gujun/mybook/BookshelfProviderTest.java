package me.gujun.mybook;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;
import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.Assert;

import me.gujun.mybook.api.entity.Book;
import me.gujun.mybook.db.BookshelfProvider;
import me.gujun.mybook.db.BookshelfTable;

/**
 * Bookshelf provider test case.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-19 9:11:59
 */
public class BookshelfProviderTest extends ProviderTestCase2<BookshelfProvider> {
    private MockContentResolver mMockResolver;

    public BookshelfProviderTest() {
        super(BookshelfProvider.class, BookshelfProvider.AUTHORITY);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mMockResolver = getMockContentResolver();
    }

    @SmallTest
    public void testUriAndGetType() throws Exception {
        String mimeType = mMockResolver.getType(BookshelfTable.CONTENT_URI);
        Assert.assertEquals(BookshelfTable.CONTENT_TYPE, mimeType);

        Uri bookshelfItemUri = ContentUris.withAppendedId(BookshelfTable.CONTENT_ID_URI_BASE, 1);
        mimeType = mMockResolver.getType(bookshelfItemUri);
        Assert.assertEquals(BookshelfTable.CONTENT_ITEM_TYPE, mimeType);
    }

    /**
     * 每一个测试用例执行完后，对应的测试数据库会自动清空
     *
     * @throws Exception
     */
    @SmallTest
    public void testInsert() throws Exception {
        Book book = new Book("三体", "刘慈欣", "科幻", "http://github-2dxgujun.qiniudn.com/三体I.png",
                "http://github-2dxgujun.qiniudn.com/三体I.txt", "给岁月以文明，给时光以生命");

        ContentValues values = book.getContentValues();
        Uri insertedUri = mMockResolver.insert(BookshelfTable.CONTENT_URI, values);
        Assert.assertNotNull(insertedUri);

        Cursor cursor = mMockResolver.query(BookshelfTable.CONTENT_URI, null, null, null, null);
        Assert.assertEquals(1, cursor.getCount());
        Assert.assertTrue(cursor.moveToFirst());

        long id = cursor.getLong(cursor.getColumnIndex(BookshelfTable._ID));
        String title = cursor.getString(cursor.getColumnIndex(BookshelfTable.TITLE));
        String author = cursor.getString(cursor.getColumnIndex(BookshelfTable.AUTHOR));
        String tag = cursor.getString(cursor.getColumnIndex(BookshelfTable.TAG));
        String coverUrl = cursor.getString(cursor.getColumnIndex(BookshelfTable.COVER_URL));
        String contentUrl = cursor.getString(cursor.getColumnIndex(BookshelfTable.CONTENT_URL));
        String intro = cursor.getString(cursor.getColumnIndex(BookshelfTable.INTRO));

        Assert.assertEquals(id, 1);
        Assert.assertEquals(title, book.getTitle());
        Assert.assertEquals(author, book.getAuthor());
        Assert.assertEquals(tag, book.getTag());
        Assert.assertEquals(coverUrl, book.getCoverUrl());
        Assert.assertEquals(contentUrl, book.getContentUrl());
        Assert.assertEquals(intro, book.getIntro());
    }
}
