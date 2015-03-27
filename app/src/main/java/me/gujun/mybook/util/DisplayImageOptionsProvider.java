package me.gujun.mybook.util;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import me.gujun.mybook.R;

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
            .showImageOnLoading(R.drawable.ic_cover_default)
            .showImageForEmptyUri(R.drawable.ic_cover_default)
            .showImageOnFail(R.drawable.ic_cover_default)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();

    public static DisplayImageOptions getDisplayImageOptions() {
        return DISPLAY_OPTIONS;
    }
}