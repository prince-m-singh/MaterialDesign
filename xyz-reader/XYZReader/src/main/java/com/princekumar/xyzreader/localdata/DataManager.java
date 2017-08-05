package com.princekumar.xyzreader.localdata;

import com.princekumar.xyzreader.datamodel.Article;
import com.princekumar.xyzreader.remote.ArticlesService;
import com.princekumar.xyzreader.utils.RxUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;


/**
 * Created by princ on 05-08-2017.
 */

@Singleton
public class DataManager {
    private final ArticlesService articlesService;
    private final DatabaseHelper databaseHelper;
    private final PreferencesHelper preferencesHelper;

    @Inject
    public DataManager(ArticlesService articlesService, DatabaseHelper databaseHelper,PreferencesHelper preferencesHelper) {
        this.articlesService = articlesService;
        this.databaseHelper = databaseHelper;
        this.preferencesHelper = preferencesHelper;
    }

    public Observable<Article> loadArticlesRemote() {
        return articlesService
                .loadArticles()
                .flatMap(Observable::fromIterable);
    }

    public Completable syncArticles() {
        return articlesService
                .loadArticles()
                .compose(RxUtils.applySchedulers())
                .flatMapCompletable(databaseHelper::saveArticlesToDatabase);
    }

    public Observable<Article> getArticlesObservableStream() {
        return databaseHelper
                .getArticlesFromDatabase()
                .flatMap(Observable::fromIterable)
                .distinct();

    }


    public Single<Article> getArticleSingle(int id) {
        return databaseHelper
                .getSingleArticleFromDatabase(id)
                .firstOrError();
    }
}
