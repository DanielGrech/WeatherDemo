package com.dgsd.android.weatherdemo.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Window;
import com.dgsd.android.weatherdemo.WeatherApp;
import com.dgsd.android.weatherdemo.receiver.ApiBroadcastReceiver;
import com.squareup.otto.Bus;

import javax.inject.Inject;

/**
 *
 */
public class BaseActivity extends Activity {
    @Inject
    protected Bus mEventBus;

    protected WeatherApp mApp;

    private ApiBroadcastReceiver mApiReceiver = new ApiBroadcastReceiver() {
        @Override
        protected void onStart(final String token) {
            setProgressBarIndeterminateVisibility(getRunningCounter() > 0);
            onApiRequestStart(token);
        }

        @Override
        protected void onFinish(final String token) {
            setProgressBarIndeterminateVisibility(getRunningCounter() > 0);
            onApiRequestFinish(token);
        }

        @Override
        protected void onError(final String token, final String errorMsg) {
            onApiRequestError(token, errorMsg);
        }
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        mApp = (WeatherApp) getApplication();
        mApp.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mApiReceiver.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mApiReceiver.unregister(this);
    }

    protected void onApiRequestStart(String action) {
        //No-op
    }

    protected void onApiRequestFinish(String action) {
        //No-op
    }

    protected void onApiRequestError(String action, String errorMsg) {
        //No-op
    }

    protected void registerForApi(String token) {
        mApiReceiver.addAcceptableToken(token);
    }

    protected <T extends Fragment> T findFragment(int id) {
        return (T) getFragmentManager().findFragmentById(id);
    }
}
