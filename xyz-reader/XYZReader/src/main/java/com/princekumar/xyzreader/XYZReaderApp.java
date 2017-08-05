package com.princekumar.xyzreader;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.princekumar.xyzreader.injections.components.ApplicationComponents;
import com.princekumar.xyzreader.injections.components.DaggerApplicationComponents;
import com.princekumar.xyzreader.injections.modules.ApplicationModule;

import timber.log.Timber;

/**
 * Created by princ on 05-08-2017.
 */

public class XYZReaderApp extends Application {
    private ApplicationComponents applicationComponent;

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

            applicationComponent = DaggerApplicationComponents.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();

            applicationComponent.inject(this);
        }
    }
    public static XYZReaderApp get(Context context) {
        return (XYZReaderApp) context.getApplicationContext();
    }

    public ApplicationComponents getComponent() {
        return applicationComponent;
    }
}
