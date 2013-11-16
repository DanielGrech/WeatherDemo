package com.dgsd.android.weatherdemo.dao;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Converts objects between their java and
 * database representations (as well as visa versa)
 */
public interface IDao<T> {

    /**
     * Convert the given content values into a java object
     *
     * @param cursor Cursor holding the object data
     * @return An object constructed from <code>values</code>
     */
    public T build(Cursor cursor);

    /**
     * Convert the given object into its database representation
     *
     * @param object The object to convert
     * @return A {@link android.content.ContentValues} instance representing the object
     */
    public ContentValues convert(T object);

}
