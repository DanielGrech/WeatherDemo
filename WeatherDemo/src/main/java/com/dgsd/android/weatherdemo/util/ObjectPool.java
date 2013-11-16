
package com.dgsd.android.weatherdemo.util;

import java.util.Stack;

/**
 * Simple object pool implementation
 */
public abstract class ObjectPool<T> {
 
    Stack<T> mObjects;
 
    /**
     * @return a new instance of type T
     */
    protected abstract T newObject();
 
    /**
     * Gives subclasses the chance to perform additional operations on an
     * object *before* it is recycled.
     *
     * @param obj
     */
    protected void onRecycle(T obj) {
        //Subclasses can override..
    }
 
    /**
     * Extract an instance from the pool
     *
     * @return A new or recycled instance of <code>T</code>
     */
    public final T retrieve() {
        if (mObjects == null || mObjects.isEmpty()) {
            return newObject();
        } else {
            T obj = mObjects.pop();
 
            onRecycle(obj);
 
            return obj;
        }
    }
 
    /**
     * Deposits an instance of <code>T</code> back into the pool
     *
     * @param obj
     *         Object to return to the pool
     */
    public final void save(T obj) {
        if (mObjects == null)
            mObjects = new Stack<T>();
 
        mObjects.push(obj);
    }
}