package me.gujun.mybook.api;

import com.google.gson.Gson;

import me.gujun.mybook.api.entity.Bookshelf;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Bookshelf API REST client.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-16 9:32:19
 */
public class BookshelfAPIClient {
    private static BookshelfAPIClient INSTANCE;
    private final BookshelfAPI mBookshelfAPI;

    private BookshelfAPIClient() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://github-2dxgujun.qiniudn.com")
                .setConverter(new GsonConverter(new Gson(), "UTF-8"))
                .build();
        mBookshelfAPI = restAdapter.create(BookshelfAPI.class);
    }

    public static BookshelfAPIClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BookshelfAPIClient();
        }
        return INSTANCE;
    }

    public Bookshelf getBookshelf() {
        return mBookshelfAPI.getBookshelf();
    }

    public void getBookshelf(Callback<Bookshelf> callback) {
        mBookshelfAPI.getBookshelf(callback);
    }
}