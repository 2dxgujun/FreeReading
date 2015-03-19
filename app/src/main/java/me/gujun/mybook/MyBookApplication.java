package me.gujun.mybook;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * <Please describe the usage of this class>
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-17 16:56:58
 */
public class MyBookApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(getApplicationContext());
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