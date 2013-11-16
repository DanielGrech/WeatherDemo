package com.dgsd.android.weatherdemo.util;

import android.util.Log;
import timber.log.Timber;

/**
 * Logger used in the release flavour of the application
 */
public class ReleaseLogger extends Timber.HollowTree {

    private String mTag;

    public ReleaseLogger(String tag) {
        this.mTag = tag;
    }

    @Override
    public void w(final String message, final Object... args) {
        Log.w(mTag, String.format(message, args));
    }

    @Override
    public void w(final Throwable t, final String message, final Object... args) {
        Log.w(mTag, String.format(message, args), t);
    }

    @Override
    public void e(final String message, final Object... args) {
        Log.e(mTag, String.format(message, args));
    }

    @Override
    public void e(final Throwable t, final String message, final Object... args) {
        Log.e(mTag, String.format(message, args), t);
    }
}
