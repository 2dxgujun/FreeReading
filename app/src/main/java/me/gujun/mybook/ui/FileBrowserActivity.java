package me.gujun.mybook.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import me.gujun.mybook.R;
import me.gujun.mybook.SingleTypeAdapter;
import me.gujun.mybook.db.BrowseRecordManager;
import me.gujun.mybook.db.model.BrowseRecord;
import me.gujun.mybook.util.TimeUtils;

public class FileBrowserActivity extends ActionBarActivity {
    private static final String TAG = FileBrowserActivity.class.getSimpleName();
    private static final boolean DEBUG = true;

    private static final FileFilter TEXT_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            if (pathname.isDirectory() || pathname.getName().endsWith(".txt")) {
                return true;
            }
            return false;
        }
    };

    private ListView mListView;

    private MenuItem mSearchMenuItem;
    private SearchView mSearchView;

    private boolean mIsSearchViewExpanded;

    private FileListAdapter mListAdapter;

    private AsyncFileSearchTask mAsyncFileSearchTask = new AsyncFileSearchTask();

    private MaterialDialog mProgressDialog;

    /**
     * The current direction.
     */
    private File mCurrentDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);

        mListAdapter = new FileListAdapter(this);

        mListView = (ListView) findViewById(R.id.file_list);
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = mListAdapter.getItem(position);
                if (file.isDirectory()) {
                    browseTo(file);
                } else {
                    openReadingActivity(file);
                    BrowseRecord record = new BrowseRecord(file, TimeUtils.getCurrentTimeFormated());
                    BrowseRecordManager.get(FileBrowserActivity.this).add(record);
                }
            }
        });

        mProgressDialog = new MaterialDialog.Builder(this)
                .title(R.string.search_in_progress)
                .content("")
                .progress(true, 0)
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        cancelSearchTask();
                    }
                })
                .build();

        browseToRoot();
    }

    private void openReadingActivity(File file) {
        Intent intent = ReadingActivity.createIntent(file);
        intent.setClass(FileBrowserActivity.this, ReadingActivity.class);
        startActivity(intent);
    }

    private void browseTo(File dir) {
        // Update the action bar sub title.
        getSupportActionBar().setSubtitle(dir.getAbsolutePath());

        mCurrentDir = dir;
        // Update list view.
        mListAdapter.setItems(dir.listFiles(TEXT_FILTER));
    }

    private boolean browseToParent() {
        if (mCurrentDir.getParent() != null) {
            browseTo(mCurrentDir.getParentFile());
            return true;
        }
        return false;
    }

    private void browseToRoot() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            browseTo(Environment.getExternalStorageDirectory());
        } else {
            browseTo(Environment.getRootDirectory());
        }
    }

    private void executeSearchTask(String keyword) {
        mAsyncFileSearchTask = new AsyncFileSearchTask();
        mAsyncFileSearchTask.search(keyword);
    }

    private void cancelSearchTask() {
        mAsyncFileSearchTask.cancel(true);
        mAsyncFileSearchTask = null;
    }

    private void expandSearchView() {
        mSearchView.onActionViewExpanded();
        mListAdapter.clear();
        mIsSearchViewExpanded = true;
    }

    private void collapseSearchView() {
        mSearchView.onActionViewCollapsed();
        browseTo(mCurrentDir);
        mIsSearchViewExpanded = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filebrowser, menu);

        mSearchMenuItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) mSearchMenuItem.getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mProgressDialog.show();
                executeSearchTask(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandSearchView();
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                collapseSearchView();
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (mIsSearchViewExpanded) {
                collapseSearchView();
                return true;
            }
        } else if (id == R.id.action_history) {
            openBrowseHistroyActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openBrowseHistroyActivity() {
        Intent intent = new Intent(FileBrowserActivity.this, BrowseHistroyActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (mIsSearchViewExpanded) {
            collapseSearchView();
        } else if (!browseToParent()) {
            super.onBackPressed();
        }
    }

    /**
     * Async task for searching files.
     */
    class AsyncFileSearchTask extends AsyncTask<String, String, List<File>> {
        private String keyword;

        private List<File> resultList = new ArrayList<>();

        /**
         * Can't update UI in this callback.
         */
        private final FileFilter SEARCH_FILTER = new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (!file.isDirectory()) {
                    String filename = file.getName();
                    if (filename.endsWith(".txt") && filename.contains(keyword)) {
                        resultList.add(file);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mListAdapter.setItems(resultList);
                            }
                        });
                        // Only the original thread that created a view hierarchy can touch its views.
                        // mListAdapter.setItems(resultList);
                    }
                } else {
                    publishProgress(file.getAbsolutePath());
                }
                return true;
            }
        };

        @Override
        protected List<File> doInBackground(String... params) {
            keyword = params[0];
            resultList.clear();
            recursiveSearch(Environment.getExternalStorageDirectory());

            return resultList;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            String filename = values[0];
            mProgressDialog.setContent("Searching in:\n" + filename);
        }

        @Override
        protected void onPostExecute(List<File> files) {
            mProgressDialog.dismiss();
            Toast.makeText(FileBrowserActivity.this, R.string.search_finished,
                    Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onCancelled(List<File> files) {
            mProgressDialog.dismiss();
            Toast.makeText(FileBrowserActivity.this, R.string.search_canceled,
                    Toast.LENGTH_LONG).show();
        }

        /**
         * Call this method to execute the file search task.
         */
        public void search(String keyword) {
            execute(keyword);
        }

        /**
         * <strong>CAUTION</strong>:
         * This method will be invoked from {@link #doInBackground(String...)} which run in a
         * background thread
         */
        private void recursiveSearch(File dir) {
            if (!isCancelled()) { // check if the task is cancelled and abort the recursive
                if (dir.isDirectory()) {
                    File[] files = dir.listFiles(SEARCH_FILTER);
                    for (File file : files) {
                        recursiveSearch(file);
                    }
                }
            }
        }
    }

    /**
     * Adapter.
     */
    class FileListAdapter extends SingleTypeAdapter<File> {
        public FileListAdapter(Context context) {
            super(context, R.layout.file_item);
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
            protected void update(File item) {
                if (item.isDirectory()) {
                    fileIconImg.setImageResource(R.drawable.ic_file_folder);
                } else {
                    fileIconImg.setImageResource(R.drawable.ic_file_txt);
                }
                fileNameTxt.setText(item.getName());
                if (mIsSearchViewExpanded) {
                    filePathTxt.setVisibility(View.VISIBLE);
                    filePathTxt.setText(item.getAbsolutePath());
                } else {
                    filePathTxt.setVisibility(View.GONE);
                }
            }
        }
    }
}