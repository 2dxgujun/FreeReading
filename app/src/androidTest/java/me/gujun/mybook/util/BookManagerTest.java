package me.gujun.mybook.util;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

import me.gujun.mybook.api.BookstoreClient;
import me.gujun.mybook.db.model.Book;
import me.gujun.mybook.db.model.Bookmark;

/**
 * Test {@link BookManager}.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-24 9:26:05
 */
public class BookManagerTest extends AndroidTestCase {
    private static final String TAG = BookManagerTest.class.getSimpleName();

    private BookManager mBookManager;

    private BookstoreClient mBookstoreClient;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mBookManager = BookManager.get(getContext());
        Assert.assertNotNull(mBookManager);

        mBookstoreClient = BookstoreClient.getInstance();
        Assert.assertNotNull(mBookstoreClient);
    }

    public void printBook(Book book) {
        Log.d(TAG, book.getTitle() + " / " + book.getAuthor());
    }

    public void printBookmark(Bookmark bookmark) {
        Book book = mBookManager.getBookByBookmark(bookmark);
        Log.d(TAG, book.getTitle() + " " + "(" + bookmark.getBegin() + " @ " + bookmark.getDate() + ")");
    }

    public void printBookList(List<Book> bookList) {
        Log.d(TAG, "-----------------List Book Begin-----------------");
        for (Book book : bookList) {
            printBook(book);
        }
        Log.d(TAG, "------------------List Book End------------------");
    }

    public void printBookmarkList(List<Bookmark> bookmarkList) {
        Log.d(TAG, "---------------List Bookmark Begin---------------");
        for (Bookmark bookmark : bookmarkList) {
            printBookmark(bookmark);
        }
        Log.d(TAG, "----------------List Bookmark End----------------");
    }

    @LargeTest
    public void testAssemble() throws Exception {
        Log.d(TAG, "Bookstore API Requesting...");
        List<Book> bookList = new ArrayList<>();
        for (me.gujun.mybook.api.entity.Book book : mBookstoreClient.listBooks().getBookList()) {
            bookList.add(new Book(book));
        }
        Log.d(TAG, "Bookstore API Response: ");
        printBookList(bookList);

        Log.d(TAG, "Adding Books...");
        for (Book book : bookList) {
            mBookManager.addBook(book, new byte[]{1, 2, 3});
        }
        bookList = mBookManager.getBookList();
        Log.d(TAG, "Add Finish, List Added Books: ");
        printBookList(bookList);

        Book book0 = bookList.get(0);
        Book book1 = bookList.get(1);
        Book book2 = bookList.get(2);
        Book book3 = bookList.get(3);
        printBook(book0);
        printBook(book1);
        printBook(book2);
        printBook(book3);

        Log.d(TAG, "Adding Bookmarks...");
        mBookManager.addBookmark(book0, 123);
        mBookManager.addBookmark(book0, 10001);
        mBookManager.addBookmark(book1, 10086);
        mBookManager.addBookmark(book2, 1);
        mBookManager.addBookmark(book2, 2);
        mBookManager.addBookmark(book2, 3);
        mBookManager.addBookmark(book3, 10086);

        List<Bookmark> bookmarkList = mBookManager.getBookmarkList();
        Log.d(TAG, "List Added Bookmarks: ");
        printBookmarkList(bookmarkList);

        bookmarkList = mBookManager.getBookmarkListByBook(book0);
        Log.d(TAG, "List Bookmarks on " + book0.getTitle());
        printBookmarkList(bookmarkList);

        bookmarkList = mBookManager.getBookmarkListByBook(book2);
        Log.d(TAG, "List Bookmarks on " + book2.getTitle());
        printBookmarkList(bookmarkList);

        Log.d(TAG, "Deleting Books...");
        mBookManager.deleteBook(book0);
        mBookManager.deleteBook(book3);
        Log.d(TAG, "Delete Finish, List Exists Books and Bookmarks");

        bookList = mBookManager.getBookList();
        bookmarkList = mBookManager.getBookmarkList();
        printBookList(bookList);
        printBookmarkList(bookmarkList);

        Bookmark bookmark0 = bookmarkList.get(0);
        Log.d(TAG, "Deleting Bookmark " + bookmark0.getBookId());
        mBookManager.deleteBookmark(bookmark0);
        Log.d(TAG, "List Bookmarks: ");
        bookmarkList = mBookManager.getBookmarkList();
        printBookmarkList(bookmarkList);
    }
}