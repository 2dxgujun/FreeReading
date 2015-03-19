package me.gujun.mybook;

import android.test.AndroidTestCase;

import junit.framework.Assert;

import me.gujun.mybook.api.BookshelfAPIClient;
import me.gujun.mybook.api.entity.Bookshelf;

public class BookshelfAPITest extends AndroidTestCase {
    private BookshelfAPIClient mBookshelfAPIClient;

    public void setUp() throws Exception {
        mBookshelfAPIClient = BookshelfAPIClient.getInstance();
        Assert.assertNotNull(mBookshelfAPIClient);
    }

    public void testGetBookshelf() {
        Bookshelf bookshelf = mBookshelfAPIClient.getBookshelf();
        System.out.print(bookshelf.toString());
    }
}