 package com.dgsd.android.weatherdemo.util;

 import android.content.ContentResolver;
 import android.content.ContentValues;
 import android.content.Context;
 import android.content.CursorLoader;
 import android.database.Cursor;
 import android.database.DatabaseUtils;
 import android.database.sqlite.SQLiteDatabase;
 import android.database.sqlite.SQLiteStatement;
 import android.net.Uri;
 import android.text.TextUtils;
 import com.dgsd.android.weatherdemo.data.DbField;
 import com.dgsd.android.weatherdemo.data.DbTable;
 import timber.log.Timber;

 import java.util.Random;

 public class ProviderUtils {
    /**
     * Pool of {@link StringBuilder} objects for reuse
     */
    private static final ObjectPool<StringBuilder> sSbPool = new ObjectPool<StringBuilder>() {
        @Override
        protected StringBuilder newObject() {
            return new StringBuilder(100);
        }
 
        @Override
        protected void onRecycle(StringBuilder sb) {
            sb.setLength(0);
        }
    };
 
    /**
     * Used to generate random id's
     */
    private static final Random RANDOM = new Random();
 
    /**
     * Whilst it would be better to let SQLite3 generate its own id's where possible
     * (using an auto-incrementing primary key), sometimes it is exceedingly useful
     * to know the id of a row beforehand.
     *
     * @return A random value suitable for use as an id.
     */
    public static long generateId() {
        return RANDOM.nextInt(Integer.MAX_VALUE);
    }
 
    /**
     * Simulates the SQL 'merge' (or 'upsert') command from other (real) databases, as it is not
     * provided by SQLite3
     *
     * @param db
     *         The database to insert into
     * @param table
     *         Table to insert values
     * @param values
     *         New values to insert
     * @param fieldsToMatch
     *         Which fields constitute a match.
     *         In the simplest case, this will just be the primary key and hence can be omitted
     *
     * @return
     */
    public static long upsert(SQLiteDatabase db, DbTable table, ContentValues values, DbField... fieldsToMatch) {
        long newId = -1;
        if (values != null) {
            boolean containsAllFieldsToMatch = true;
            if (fieldsToMatch != null && fieldsToMatch.length > 0) {
                for (DbField field : fieldsToMatch) {
                    if (!values.containsKey(field.getName())) {
                        containsAllFieldsToMatch = false;
                        break;
                    }
                }
            } else {
                containsAllFieldsToMatch = false;
            }
 
            if (containsAllFieldsToMatch) {
                final String[] tableFields = table.getFieldNames();
 
                final StringBuilder sb = sSbPool.retrieve();
 
                sb.append("INSERT OR REPLACE INTO ").append(table)
                        .append(" (").append(TextUtils.join(", ", tableFields)).append(")");
                sb.append(" VALUES ");
                sb.append("(");
 
                for (int i = 0, len = tableFields.length; i < len; i++) {
                    if (i != 0)
                        sb.append(" ,");
 
                    if (values.containsKey(tableFields[i])) {
                        sb.append("?");
                    } else {
                        sb.append(" (SELECT ").append(tableFields[i])
                                .append(" FROM ").append(table)
                                .append(" WHERE ");
 
                        for (int j = 0, fieldLen = fieldsToMatch.length; j < fieldLen; j++) {
                            if (j != 0)
                                sb.append(" AND ");
 
                            sb.append(fieldsToMatch[j]);
                            sb.append(" = ");
 
                            final String val = values.getAsString(fieldsToMatch[j].getName());
                            if (val == null)
                                sb.append(val);
                            else
                                sb.append(DatabaseUtils.sqlEscapeString(values.getAsString(fieldsToMatch[j].getName())));
                        }
 
                        sb.append(")");
                    }
                }
 
                sb.append(")");
 
                SQLiteStatement statement = db.compileStatement(sb.toString());
 
                int ac = 1;
                for (String field : tableFields)
                    if (values.containsKey(field))
                        bind(statement, ac++, values.getAsString(field));
 
                newId = statement.executeInsert();
 
                sSbPool.save(sb);
            } else {
                newId = db.replaceOrThrow(table.getName(), null, values);
            }
        }
 
        return newId;
    }
 
    /**
     * Null-safe argument binding
     *
     * @param statement
     *         Statement to bind too
     * @param bindArg
     *         The index of the bind arguement to insert <code>value</code> at
     * @param value
     *         The value to bind to the statement
     */
    public static void bind(SQLiteStatement statement, int bindArg, String value) {
        if (value == null)
            statement.bindNull(bindArg);
        else
            statement.bindString(bindArg, value);
    }
 
    /**
     * Returns a comma-separated string for '?' for use in a selection statement
     *
     * @param size
     *         The number of arguments
     *
     * @return A comma-separated string of '?'
     */
    public static String makeArgString(int size) {
        StringBuilder sb = new StringBuilder();
 
        for (int i = 0; i < size; i++) {
            if (i != 0)
                sb.append(',');
 
            sb.append('?');
        }
 
        return sb.toString();
    }
 
    public static QueryBuilder query(Uri uri) {
        return new QueryBuilder(uri);
    }
 
    public static UpdateBuilder update(Uri uri) {
        return new UpdateBuilder(uri);
    }
 
    /**
     * Provides a fluent wrapper to create & execute update
     * commands on a given {@link Uri}
     */
    public static class UpdateBuilder {
        private final Uri mUri;
        private ContentValues mValues;
        private String mSelection;
        private String[] mSelectionArgs;
 
        /**
         * @param uri
         *         The uri to run the update command against
         */
        private UpdateBuilder(Uri uri) {
            mUri = uri;
        }
 
        /**
         * The SQL 'values' clause.
         *
         * @param values
         *         The values to set in this update statement
         *
         * @return This {@link UpdateBuilder} object to build upon
         */
        public UpdateBuilder set(ContentValues values) {
            mValues = values;
            return this;
        }
 
        /**
         * The SQL 'WHERE' clause.
         *
         * @param sel
         *         Selection string. All '?' found in the string will be substituted with their
         *         corresponding index in <code>selArgs</code>
         * @param selArgs
         *         The arguments to substitute for '?' in the selection string
         *
         * @return This {@link UpdateBuilder} object to build upon
         */
        public UpdateBuilder where(String sel, String... selArgs) {
            mSelection = sel;
            mSelectionArgs = selArgs;
 
            return this;
        }
 
        /**
         * Commit the update statement built up with this object
         * to the underlying content provider
         *
         * @param cr
         *         A {@link ContentResolver} instance to run the commit against
         */
        public void commit(ContentResolver cr) {
            if (mValues.size() > 0) {
                cr.update(mUri, mValues, mSelection, mSelectionArgs);
            } else {
                Timber.w("Tried to commit update with no values. [mUri = " + mUri + "]");
            }
        }
    }
 
    /**
     * Providers a fluent wrapper to build database queries
     * and represent them as a {@link android.database.Cursor} or
     * {@link android.support.v4.content.CursorLoader} instance
     */
    public static class QueryBuilder {
        private Uri mUri;
        private String mSelection;
        private String[] mSelectionArgs;
        private String[] mProjection;
        private String mSort;
        private int mLimit = -1;
 
        /**
         * @param uri
         *         The uri to query against
         */
        private QueryBuilder(Uri uri) {
            mUri = uri;
        }
 
        /**
         * The SQL 'WHERE' clause.
         *
         * @param sel
         *         Selection string. All '?' found in the string will be substituted with their
         *         corresponding index in <code>selArgs</code>
         * @param selArgs
         *         The arguments to substitute for '?' in the selection string
         *
         * @return This {@link QueryBuilder} object to build upon
         */
        public QueryBuilder where(String sel, String... selArgs) {
            mSelection = sel;
            mSelectionArgs = selArgs;
 
            return this;
        }
 
        /**
         * The SQL 'SELECT' clause.
         *
         * @param columns
         *         The columns to return from the database query
         *
         * @return This {@link QueryBuilder} object to build upon
         */
        public QueryBuilder select(String... columns) {
            mProjection = columns;
            return this;
        }
 
        /**
         * The SQL 'ORDER BY' clause.
         *
         * @param sort
         *         The sort argument to use for the query
         *
         * @return This {@link QueryBuilder} object to build upon
         */
        public QueryBuilder sort(String sort) {
            mSort = sort;
            return this;
        }
 
        /**
         * The SQL 'LIMIT' clause. Note that it's upto the underlying {@link android.content.ContentProvider}
         * to actually implement the limit clause
         *
         * @param limit
         *         The limit to impose on query results
         *
         * @return This {@link QueryBuilder} object to build upon
         */
        public QueryBuilder limit(int limit) {
            mLimit = limit;
            return this;
        }
 
        /**
         * @return A cursor over the database results of the query
         */
        public Cursor cursor(Context context) {
            return context.getContentResolver().query(getUriWithQueryParams(), mProjection,
                    mSelection, mSelectionArgs, mSort);
        }
 
        /**
         * @return A CursorLoader over the database results of the query
         */
        public CursorLoader loader(Context context) {
            return new CursorLoader(context, getUriWithQueryParams(), mProjection, mSelection, mSelectionArgs, mSort);
        }
 
        private Uri getUriWithQueryParams() {
            Uri uri = mUri;
            if (mLimit > 0) {
                uri = mUri.buildUpon().appendQueryParameter("limit", String.valueOf(mLimit)).build();
            }
 
            return uri;
        }
    }
}