package me.gujun.mybook.util;

import android.content.Context;
import android.net.Uri;

import java.util.List;

import me.gujun.mybook.db.BookmarkManager;
import me.gujun.mybook.db.BookshelfManager;
import me.gujun.mybook.db.model.Book;
import me.gujun.mybook.db.model.Bookmark;

/**
 * Book manager.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-24 8:52:38
 */
public class BookManager {
    private static BookManager INSTANCE;

    private BookFileManager mBookFileManager;
    private BookshelfManager mBookshelfManager;
    private BookmarkManager mBookmarkManager;

    private BookManager(Context context) {
        mBookFileManager = BookFileManager.get(context);
        mBookshelfManager = BookshelfManager.get(context);
        mBookmarkManager = BookmarkManager.get(context);
    }

    public static BookManager get(Context context) {
        if (INSTANCE == null) {
            synchronized (BookManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BookManager(context);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Get book list.
     *
     * @return Book list.
     */
    public List<Book> getBookList() {
        return mBookshelfManager.list();
    }

    /**
     * Get bookmark list.
     *
     * @return Bookmark list.
     */
    public List<Bookmark> getBookmarkList() {
        return mBookmarkManager.list();
    }

    /**
     * Get bookmark list by book (book id).
     *
     * @param book The book you want to query the bookmark.
     * @return Bookmark list added on the given book.
     */
    public List<Bookmark> getBookmarkListByBook(Book book) {
        return mBookmarkManager.listByBookId(book);
    }

    /**
     * Get book list by bookmark (book id on bookmark table).
     *
     * @param bookmark The bookmark you want to query the book.
     * @return Book item related to the given bookmark.
     */
    public Book getBookByBookmark(Bookmark bookmark) {
        return mBookshelfManager.getBookById(bookmark.getBookId());
    }

    /**
     * Add a new book.
     *
     * @param book       {@link Book} instance.
     * @param bookBuffer Binary book content.
     * @return true if add success, false otherwise.
     */
    public boolean addBook(Book book, byte[] bookBuffer) {
        Uri uri = mBookshelfManager.add(book);
        if (uri != null) {
            try {
                mBookFileManager.saveBookBinary(book.getTitle(), bookBuffer);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Delete a book.
     *
     * @param book The book you want to delete.
     * @return true if delete success, false otherwise.
     */
    public boolean deleteBook(Book book) {
        int row = mBookshelfManager.deleteById(book);
        if (row != -1) {
            boolean deleted = mBookFileManager.deleteBookFile(book.getTitle());
            if (deleted) {
                mBookmarkManager.deleteByBookId(book);
                return true;
            }
        }
        return false;
    }

    /**
     * Add a new bookmark.
     *
     * @param book     The book you want to add a bookmark on.
     * @param position The binary book file bytes position.
     * @return true if mark success, false otherwise.
     */
    public boolean addBookmark(Book book, int position) {
        Bookmark bookmark = new Bookmark();
        bookmark.setBookId(book.getId())
                .setBegin(position)
                .setDate(TimeUtils.getCurrentTimeFormated());
        Uri uri = mBookmarkManager.add(bookmark);
        if (uri != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Delete a bookmark.
     *
     * @param bookmark The bookmark you want to delete.
     * @return
     */
    public boolean deleteBookmark(Bookmark bookmark) {
        int row = mBookmarkManager.deleteById(bookmark);
        if (row != -1) {
            return true;
        }
        return false;
    }
}