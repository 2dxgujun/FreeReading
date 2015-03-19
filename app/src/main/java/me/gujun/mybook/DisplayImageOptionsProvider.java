package me.gujun.mybook;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * Display image options provider.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-18 20:27:30
 */
public class DisplayImageOptionsProvider {
    private static final DisplayImageOptions DISPLAY_OPTIONS
            = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.ic_launcher)
            .showImageForEmptyUri(R.mipmap.ic_launcher)
            .showImageOnFail(R.mipmap.ic_launcher)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();

    public static DisplayImageOptions getDisplayImageOptions() {
        return DISPLAY_OPTIONS;
    }
}