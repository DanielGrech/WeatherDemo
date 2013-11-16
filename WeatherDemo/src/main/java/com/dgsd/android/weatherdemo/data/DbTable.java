package com.dgsd.android.weatherdemo.data;

import android.text.TextUtils;

public class DbTable {
 
    final String mName;
    final DbField[] mFields;
    final String[] mCustomScripts;
 
    String[] mFieldNames;
 
    /**
     * Static helper to aid creating new DbTable instances
     *
     * @param tableName
     *         The name of the new table to create
     *
     * @return A {@link Builder} object which can be used to construct a new {@link DbTable} instance
     */
    public static Builder with(String tableName) {
        return new Builder(tableName);
    }
 
 
    /**
     * @param n
     *         The name of the table
     * @param f
     *         Columns to create as part of the table
     */
    private DbTable(String n, DbField[] f) {
        this(n, f, (String[]) null);
    }
 
    /**
     * @param n
     *         The name of the table
     * @param f
     *         Columns to create as part of the table
     * @param cs
     *         Extra SQL which should be run after the table is created (such as constraints to enforce)
     */
    private DbTable(String n, DbField[] f, String... cs) {
        mName = n;
        mFields = f;
        mCustomScripts = cs;
    }
 
    public String getName() {
        return mName;
    }
 
    /**
     * @return The fields found in this table
     */
    public DbField[] getFields() {
        return mFields;
    }
 
    /**
     * @return The name of this table
     */
    @Override
    public String toString() {
        return mName;
    }
 
    /**
     * @return Column names found in this table
     */
    public String[] getFieldNames() {
        if (mFieldNames == null) {
            mFieldNames = new String[mFields.length];
 
            for (int i = 0, size = mFields.length; i < size; i++)
                mFieldNames[i] = mFields[i].getName();
        }
 
        return mFieldNames;
    }
 
    /**
     * @return An SQL string which, when executed, will drop the table from the database
     */
    public String getDropSql() {
        return "DROP TABLE " + mName;
    }
 
    /**
     * @return Any custom SQL scripts which should be executed after this table is created
     */
    public String[] getPostCreateSql() {
        return mCustomScripts;
    }
 
    /**
     * @return An SQL string which, when executed, will create the table from in database
     */
    public String getCreateSql() {
        StringBuilder builder = new StringBuilder().append("CREATE TABLE ").append(mName).append(" ").append("(");
 
        // Ensure that a comma does not appear on the last iteration
        String comma = "";
        DbField[] mFields = getFields();
        for (DbField field : mFields) {
            builder.append(comma);
            comma = ",";
 
            builder.append(field.getName());
            builder.append(" ");
            builder.append(field.getType());
            builder.append(" ");
 
            if (field.getConstraint() != null) {
                builder.append(field.getConstraint());
            }
        }
 
        builder.append(")");
 
        return builder.toString();
 
    }
 
    /**
     * Fluent wrapper used to create instances of {@link DbTable};
     */
    public static class Builder {
 
        private final String mTableName;
        private DbField[] mColumns;
        private String[] mScripts;
 
        /**
         * Construct a new builder for a table named <code>tableName</code>
         *
         * @param tableName
         *         The name of the new table to create
         */
        private Builder(String tableName) {
            if (TextUtils.isEmpty(tableName) || TextUtils.isEmpty(tableName.trim())) {
                throw new IllegalArgumentException("Must supply a non-empty name for this table");
            }
 
            mTableName = tableName;
        }
 
        /**
         * Set the columns which will be created on the new table
         *
         * @param columns
         *         The columns to be created with the table (in order)
         *
         * @return <code>this</code> object (for chaining)
         */
        public Builder columns(DbField... columns) {
            mColumns = columns;
            return this;
        }
 
        /**
         * Set the columns which will be created on the new table
         *
         * @param customScripts
         *         The scripts to be run on this table <i>after</i> it has been created
         *
         * @return <code>this</code> object (for chaining)
         */
        public Builder scripts(String... customScripts) {
            mScripts = customScripts;
            return this;
        }
 
        /**
         * @return An instance of {@link DbTable} with the properties used in this builder
         */
        public DbTable create() {
            if (mColumns == null || mColumns.length == 0) {
                throw new IllegalStateException("Cant create without setting columns");
            }
 
            return new DbTable(mTableName, mColumns, mScripts);
        }
    }
}