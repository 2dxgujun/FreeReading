package me.gujun.mybook.ui;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;

import me.gujun.mybook.R;
import me.gujun.mybook.db.BookshelfTable;
import me.gujun.mybook.ui.view.BookshelfGridView;

/**
 * Bookshelf activity.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-17 16:27:14
 */
public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private BookshelfGridView mBookshelfGridView;

    private SimpleCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBookshelfGridView = (BookshelfGridView) findViewById(R.id.bookshelf);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
