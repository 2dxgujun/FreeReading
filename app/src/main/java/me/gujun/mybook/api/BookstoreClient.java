package me.gujun.mybook.api;

import com.google.gson.Gson;

import me.gujun.mybook.api.entity.BookList;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Bookstore API REST client.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-16 9:32:19
 */
public class BookstoreClient {
    private static BookstoreClient INSTANCE;
    private final BookstoreAPI mBookstoreAPI;

    private BookstoreClient() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://github-2dxgujun.qiniudn.com")
                .setConverter(new GsonConverter(new Gson(), "UTF-8"))
                .build();
        mBookstoreAPI = restAdapter.create(BookstoreAPI.class);
    }

    public static BookstoreClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BookstoreClient();
        }
        return INSTANCE;
    }

    public BookList listBooks() {
        return mBookstoreAPI.listBooks();
    }

    public void listBooks(Callback<BookList> callback) {
        mBookstoreAPI.listBooks(callback);
    }
}