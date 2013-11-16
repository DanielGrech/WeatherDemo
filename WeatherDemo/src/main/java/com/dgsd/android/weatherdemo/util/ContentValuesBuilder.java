package com.dgsd.android.weatherdemo.util;

import android.content.ContentValues;
import com.dgsd.android.weatherdemo.data.DbField;

/**
 * Helper class to make working with {@link android.content.ContentValues} easier
 */
public class ContentValuesBuilder {
    private ContentValues mValues;

    public ContentValuesBuilder() {
        mValues = new ContentValues();
    }

    public ContentValuesBuilder put(DbField key, int value) {
        return put(key.getName(), value);
    }

    public ContentValuesBuilder put(DbField key, float value) {
        return put(key.getName(), value);
    }

    public ContentValuesBuilder put(DbField key, double value) {
        return put(key.getName(), value);
    }

    public ContentValuesBuilder put(DbField key, String value) {
        return put(key.getName(), value);
    }

    public ContentValuesBuilder put(DbField key, Enum value) {
        return put(key.getName(), value);
    }

    public ContentValuesBuilder put(String key, int value) {
        mValues.put(key, value);
        return this;
    }

    public ContentValuesBuilder put(String key, long value) {
        mValues.put(key, value);
        return this;
    }

    public ContentValuesBuilder put(String key, float value) {
        mValues.put(key, value);
        return this;
    }

    public ContentValuesBuilder put(String key, double value) {
        mValues.put(key, value);
        return this;
    }

    public ContentValuesBuilder put(String key, String value) {
        mValues.put(key, value);
        return this;
    }

    public ContentValuesBuilder put(String key, Enum eVal) {
        mValues.put(key, eVal.ordinal());
        return this;
    }

    public ContentValues build() {
        return mValues;
    }
}
