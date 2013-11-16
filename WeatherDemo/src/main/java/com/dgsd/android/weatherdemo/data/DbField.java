package com.dgsd.android.weatherdemo.data;

public class DbField {
    private final String mName;
    private final String mType;
    private final String mConstraint;
 
    /**
     * Constructs a database field with a null (empty) constraint
     *
     * @param n
     *         The name of the field
     * @param t
     *         The data type of the field
     */
    public DbField(String n, String t) {
        this(n, t, null);
    }
 
    /**
     * @param n
     *         The name of the field
     * @param t
     *         The data type of the field
     * @param c
     *         The constraint to add of the field (Eg <code>"DEFAULT 1"</code>)
     */
    public DbField(String n, String t, String c) {
        mName = n;
        mType = t;
        mConstraint = c;
    }
 
    @Override
    public String toString() {
        return mName;
    }
 
    public String getName() {
        return mName;
    }
 
    public String getType() {
        return mType;
    }
 
    public String getConstraint() {
        return mConstraint;
    }
}