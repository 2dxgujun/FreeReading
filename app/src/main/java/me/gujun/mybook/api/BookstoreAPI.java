package me.gujun.mybook.api;

import me.gujun.mybook.api.entity.BookList;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Bookstore REST API.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-16 9:29:57
 */
public interface BookstoreAPI {
    @GET("/json.txt")
    BookList listBooks();

    @GET("/json.txt")
    void listBooks(Callback<BookList> callback);
}