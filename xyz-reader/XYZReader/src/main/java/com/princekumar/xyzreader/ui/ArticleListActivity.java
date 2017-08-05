package com.princekumar.xyzreader.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.princekumar.xyzreader.R;
import com.princekumar.xyzreader.localdata.DataManager;
import com.princekumar.xyzreader.utils.ArticlesAdapter;
import com.princekumar.xyzreader.utils.RequestState;
import com.princekumar.xyzreader.utils.RxUtils;

import javax.inject.Inject;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * An activity representing a list of Articles. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link ArticleDetailActivity} representing item details. On tablets, the
 * activity presents a grid of items as cards.
 */
public class ArticleListActivity extends  BaseActivity  implements ArticlesAdapter.OnArticleClickListener,SwipeRefreshLayout.OnRefreshListener {



    private Completable articlesSync;
    private Disposable articlesSubscribtion;

    @Inject
    DataManager dataManager;

    @BindInt(R.integer.list_column_count)
    int columnsCount;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private ArticlesAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);

        setContentView(R.layout.activity_article_list);
        ButterKnife.bind(this);

        articlesSync = dataManager
                .syncArticles()
                .doOnSubscribe(disposable -> Timber.d("Sync started..."))
                .doOnComplete(() -> Timber.d("Sync completed"));

        if (savedInstanceState == null && articlesSubscribtion == null) {
            articlesSubscribtion = articlesSync.subscribe();
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        int columnsCount = getResources().getInteger(R.integer.list_column_count);

        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(columnsCount, StaggeredGridLayoutManager.VERTICAL);
        handleLoadingIndicator(swipeRefreshLayout);

        adapter = new ArticlesAdapter(this);
        adapter.setHasStableIds(true);

        recyclerView.setLayoutManager(sglm);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        dataManager.getArticlesObservableStream()
                .doOnSubscribe(disposable -> adapter.clearList())
                .doOnNext(article -> Timber.tag("myxyzreader").d("Fetch article from db: " + article.title()))
                .subscribe(article -> adapter.addArticle(article));
    }

    private void handleLoadingIndicator(SwipeRefreshLayout layout) {
        dataManager.getRequestState().subscribe(state -> {
            switch (state) {
                case RequestState.IDLE:
                    break;
                case RequestState.LOADING:
                    layout.setRefreshing(true);
                    break;
                case RequestState.COMPLETED:
                    layout.setRefreshing(false);
                    break;
                case RequestState.ERROR:
                    layout.setRefreshing(false);
                    break;
            }
        });
    }


    @Override
    public void articleClicked(int id) {
        //Toast.makeText(this, "Article: " + String.valueOf(id), Toast.LENGTH_SHORT).show();
        startActivity(ArticleDetailActivity.prepareIntent(this, id));
    }

    @Override
    public void onRefresh() {
        if (articlesSubscribtion != null && !articlesSubscribtion.isDisposed()) {
            articlesSubscribtion.dispose();
        }
        articlesSubscribtion = articlesSync.subscribe();

    }
}