package com.princekumar.xyzreader.localdata;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.princekumar.xyzreader.datamodel.Article;
import com.princekumar.xyzreader.remote.ArticlesService;
import com.princekumar.xyzreader.utils.RequestState;
import com.princekumar.xyzreader.utils.RxUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * Created by princ on 05-08-2017.
 */

@Singleton
public class DataManager {
    private final ArticlesService articlesService;
    private final DatabaseHelper databaseHelper;
    private final BehaviorRelay<Integer> requestState;
   // private final PreferencesHelper preferencesHelper;

    @Inject
    public DataManager(ArticlesService articlesService, DatabaseHelper databaseHelper, BehaviorRelay<Integer> requestStater) {
        this.articlesService = articlesService;
        this.databaseHelper = databaseHelper;
        this.requestState=requestStater;
        //this.preferencesHelper = preferencesHelper;
    }

    public Observable<Article> loadArticlesRemote() {
        return articlesService
                .loadArticles()
                .flatMap(Observable::fromIterable);
    }

    private void publishRequestState(@RequestState.State int state) {
        Observable.just(state)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(requestState);
    }


    public Completable syncArticles() {
        return articlesService
                .loadArticles()
                .compose(RxUtils.applySchedulers())
                .doOnSubscribe(disposable -> publishRequestState(RequestState.LOADING))
                .doOnError(throwable -> publishRequestState(RequestState.ERROR))
                .doOnComplete(() -> publishRequestState(RequestState.COMPLETED))
                .flatMapCompletable(databaseHelper::saveArticlesToDatabase);
    }

    public Observable<Article> getArticlesObservableStream() {
        return databaseHelper
                .getArticlesFromDatabase()
                .compose(RxUtils.applySchedulers())
                .flatMap(Observable::fromIterable)
                .distinct();

    }


    public Single<Article> getArticleSingle(int id) {
        return databaseHelper
                .getSingleArticleFromDatabase(id)
                .compose(RxUtils.applySchedulers())
                .firstOrError();
    }

    public BehaviorRelay<Integer> getRequestState() {
        return requestState;
    }
}
