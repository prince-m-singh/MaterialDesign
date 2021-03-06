package com.princekumar.xyzreader.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.princekumar.xyzreader.R;
import com.princekumar.xyzreader.datamodel.Article;
import com.princekumar.xyzreader.localdata.DataManager;
import com.princekumar.xyzreader.utils.StringUtils;

import javax.inject.Inject;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 */
public class ArticleDetailActivity extends BaseActivity  implements
        AppBarLayout.OnOffsetChangedListener {

    private static final String ARTICLE_ID_KEY = "ARTICLE_ID_KEY";

    private int currentId;

    @Inject
    DataManager dataManager;

    @BindBool(R.bool.show_title_in_toolbar)
    boolean showTitleInToolbar;

    @BindView(R.id.details_toolbar)
    Toolbar detailsToolbar;
    @BindView(R.id.details_toolbar_collapsing)
    CollapsingToolbarLayout detailsToolbarCollapsing;
    @BindView(R.id.details_app_bar)
    AppBarLayout detailsAppbar;
    @BindView(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;
    @BindView(R.id.share_fab)
    FloatingActionButton shareFab;
    @BindView(R.id.photo)
    ImageView photoView;
    @BindView(R.id.article_title)
    TextView articleTitleView;
    @BindView(R.id.article_subtitle)
    TextView articleSubtitleView;
    @BindView(R.id.article_body)
    TextView articleBodyView;


    // --------- VIEW LIFECYCLE ---------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);

        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null && extras.containsKey(ARTICLE_ID_KEY)) {
            currentId = extras.getInt(ARTICLE_ID_KEY);
        }

        if (detailsToolbar != null) {
            setSupportActionBar(detailsToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            detailsToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            detailsToolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

        shareFab.setOnClickListener(v -> startActivity(
                Intent.createChooser(ShareCompat.IntentBuilder.from(ArticleDetailActivity.this)
                        .setType("text/plain")
                        .setText(articleBodyView.getText().toString())
                        .getIntent(), getString(R.string.action_share))));


        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX,
                                       int oldScrollY) {
                boolean scrollingDirection = scrollY > oldScrollY;

                if (scrollingDirection && shareFab.isShown()) {
                    shareFab.hide();
                } else {
                    shareFab.show();
                }
            }
        });

        dataManager
                .getArticleSingle(currentId)
                .doOnSubscribe(disposable -> supportPostponeEnterTransition())
                .doFinally(this::supportStartPostponedEnterTransition)
                .subscribe(this::updateActivityLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        detailsAppbar.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        detailsAppbar.removeOnOffsetChangedListener(this);
    }

    // --------- APPBAR SCROLLING ---------

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if (!showTitleInToolbar) {
            return;
        }

        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / maxScroll;
        float nontransparentAlpha = 1;
        final float fadeSpeedModifier = 4;

        articleSubtitleView.setAlpha(nontransparentAlpha - percentage * fadeSpeedModifier);
    }

    // --------- UTILITY FUNCTIONS ---------

    public static Intent prepareIntent(Context context, int id) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra(ARTICLE_ID_KEY, id);
        return intent;
    }

    private void updateActivityLayout(Article article) {
        String title = article.title();

        String subtitle = StringUtils.getFormattedDetailsSubtitle(
                article.published_date(),
                article.author());

        String photoUrl = article.photo();

        if (detailsToolbarCollapsing != null) {

            if (showTitleInToolbar) {
                detailsToolbarCollapsing.setTitle(title);
            } else {
                articleTitleView.setText(title);
            }

            articleSubtitleView.setText(subtitle);
        }

        String bookText = article.body();
        articleBodyView.setText(StringUtils.getFormattedBookText(bookText));

        Glide.clear(photoView);
        Glide.with(this)
                .load(photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photoView);

        Timber.d("Current id: " + currentId);
        Timber.d("Title: " + title);
        Timber.d("Subtitle: " + subtitle);
        Timber.d("URL: " + photoUrl);
    }
}
