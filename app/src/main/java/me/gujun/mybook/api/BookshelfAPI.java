package me.gujun.mybook.api;

import me.gujun.mybook.api.entity.Bookshelf;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Bookshelf REST API.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-16 9:29:57
 */
public interface BookshelfAPI {
    @GET("/json.txt")
    Bookshelf getBookshelf();

    @GET("/json.txt")
    void getBookshelf(Callback<Bookshelf> callback);
}