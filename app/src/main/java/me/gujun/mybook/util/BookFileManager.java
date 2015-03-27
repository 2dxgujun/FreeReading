package me.gujun.mybook.util;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Book file manager.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-19 15:54:21
 */
public class BookFileManager {
    private static final String BOOK_DIR = "BookCache";
    private static final String FILE_SUFFIX_TXT = ".txt";

    private static BookFileManager INSTANCE;

    private File mBookDir;

    private BookFileManager(Context context) {
        mBookDir = new File(context.getCacheDir(), BOOK_DIR);
        if (!mBookDir.exists() && !mBookDir.mkdirs()) {
            throw new RuntimeException("Can't make dirs in "
                    + mBookDir.getAbsolutePath());
        }
    }

    public static BookFileManager get(Context context) {
        if (INSTANCE == null) {
            synchronized (BookManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BookFileManager(context);
                }
            }
        }
        return INSTANCE;
    }

    public File getBookFile(String bookTitle) {
        File bookFile = new File(mBookDir, bookTitle + FILE_SUFFIX_TXT);
        if (bookFile.exists()) {
            return bookFile;
        }
        return null;
    }

    public String getBookPath(String bookTitle) {
        File bookFile = new File(mBookDir, bookTitle + FILE_SUFFIX_TXT);
        if (bookFile.exists()) {
            return bookFile.getPath();
        }
        return null;
    }

    public boolean deleteBookFile(String bookTitle) {
        File bookFile = getBookFile(bookTitle);
        if (bookFile != null) {
            return bookFile.delete();
        } else {
            return false;
        }
    }

    public void saveBookBinary(String bookTitle, byte[] buffer) {
        File bookFile = getBookFile(bookTitle);
        if (bookFile != null) {
            boolean deleted = bookFile.delete();
            if (!deleted) {
                throw new RuntimeException("Can not deleteById book file.");
            }
        } else {
            bookFile = new File(mBookDir, bookTitle + FILE_SUFFIX_TXT);
        }

        try {
            boolean created = bookFile.createNewFile();
            if (!created) {
                throw new RuntimeException("Can not create book file.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Catch exception on creating book file.");
        }

        FileOutputStream output = null;
        try {
            // assert file != null;
            output = new FileOutputStream(bookFile);
            output.write(buffer);
        } catch (IOException e) {
            throw new RuntimeException("Catch exception on writing book file.");
        } finally {
            if (output != null) {
                try {
                    output.flush();
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}