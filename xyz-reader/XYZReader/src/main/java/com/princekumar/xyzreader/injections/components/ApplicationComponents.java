package com.princekumar.xyzreader.injections.components;

import android.app.Application;
import android.content.Context;

import com.princekumar.xyzreader.XYZReaderApp;
import com.princekumar.xyzreader.injections.ApplicationContext;
import com.princekumar.xyzreader.injections.modules.ApplicationModule;
import com.princekumar.xyzreader.localdata.DatabaseHelper;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by princ on 05-08-2017.
 */

@Singleton
@Component (modules = ApplicationModule.class)
public interface ApplicationComponents {

    void inject(XYZReaderApp xyzReaderApp);

    @ApplicationContext
    Context getContext();

    Application getApplication();


}
