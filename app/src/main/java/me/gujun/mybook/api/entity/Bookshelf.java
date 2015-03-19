package me.gujun.mybook.api.entity;

import java.util.List;

/**
 * Bookshelf entity.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-16 9:26:59
 */
public class Bookshelf {
    private List<Book> book_list;

    public List<Book> getBookList() {
        return book_list;
    }
}