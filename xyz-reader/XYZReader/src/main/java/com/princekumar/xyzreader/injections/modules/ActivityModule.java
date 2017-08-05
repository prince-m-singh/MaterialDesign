package com.princekumar.xyzreader.injections.modules;

import android.app.Activity;
import android.content.Context;

import com.princekumar.xyzreader.injections.ActivityContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by princ on 05-08-2017.
 */
@Module
public class ActivityModule {
    private Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return activity;
    }

    @Provides
    Activity provideActivity() {
        return activity;
    }
}
