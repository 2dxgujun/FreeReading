package me.gujun.mybook;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Environment;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * My Book Application.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-17 16:56:58
 */
public class MyBookApplication extends Application {
    private static final String PREFS_NAME = "app_default";
    private static final String KEY_ASSETS_DELIVERED = "assets_delivered";

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(getApplicationContext());
    }

    private boolean isAssetsDelivered() {
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_ASSETS_DELIVERED, false);
    }

    private void deliverAssetsToStorage() {
        File storageDir;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = Environment.getExternalStorageDirectory();
        } else {
            storageDir = getFilesDir();
        }
        storageDir = new File(storageDir, "散文");
        if (storageDir.mkdir()) {

        } else {

        }
    }

    class AssetsDeliverTask extends AsyncTask<File, Void, Boolean> {
        @Override
        protected Boolean doInBackground(File... storageDir) {
            AssetManager asset = null;
            try {
                asset = getAssets();
                String[] filenames = asset.list("docs/");
                for (String filename : filenames) {
                    InputStream in = asset.open("docs/" + filename);
                    byte[] buffer = new byte[128];

                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (asset != null) {
                    asset.close();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean delivered) {
            SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME,
                    Context.MODE_PRIVATE).edit();
            editor.putBoolean(KEY_ASSETS_DELIVERED, delivered);
            editor.commit();
        }
    }

    /**
     * Initialize ImageLoader.
     *
     * @param context application context
     */
    protected void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(300, 300)
                .diskCacheSize(5 * 1024 * 1024); // set disk cache
        ImageLoader.getInstance().init(builder.build());
    }
}