package com.princekumar.xyzreader.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.princekumar.xyzreader.R;
import com.princekumar.xyzreader.localdata.DataManager;
import com.princekumar.xyzreader.utils.RxUtils;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * An activity representing a list of Articles. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link ArticleDetailActivity} representing item details. On tablets, the
 * activity presents a grid of items as cards.
 */
public class ArticleListActivity extends  BaseActivity {

    @Inject
    DataManager dataManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);

        dataManager.loadArticlesRemote()
                .compose(RxUtils.applySchedulers())
                .doOnSubscribe(disposable -> Timber.d("Start loading..."))
                .doOnComplete(() -> Timber.d("Completed!"))
                .doOnError(throwable -> Timber.d("Error!"))
                .subscribe(article -> Timber.d("Article: " + article.title()));
    }
}