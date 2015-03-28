package me.gujun.mybook.ui;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import me.gujun.mybook.R;
import me.gujun.mybook.db.model.Book;
import me.gujun.mybook.db.table.BookshelfTable;
import me.gujun.mybook.ui.view.BookshelfGridView;
import me.gujun.mybook.util.BookManager;

/**
 * Bookshelf activity.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-17 16:27:14
 */
public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private BookshelfGridView mBookshelfGridView;
    private LinearLayout mBookstoreReminderView;

    private MaterialDialog mDeleteDialog;

    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBookshelfGridView = (BookshelfGridView) findViewById(R.id.bookshelf);
        mBookshelfGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = mBookshelfGridView.getBook(position);
                openReadingActivity(book);
            }
        });

        mBookshelfGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mPosition = position;
                mDeleteDialog.show();
                return true;
            }
        });

        mBookstoreReminderView = (LinearLayout) findViewById(R.id.bookstore_reminder);
        mBookstoreReminderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBookstoreActivity();
            }
        });

        mDeleteDialog = new MaterialDialog.Builder(this)
                .title(R.string.delete_book_hint)
                .positiveText(R.string.delete)
                .negativeText(R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        Book book = mBookshelfGridView.getBook(mPosition);
                        try {
                            BookManager.get(getApplicationContext()).deleteBook(book);
                            Toast.makeText(MainActivity.this, R.string.delete_success, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, R.string.delete_failure, Toast.LENGTH_LONG).show();
                        }

                        getLoaderManager().restartLoader(0, null, MainActivity.this);
                    }
                })
                .build();

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_browser) {
            openFileBrowserActivity();
        } else if (id == R.id.action_bookstore) {
            openBookstoreActivity();
            return true;
        } else if (id == R.id.action_bookmark) {
            openBookmarkActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openReadingActivity(Book book) {
        Intent intent = ReadingActivity.createIntent(book);
        intent.setClass(MainActivity.this, ReadingActivity.class);
        startActivity(intent);
    }

    private void openFileBrowserActivity() {
        Intent intent = new Intent(MainActivity.this, FileBrowserActivity.class);
        startActivity(intent);
    }

    private void openBookstoreActivity() {
        Intent intent = new Intent(MainActivity.this, BookstoreActivity.class);
        startActivity(intent);
    }

    private void openBookmarkActivity() {
        Intent intent = new Intent(MainActivity.this, BookmarkActivity.class);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cl = new CursorLoader(this, BookshelfTable.CONTENT_URI,
                null, null, null, null);
        cl.setUpdateThrottle(2000);
        return cl;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ArrayList<Book> mBookList = new ArrayList<>();
        while (data.moveToNext()) {
            mBookList.add(Book.resolve(data));
        }

        mBookstoreReminderView.setVisibility(mBookList.isEmpty() ? View.VISIBLE : View.GONE);
        mBookshelfGridView.setBookList(mBookList);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mBookshelfGridView.setBookList(null);
    }
}