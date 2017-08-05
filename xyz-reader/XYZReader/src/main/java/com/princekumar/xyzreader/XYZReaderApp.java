package com.princekumar.xyzreader;

import android.app.Application;

import com.facebook.stetho.Stetho;

import timber.log.Timber;

/**
 * Created by princ on 05-08-2017.
 */

public class XYZReaderApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.uprootAll();
            Timber.plant(new Timber.DebugTree()
            {
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    // Add the line number
                    return String.format("[L:%s] [M:%s] [C:%s]",
                            element.getLineNumber(),
                            element.getMethodName(),
                            super.createStackElementTag(element));
                }
            });

            Stetho.initializeWithDefaults(this);
        }
    }
}
