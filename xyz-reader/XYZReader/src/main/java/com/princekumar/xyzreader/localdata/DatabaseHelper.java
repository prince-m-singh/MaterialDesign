package com.princekumar.xyzreader.localdata;

import android.database.sqlite.SQLiteDatabase;

import com.princekumar.xyzreader.datamodel.Article;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Completable;
import io.reactivex.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by princ on 05-08-2017.
 */

@Singleton
public class DatabaseHelper {
    private final BriteDatabase briteDb;

    @Inject
    public DatabaseHelper(DbOpenHelper dbOpenHelper) {
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        briteDb = sqlBrite.wrapDatabaseHelper(dbOpenHelper, Schedulers.io());
    }

    public Completable saveArticlesToDatabase(Collection<Article> articles) {

        return Completable.create(e -> {

            BriteDatabase.Transaction transaction = briteDb.newTransaction();

            try {
                briteDb.delete(ArticlesDatabase.Table.TABLE_NAME, null);

                for (Article article : articles) {
                    long result = briteDb.insert(
                            ArticlesDatabase.Table.TABLE_NAME,
                            ArticlesDatabase.toContentValues(article),
                            SQLiteDatabase.CONFLICT_REPLACE
                    );

                    if (result < 0) {
                        e.onError(new Throwable("Can't insert values into db."));
                    }
                }

                transaction.markSuccessful();
                e.onComplete();
            } finally {
                transaction.end();
            }
        });
    }

    public Observable<List<Article>> getArticlesFromDatabase() {

        // rxJava1 Observable
        rx.Observable<List<Article>> listObservable =
                briteDb
                        .createQuery(ArticlesDatabase.Table.TABLE_NAME, "SELECT * FROM " + ArticlesDatabase.Table.TABLE_NAME)
                        .mapToList(ArticlesDatabase::parseCursor);

        // Convert to rxJava2
        return RxJavaInterop.toV2Observable(listObservable);
    }

    public Observable<Article> getSingleArticleFromDatabase(int id) {

        rx.Observable<Article> articleObservable = briteDb
                .createQuery(ArticlesDatabase.Table.TABLE_NAME,
                        "SELECT * FROM " + ArticlesDatabase.Table.TABLE_NAME + " WHERE server_id = ?",
                        String.valueOf(id))
                .mapToOne(ArticlesDatabase::parseCursor);

        return RxJavaInterop.toV2Observable(articleObservable);
    }

}
