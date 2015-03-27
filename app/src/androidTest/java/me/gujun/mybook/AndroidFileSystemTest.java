package me.gujun.mybook;

import android.os.Environment;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;

/**
 * <Please describe the usage of this class>
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-25 17:24:55
 */
public class AndroidFileSystemTest extends AndroidTestCase {
    private static final FileFilter TEXT_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            if (pathname.isDirectory() || pathname.getName().endsWith(".txt")) {
                return true;
            }
            return false;
        }
    };

    @SmallTest
    public void testListDataFolder() throws Exception {
        File storageRoot = Environment.getExternalStorageDirectory();
        browseTo(storageRoot);
    }

    private void browseTo(File dir) {
        Log.d("FUCK THE TAG", dir.getName());

        if (dir.isDirectory()) {
            File[] files = dir.listFiles(TEXT_FILTER);
            for (File file : files) {
                browseTo(file);
            }
        }
    }
}