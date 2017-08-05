package com.princekumar.xyzreader.injections.components;

import com.princekumar.xyzreader.injections.PreActivity;
import com.princekumar.xyzreader.ui.ArticleDetailActivity;
import com.princekumar.xyzreader.ui.ArticleListActivity;
import com.princekumar.xyzreader.injections.modules.ActivityModule;

import dagger.Component;

/**
 * Created by princ on 05-08-2017.
 */

@PreActivity
@Component(dependencies = ApplicationComponents.class, modules = ActivityModule.class)
public interface ActivityComponents {

    void inject(ArticleListActivity articleListActivity);
    void inject(ArticleDetailActivity articleListActivity);

}