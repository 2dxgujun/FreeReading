package me.gujun.mybook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.gujun.mybook.R;
import me.gujun.mybook.api.BookstoreClient;
import me.gujun.mybook.api.entity.BookList;
import me.gujun.mybook.db.model.Book;
import me.gujun.mybook.ui.view.BookshelfListView;
import me.gujun.mybook.util.BookManager;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Bookstore activity.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-17 16:27:14
 */
public class BookstoreActivity extends ActionBarActivity {
    private static final int MSG_GET_BOOKSHELF = 1;
    private static final int MSG_DOWNLOAD = 2;

    private BookshelfListView mBookshelfListView;
    private LinearLayout mBookstoreRefreshView;
    private ProgressBar mLoadingProgressBar;

    private MaterialDialog mPromptDialog;
    private MaterialDialog mProgressDialog;

    private int mPosition;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_GET_BOOKSHELF) { // Refresh the bookstore content
                mLoadingProgressBar.setVisibility(View.VISIBLE);
                BookstoreClient.getInstance().listBooks(new retrofit.Callback<BookList>() {
                    @Override
                    public void success(BookList bookAPIList, Response response) {
                        List<Book> bookList = new ArrayList<>();
                        for (me.gujun.mybook.api.entity.Book book : bookAPIList.getBookList()) {
                            Book newBook = BookManager.get(BookstoreActivity.this).getBookByTitle(book.getTitle());
                            if (newBook == null) {
                                newBook = new Book(book);
                            }
                            bookList.add(newBook);
                        }
                        mBookshelfListView.setBookList(bookList);
                        mBookstoreRefreshView.setVisibility(View.GONE);
                        mLoadingProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        mBookstoreRefreshView.setVisibility(View.VISIBLE);
                        mLoadingProgressBar.setVisibility(View.GONE);
                    }
                });
            } else if (msg.what == MSG_DOWNLOAD) { // Start downloading book content
                mProgressDialog.show();
                final int position = msg.arg1;
                final Book book = (Book) msg.obj;
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(book.getContentUrl()).build();
                client.newCall(request).enqueue(new com.squareup.okhttp.Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        mProgressDialog.dismiss();
                        post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BookstoreActivity.this, R.string.download_failure,
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                        int id = BookManager.get(getApplicationContext()).addBook(book, response.body().bytes());
                        // Must set the book id, needed when add a bookmark
                        book.setId(id);

                        mBookshelfListView.post(new Runnable() {
                            @Override
                            public void run() {
                                mBookshelfListView.markedBookAsDownloaded(position);
                            }
                        });

                        mProgressDialog.dismiss();
                        post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BookstoreActivity.this, R.string.download_success,
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookstore);

        mBookshelfListView = (BookshelfListView) findViewById(R.id.bookshelf);

        mBookstoreRefreshView = (LinearLayout) findViewById(R.id.bookstore_refresh);
        mBookstoreRefreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshBookshelf();
                mBookstoreRefreshView.setVisibility(View.GONE);
            }
        });

        mLoadingProgressBar = (ProgressBar) findViewById(R.id.loading_progress);

        refreshBookshelf();

        mBookshelfListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Book book = mBookshelfListView.getBook(position);
                if (mBookshelfListView.isBookDownloaded(position)) {
                    openReadingActivity(book);
                } else {
                    mPosition = position;
                    mPromptDialog.setTitle("《" + book.getTitle() + "》");
                    mPromptDialog.show();
                }
            }
        });

        mPromptDialog = new MaterialDialog.Builder(this)
                .title("")
                .content(R.string.download_hint)
                .positiveText(R.string.download)
                .negativeText(R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        mHandler.obtainMessage(MSG_DOWNLOAD, mPosition, 0,
                                mBookshelfListView.getBook(mPosition)).sendToTarget();
                    }
                })
                .build();

        mProgressDialog = new MaterialDialog.Builder(this)
                .title(R.string.download_in_progress)
                .progress(true, 0)
                .cancelable(false)
                .build();
    }

    private void openReadingActivity(Book book) {
        Intent intent = ReadingActivity.createIntent(book);
        intent.setClass(BookstoreActivity.this, ReadingActivity.class);
        startActivity(intent);
    }

    private void refreshBookshelf() {
        mHandler.sendEmptyMessage(MSG_GET_BOOKSHELF);
    }
}