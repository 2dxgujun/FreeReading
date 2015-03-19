package me.gujun.mybook.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import me.gujun.mybook.R;
import me.gujun.mybook.api.BookshelfAPIClient;
import me.gujun.mybook.api.entity.Bookshelf;
import me.gujun.mybook.ui.view.BookshelfListView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Online bookshelf activity.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-17 16:27:14
 */
public class OnlineBookshelfActivity extends ActionBarActivity {
    private BookshelfListView mBookshelfListView;
    private BookshelfAPIClient mBookshelfAPIClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_bookshelf);

        mBookshelfListView = (BookshelfListView) findViewById(R.id.bookshelf);

        mBookshelfAPIClient = BookshelfAPIClient.getInstance();
        mBookshelfAPIClient.getBookshelf(new Callback<Bookshelf>() {
            @Override
            public void success(Bookshelf bookshelf, Response response) {
                mBookshelfListView.setBookList(bookshelf.getBookList());
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}