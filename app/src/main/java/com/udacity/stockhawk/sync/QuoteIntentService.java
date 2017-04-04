package com.udacity.stockhawk.sync;

import android.app.IntentService;
import android.content.Intent;

import com.udacity.stockhawk.R;

import timber.log.Timber;


public class QuoteIntentService extends IntentService {

    public QuoteIntentService() {
        super(QuoteIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Timber.d(getBaseContext().getString(R.string.intent_handled));
        QuoteSyncJob.getQuotes(getApplicationContext());
    }
}
