package com.dgsd.android.weatherdemo.dao;

import android.content.ContentValues;

/**
 * Converts objects between their java and
 * database representations (as well as visa versa)
 */
public interface IDao<T> {

    /**
     * Convert the given content values into a java object
     *
     * @param values The database values holding the object data
     * @return An object constructed from <code>values</code>
     */
    public T build(ContentValues values);

    /**
     * Convert the given object into its database representation
     *
     * @param object The object to convert
     * @return A {@link android.content.ContentValues} instance representing the object
     */
    public ContentValues convert(T object);

}
