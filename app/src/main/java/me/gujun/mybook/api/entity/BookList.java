package me.gujun.mybook.api.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Book list entity.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-16 9:26:59
 */
public class BookList {
    @SerializedName("book_list")
    private List<Book> bookList;

    public List<Book> getBookList() {
        return bookList;
    }
}