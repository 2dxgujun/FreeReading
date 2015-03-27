package me.gujun.mybook.db;

import android.content.ContentUris;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

import me.gujun.mybook.api.BookstoreClient;
import me.gujun.mybook.api.entity.BookList;
import me.gujun.mybook.db.model.Book;
import me.gujun.mybook.db.provider.BookshelfProvider;
import me.gujun.mybook.db.table.BookshelfTable;

import static me.gujun.mybook.db.table.BookshelfTable.AUTHORITY;

/**
 * Bookshelf provider test case.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-23 14:10:31
 */
public class BookshelfManagerTest extends ProviderTestCase2<BookshelfProvider> {
    private static final String TAG = BookshelfManagerTest.class.getSimpleName();
    private MockContentResolver mMockResolver;
    private BookshelfManager mBookshelfManager;

    private BookstoreClient mBookstoreClient;
    private List<Book> mTestBookList = new ArrayList<>();

    public BookshelfManagerTest() {
        super(BookshelfProvider.class, AUTHORITY);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mMockResolver = getMockContentResolver();
        Assert.assertNotNull(mMockResolver);

        mBookshelfManager = BookshelfManager.get(mMockResolver);
        Assert.assertNotNull(mBookshelfManager);

        mBookstoreClient = BookstoreClient.getInstance();
        Assert.assertNotNull(mBookstoreClient);
        BookList bookList = mBookstoreClient.listBooks();
        Assert.assertNotNull(bookList);

        for (me.gujun.mybook.api.entity.Book book : bookList.getBookList()) {
            mTestBookList.add(new Book(book));
        }
        System.out.print("Fuck you");
//        Assert.assertTrue(!mTestBookList.isEmpty());
//        Log.d(TAG, "book list api response: " + mTestBookList.toString());
    }

    @SmallTest
    public void testUriAndGetType() throws Exception {
        String mimeType = mMockResolver.getType(BookshelfTable.CONTENT_URI);
        Assert.assertEquals(BookshelfTable.CONTENT_TYPE, mimeType);

        Uri itemUri = ContentUris.withAppendedId(BookshelfTable.CONTENT_ID_URI_BASE, 1);
        mimeType = mMockResolver.getType(itemUri);
        Assert.assertEquals(BookshelfTable.CONTENT_ITEM_TYPE, mimeType);
    }

    @MediumTest
    public void testInsert() {
        for (Book book : mTestBookList) {
            Uri uri = mBookshelfManager.add(book);
            Assert.assertNotNull(uri);
        }
    }

    @MediumTest
    public void testList() {
        List<Book> bookList = mBookshelfManager.list();
        Assert.assertNotNull(bookList);
        Log.d(TAG, "list from database: " + bookList.toString());
    }

    public void listBooks() {
        List<Book> bookList = mBookshelfManager.list();
        Assert.assertNotNull(bookList);
        Log.d(TAG, "List from bookmark.db: " + bookList.toString());
    }

    @MediumTest
    public void testDelete() {
        mBookshelfManager.add(new Book("Fuck you", null, null, null, null, null));
        listBooks();

        List<Book> bookList = mBookshelfManager.list();
        Book book = bookList.get(0);
        int row = mBookshelfManager.deleteById(book);
        Assert.assertEquals(row, 1);

        listBooks();
    }
}