package com.dgsd.android.weatherdemo.model;

import android.test.AndroidTestCase;

import java.io.*;

/**
 * Created by daniel on 16/11/2013.
 */
public abstract class BaseModelTests extends AndroidTestCase {

    /**
     * @param resource the raw resource id of the file to read
     * @return A string representing the contents of the resource
     */
    protected String getJsonFromResource(int resource) {
        InputStream is = getContext().getResources().openRawResource(resource);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return writer.toString();
    }
}
