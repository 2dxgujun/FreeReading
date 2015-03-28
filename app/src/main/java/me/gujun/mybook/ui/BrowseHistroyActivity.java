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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.gujun.mybook.R;
import me.gujun.mybook.SingleTypeAdapter;
import me.gujun.mybook.db.BrowseRecordManager;
import me.gujun.mybook.db.model.BrowseRecord;
import me.gujun.mybook.db.table.BrowseRecordTable;

public class BrowseHistroyActivity extends ActionBarActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {
    private ListView mHistoryListView;
    private LinearLayout mNoHistoryReminder;

    private HistoryListAdapter mAdapter;

    private MaterialDialog mDeleteDialog;

    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_histroy);

        mNoHistoryReminder = (LinearLayout) findViewById(R.id.no_history_reminder);

        mAdapter = new HistoryListAdapter(this);
        mHistoryListView = (ListView) findViewById(R.id.history_list);
        mHistoryListView.setAdapter(mAdapter);
        mHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BrowseRecord record = mAdapter.getItem(position);
                File file = new File(record.getFilePath());
                if (file.exists()) {
                    openReadingActivity(record);
                } else {
                    Toast.makeText(BrowseHistroyActivity.this, R.string.file_not_exists, Toast.LENGTH_LONG).show();
                }
            }
        });

        mHistoryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mPosition = position;
                mDeleteDialog.show();
                return true;
            }
        });

        mDeleteDialog = new MaterialDialog.Builder(this)
                .title(R.string.delete_browse_history_hint)
                .positiveText(R.string.delete)
                .negativeText(R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        BrowseRecord record = mAdapter.getItem(mPosition);
                        try {
                            BrowseRecordManager.get(getApplicationContext()).deleteById(record);
                            Toast.makeText(BrowseHistroyActivity.this, R.string.delete_success, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(BrowseHistroyActivity.this, R.string.delete_failure, Toast.LENGTH_LONG).show();
                        }

                        getLoaderManager().restartLoader(0, null, BrowseHistroyActivity.this);
                    }
                })
                .build();

        getLoaderManager().initLoader(0, null, this);
    }

    private void openReadingActivity(BrowseRecord record) {
        Intent intent = ReadingActivity.createIntent(record);
        intent.setClass(BrowseHistroyActivity.this, ReadingActivity.class);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cl = new CursorLoader(this, BrowseRecordTable.CONTENT_URI,
                null, null, null, null);
        cl.setUpdateThrottle(2000);
        return cl;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<BrowseRecord> mRecordList = new ArrayList<>();
        while (data.moveToNext()) {
            mRecordList.add(BrowseRecord.resolve(data));
        }

        mNoHistoryReminder.setVisibility(mRecordList.isEmpty() ? View.VISIBLE : View.GONE);

        mAdapter.setItems(mRecordList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.setItems((BrowseRecord[]) null);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Adapter.
     */
    class HistoryListAdapter extends SingleTypeAdapter<BrowseRecord> {
        public HistoryListAdapter(Context context) {
            super(context, R.layout.history_item);
        }

        @Override
        protected ViewHolder onCreateViewHolder(View convertView) {
            FileViewHolder holder = new FileViewHolder();
            holder.fileIconImg = (ImageView) convertView.findViewById(R.id.iv_file_icon);
            holder.fileNameTxt = (TextView) convertView.findViewById(R.id.tv_file_name);
            holder.filePathTxt = (TextView) convertView.findViewById(R.id.tv_file_path);

            return holder;
        }

        /**
         * View holder.
         */
        class FileViewHolder extends ViewHolder {
            private ImageView fileIconImg;
            private TextView fileNameTxt;
            private TextView filePathTxt;

            @Override
            protected void update(BrowseRecord item) {
                fileIconImg.setImageResource(R.drawable.ic_file_txt);
                fileNameTxt.setText(item.getFileName());
                filePathTxt.setText(item.getFilePath());
            }
        }
    }
}