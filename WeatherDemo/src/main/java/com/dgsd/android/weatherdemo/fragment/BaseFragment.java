package com.dgsd.android.weatherdemo.fragment;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import com.dgsd.android.weatherdemo.WeatherApp;
import com.squareup.otto.Bus;

import javax.inject.Inject;

/**
 * Created by daniel on 17/11/2013.
 */
public class BaseFragment extends Fragment {

    @Inject
    protected Bus mEventBus;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final WeatherApp app = (WeatherApp) getActivity().getApplication();
        app.inject(this);
    }

    protected void reload(int loaderId, LoaderManager.LoaderCallbacks callbacks) {
        final Loader loader = getLoaderManager().getLoader(loaderId);
        if (loader == null) {
            getLoaderManager().initLoader(loaderId, null, callbacks);
        } else {
            getLoaderManager().restartLoader(loaderId, null, callbacks);
        }
    }
}
