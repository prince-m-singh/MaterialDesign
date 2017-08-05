package com.princekumar.xyzreader.injections.modules;

import android.app.Application;
import android.content.Context;

import com.princekumar.xyzreader.injections.ApplicationContext;
import com.princekumar.xyzreader.remote.ArticlesService;
import com.princekumar.xyzreader.remote.ServiceFactory;

import javax.inject.Singleton;

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

    @Provides
    @Singleton
    ArticlesService provideArticlesService() {
        return ServiceFactory.createFrom(ArticlesService.class, ArticlesService.ENDPOINT);
    }

}
