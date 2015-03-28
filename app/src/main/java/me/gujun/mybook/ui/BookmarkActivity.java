package me.gujun.mybook.ui;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import me.gujun.mybook.R;
import me.gujun.mybook.SingleTypeAdapter;
import me.gujun.mybook.db.BookshelfManager;
import me.gujun.mybook.db.model.Book;
import me.gujun.mybook.db.model.Bookmark;
import me.gujun.mybook.db.table.BookmarkTable;
import me.gujun.mybook.util.BookManager;
import me.gujun.mybook.util.DisplayImageOptionsProvider;

public class BookmarkActivity extends ActionBarActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {
    private ListView mBookmarkListView;
    private LinearLayout mNoBookmarkReminder;
    private BookmarkListAdapter mAdapter;

    private MaterialDialog mDeleteDialog;

    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        mNoBookmarkReminder = (LinearLayout) findViewById(R.id.no_bookmark_reminder);

        mAdapter = new BookmarkListAdapter(this);
        mBookmarkListView = (ListView) findViewById(R.id.bookmark_list);
        mBookmarkListView.setAdapter(mAdapter);
        mBookmarkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bookmark bookmark = mAdapter.getItem(position);
                Book book = BookManager.get(getApplicationContext()).getBookByBookmark(bookmark);
                openReadingActivity(book, bookmark);
            }
        });

        mBookmarkListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mPosition = position;
                mDeleteDialog.show();
                return true;
            }
        });

        mDeleteDialog = new MaterialDialog.Builder(this)
                .title(R.string.delete_bookmark_hint)
                .positiveText(R.string.delete)
                .negativeText(R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        Bookmark bookmark = mAdapter.getItem(mPosition);
                        try {
                            BookManager.get(getApplicationContext()).deleteBookmark(bookmark);
                            Toast.makeText(BookmarkActivity.this, R.string.delete_success, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(BookmarkActivity.this, R.string.delete_failure, Toast.LENGTH_LONG).show();
                        }

                        getLoaderManager().restartLoader(0, null, BookmarkActivity.this);
                    }
                })
                .build();

        getLoaderManager().initLoader(0, null, this);
    }

    private void openReadingActivity(Book book, Bookmark bookmark) {
        Intent intent = ReadingActivity.createIntent(book, bookmark);
        intent.setClass(BookmarkActivity.this, ReadingActivity.class);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cl = new CursorLoader(this, BookmarkTable.CONTENT_URI,
                null, null, null, null);
        cl.setUpdateThrottle(2000);
        return cl;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<Bookmark> mBookmarkList = new ArrayList<>();
        while (data.moveToNext()) {
            mBookmarkList.add(Bookmark.resolve(data));
        }

        mNoBookmarkReminder.setVisibility(mBookmarkList.isEmpty() ? View.VISIBLE : View.GONE);

        mAdapter.setItems(mBookmarkList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.setItems((Bookmark[]) null);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Bookmark list adapter.
     */
    class BookmarkListAdapter extends SingleTypeAdapter<Bookmark> {
        private BookshelfManager mBookshelfManager;

        public BookmarkListAdapter(Context context) {
            super(context, R.layout.bookmark_list_item);
            mBookshelfManager = BookshelfManager.get(context);
        }

        @Override
        protected ViewHolder onCreateViewHolder(View convertView) {
            BookmarkViewHolder holder = new BookmarkViewHolder();
            holder.coverImg = (ImageView) convertView.findViewById(R.id.iv_cover);
            holder.titleTxt = (TextView) convertView.findViewById(R.id.tv_title);
            holder.authorTxt = (TextView) convertView.findViewById(R.id.tv_author);
            holder.tagTxt = (TextView) convertView.findViewById(R.id.tv_tag);
            holder.dateTxt = (TextView) convertView.findViewById(R.id.tv_date);
            return holder;
        }

        /**
         * Bookmark list item view holder.
         */
        class BookmarkViewHolder extends ViewHolder {
            ImageView coverImg;
            TextView titleTxt;
            TextView authorTxt;
            TextView tagTxt;
            TextView dateTxt;

            @Override
            protected void update(Bookmark bookmark) {
                int bookId = bookmark.getBookId();
                Book book = mBookshelfManager.getBookById(bookId);
                ImageLoader.getInstance().displayImage(book.getCoverUrl(), coverImg,
                        DisplayImageOptionsProvider.getDisplayImageOptions());
                titleTxt.setText(book.getTitle());
                authorTxt.setText(book.getAuthor());
                tagTxt.setText(book.getTag());
                dateTxt.setText(bookmark.getDate());
            }
        }
    }
}