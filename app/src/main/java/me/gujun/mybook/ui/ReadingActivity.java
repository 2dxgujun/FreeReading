package me.gujun.mybook.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import me.gujun.mybook.R;
import me.gujun.mybook.db.model.Book;
import me.gujun.mybook.db.model.Bookmark;
import me.gujun.mybook.ui.view.PageWidget;
import me.gujun.mybook.util.BookManager;
import me.gujun.mybook.util.BookPageFactory;

public class ReadingActivity extends ActionBarActivity {
    private static final String TAG = ReadingActivity.class.getSimpleName();
    private static final String EXTRA_BOOK = "EXTRA_BOOK";
    private static final String EXTRA_BOOKMARK = "EXTRA_BOOKMARK";
    private static final String EXTRA_FILE = "EXTRA_FILE";

    private Book mBook;
    private Bookmark mBookmark;
    private File mFile;

    private Bitmap mCurrPageBitmap;
    private Bitmap mNextPageBitmap;
    private Canvas mCurrPageCanvas;
    private Canvas mNextPageCanvas;

    private BookPageFactory mPageFactory;
    private PageWidget mPageWidget;
    private Button mToggleBtn;

    private boolean mIsToolbarShown = false;

    public static Intent createIntent(Book book) {
        return createIntent(book, null);
    }

    public static Intent createIntent(Book book, Bookmark bookmark) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_BOOK, book);
        intent.putExtra(EXTRA_BOOKMARK, bookmark);
        return intent;
    }

    public static Intent createIntent(File file) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_FILE, file);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_reading);

        mBook = getIntent().getParcelableExtra(EXTRA_BOOK);
        mBookmark = getIntent().getParcelableExtra(EXTRA_BOOKMARK);
        mFile = (File) getIntent().getSerializableExtra(EXTRA_FILE);
        if (mBook != null) {
            if (mBookmark == null) {
                mBookmark = new Bookmark();
            }
            setTitle(mBook.getTitle() + " - " + mBook.getAuthor());
        } else if (mFile != null) {
            setTitle(mFile.getName());
        }

        WindowManager manage = getWindowManager();
        Display display = manage.getDefaultDisplay();

        final int screenWidth = display.getWidth();
        final int screenHeight = display.getHeight();

        mCurrPageBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        mNextPageBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        mCurrPageCanvas = new Canvas(mCurrPageBitmap);
        mNextPageCanvas = new Canvas(mNextPageBitmap);

        mPageWidget = (PageWidget) findViewById(R.id.reading_page);
        mPageWidget.setScreen(screenWidth, screenHeight);

        mToggleBtn = (Button) findViewById(R.id.btn_toggle);
        mToggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsToolbarShown) {
                    getSupportActionBar().hide();
                } else {
                    getSupportActionBar().show();
                }
                mIsToolbarShown = !mIsToolbarShown;
            }
        });

        mPageWidget.setBitmaps(mCurrPageBitmap, mCurrPageBitmap);
        mPageWidget.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mPageWidget.abortAnimation();
                    mPageWidget.calcCornerXY(event.getX(), event.getY());
                    mPageFactory.onDraw(mCurrPageCanvas);

                    if (mPageWidget.DragToRight()) { // Drag to right
                        try {
                            hideToolbar();
                            mPageFactory.prePage();
                        } catch (IOException e1) {
                            Log.e(TAG, "onTouch->prePage error", e1);
                        }
                        if (mPageFactory.isfirstPage()) {
                            Toast.makeText(ReadingActivity.this, "当前是第一页", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        mPageFactory.onDraw(mNextPageCanvas);
                    } else { // Drag to left
                        try {
                            hideToolbar();
                            mPageFactory.nextPage();
                        } catch (IOException e1) {
                            Log.e(TAG, "onTouch->nextPage error", e1);
                        }
                        if (mPageFactory.islastPage()) {
                            Toast.makeText(ReadingActivity.this, "已经是最后一页了", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        mPageFactory.onDraw(mNextPageCanvas);
                    }
                    mPageWidget.setBitmaps(mCurrPageBitmap, mNextPageBitmap);
                }
                return mPageWidget.doTouchEvent(event);
            }
        });

        mPageFactory = new BookPageFactory(screenWidth, screenHeight);
        mPageFactory.setBgBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bg_reading));
        mPageFactory.setM_textColor(Color.rgb(28, 28, 28));
        try {
            if (mBook != null) {
                mPageFactory.openBook(this, mBook, mBookmark);
            } else {
                mPageFactory.openBook(mFile);
            }
            mPageFactory.onDraw(mCurrPageCanvas);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "打开电子书失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mBook != null) {
            getMenuInflater().inflate(R.menu.menu_reading, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_add_bookmark) {
            BookManager.get(this).addBookmark(mBook, mPageFactory.getM_mbBufBegin());
            Toast.makeText(this, R.string.add_bookmark_success, Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void hideToolbar() {
        getSupportActionBar().hide();
    }
}