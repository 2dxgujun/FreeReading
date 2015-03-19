package me.gujun.mybook.db;

import me.gujun.mybook.api.entity.Book;

/**
 * Book wrapper.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-19 10:34:40
 */
public class BookWrapper {
    private long id;
    private Book book;

    public BookWrapper(long id, Book book) {
        this.id = id;
        this.book = book;
    }

    public long getId() {
        return id;
    }

    public BookWrapper setId(long id) {
        this.id = id;
        return this;
    }

    public Book getBook() {
        return book;
    }

    public BookWrapper setBook(Book book) {
        this.book = book;
        return this;
    }
}