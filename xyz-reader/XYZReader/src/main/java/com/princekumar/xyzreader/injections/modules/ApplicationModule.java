package com.princekumar.xyzreader.injections.modules;

import android.app.Application;
import android.content.Context;

import com.princekumar.xyzreader.injections.ApplicationContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by princ on 05-08-2017.
 */
@Module
public class ApplicationModule {
    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }
}
