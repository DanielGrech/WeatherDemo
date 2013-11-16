package com.dgsd.android.weatherdemo.data;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * Base class for an Asynchronous loader which returns a list of objects
 */
public abstract class AsyncLoader<T> extends AsyncTaskLoader<T> {
    private T mData;
 
    private ContentObserver mContentObserver;
 
    public AsyncLoader(Context context) {
        super(context);
    }
 
    /**
     * @return {@link Uri} to notify when a change occurs
     */
    protected abstract Uri getContentUri();
 
    /* Runs on the UI thread */
    @Override
    public void deliverResult(T objects) {
        if (isReset()) {
            return;
        }
 
        mData = objects;
 
        if (isStarted()) {
            super.deliverResult(objects);
        }
    }
 
    @Override
    protected void onStartLoading() {
        if (hasData()) {
            // If we currently have a result available, deliver it
            // immediately.
            deliverResult(mData);
        }
 
        if (mContentObserver == null)
            mContentObserver = new ForceLoadContentObserver();
 
        if (getContentUri() != null)
            getContext().getContentResolver().registerContentObserver(getContentUri(), true, mContentObserver);
 
        if (!hasData())
            forceLoad();
    }
 
    /**
     * Must be called from the UI thread
     */
    @Override
    protected void onStopLoading() {
        cancelLoad();
    }
 
    @Override
    public void onContentChanged() {
        super.onContentChanged();
    }
 
    @Override
    protected void onReset() {
        super.onReset();
 
        onStopLoading();
 
        mData = null;
 
        if (mContentObserver != null && getContentUri() != null) {
            getContext().getContentResolver().unregisterContentObserver(mContentObserver);
            mContentObserver = null;
        }
    }
 
    private boolean hasData() {
        if (mData == null) {
            return false;
        }
 
        if (mData instanceof Collection) {
            return !((Collection) mData).isEmpty();
        }
 
        if (mData.getClass().isArray()) {
            return Array.getLength(mData) != 0;
        }
 
        return true;
    }
}