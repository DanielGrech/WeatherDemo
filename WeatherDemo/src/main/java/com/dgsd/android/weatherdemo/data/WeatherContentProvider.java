package com.dgsd.android.weatherdemo.data;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import com.dgsd.android.weatherdemo.util.ProviderUtils;
import timber.log.Timber;

import java.sql.SQLException;

/**
 * Provies access to the underlying datastore
 */
public class WeatherContentProvider extends ContentProvider {

    private static final String AUTHORITY = "com.dgsd.android.weatherdemo.data.WeatherContentProvider";

    private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public static final Uri FORECAST_LOADER_URI = Uri.withAppendedPath(BASE_URI, "forecast_loader");

    public static final int TYPE_FORECASTS = 0x1;

    public static Uri FORECASTS_URI;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static Db mDb;

    private ContentResolver mContentResolver;

    static {
        FORECASTS_URI = Uri.withAppendedPath(BASE_URI, "forecasts");

        sURIMatcher.addURI(AUTHORITY, "forecasts", TYPE_FORECASTS);
    }

    @Override
    public boolean onCreate() {
        mDb = Db.get(getContext());
        mContentResolver = getContext().getContentResolver();
        return true;
    }

    @Override
    public String getType(final Uri uri) {
        return sURIMatcher.match(uri) == UriMatcher.NO_MATCH ?
                null : uri.toString();
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String sel, String[] selArgs, String sort) {
        try {

            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
            qb.setTables(getTableFromType(sURIMatcher.match(uri)).getName());

            Cursor cursor = qb.query(mDb.getReadableDatabase(),
                    projection, sel, selArgs, null, null, sort);

            if (cursor != null) {
                cursor.setNotificationUri(mContentResolver, uri);
            }
            return cursor;
        } catch (Exception e) {
            Timber.e(e, "Error quering database");
        }

        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDb.getWritableDatabase();

        try {
            final int type = sURIMatcher.match(uri);
            final DbTable table = getTableFromType(type);
            final DbField[] upsertFields = getUpsertFieldsFromType(type);

            final long id;
            if (upsertFields == null || upsertFields.length == 0) {
                id = db.replaceOrThrow(table.getName(), null, values);
            } else {
                id = ProviderUtils.upsert(db, table, values, upsertFields);
            }

            if (id > 0) {
                Uri newUri = ContentUris.withAppendedId(uri, id);
                mContentResolver.notifyChange(uri, null);
                return newUri;
            } else {
                throw new SQLException("Filed to insert row into " + uri);
            }

        } catch (Exception e) {
            Timber.e(e, "Error inserting into database");
        }

        return null;
    }

    @Override
    public int delete(Uri uri, String sel, String[] selArgs) {
        try {
            final int type = sURIMatcher.match(uri);
            final SQLiteDatabase db = mDb.getWritableDatabase();
            int rowsAffected = db.delete(getTableFromType(type).getName(), sel, selArgs);

            mContentResolver.notifyChange(uri, null);
            return rowsAffected;
        } catch (Exception e) {
            Timber.e(e, "Error deleting from database");
        }

        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String sel, String[] selArgs) {
        try {
            final int type = sURIMatcher.match(uri);
            final SQLiteDatabase db = mDb.getWritableDatabase();
            final int rowsAffected = db.update(getTableFromType(type).getName(), values, sel, selArgs);

            mContentResolver.notifyChange(uri, null);
            return rowsAffected;
        } catch (Exception e) {
            Timber.e(e, "Error updating database");
        }

        return 0;
    }

    private DbField[] getUpsertFieldsFromType(int type) {
        switch (type) {
            case TYPE_FORECASTS:
                return new DbField[]{Db.Field.DATE_STR};
            default:
                return null;
        }
    }

    private DbTable getTableFromType(int type) {
        switch (type) {
            case TYPE_FORECASTS:
                return Db.Table.FORECASTS;
            default:
                throw new IllegalArgumentException("Unrecognised uri type: " + type);
        }
    }
}
