package com.dgsd.android.weatherdemo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import com.dgsd.android.weatherdemo.R;
import com.dgsd.android.weatherdemo.service.ApiExecutorService;

import java.util.HashSet;
import java.util.Set;

/**
 * Catches broadcasts sent at different lifecycle events
 * of Api requests as executed by {@link com.dgsd.android.weatherdemo.service.ApiExecutorService}
 */
public abstract class ApiBroadcastReceiver extends BroadcastReceiver {

    private Set<String> mAcceptableTokens = new HashSet<>();

    protected abstract void onStart(String token);

    protected abstract void onFinish(String token);

    protected abstract void onError(String token, String errorMsg);

    public void register(Context context) {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(ApiExecutorService.ACTION_API_START);
        filter.addAction(ApiExecutorService.ACTION_API_FINISH);
        filter.addAction(ApiExecutorService.ACTION_API_ERROR);
        LocalBroadcastManager.getInstance(context).registerReceiver(this, filter);
    }

    public void unregister(Context context) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
    }

    public void addAcceptableToken(String token) {
        mAcceptableTokens.add(token);
    }

    /**
     * Reference counter for the number of requests currently executing
     */
    private int mRunningCounter = 0;

    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        final String token = intent.getStringExtra(ApiExecutorService.EXTRA_TOKEN);
        if (action != null && token != null && mAcceptableTokens.contains(token)) {
            switch (action) {
                case ApiExecutorService.ACTION_API_START:
                    mRunningCounter++;
                    onStart(action);
                    break;

                case ApiExecutorService.ACTION_API_FINISH:
                    mRunningCounter--;
                    if (mRunningCounter < 0) {
                        mRunningCounter = 0;
                    }

                    onFinish(action);
                    break;

                case ApiExecutorService.ACTION_API_ERROR:
                    String errorMsg = intent.getStringExtra(ApiExecutorService.EXTRA_ERROR_MESSAGE);
                    if (TextUtils.isEmpty(errorMsg)) {
                        errorMsg = context.getString(R.string.unknown_error);
                    }

                    onError(action, errorMsg);

                    break;
            }
        }
    }

    /**
     * @return The number of currently executing requests
     */
    protected int getRunningCounter() {
        return mRunningCounter;
    }
}
