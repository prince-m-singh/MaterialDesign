package com.princekumar.xyzreader.remote;

import com.princekumar.xyzreader.datamodel.Article;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by princ on 05-08-2017.
 */

public interface ArticlesService {
    String ENDPOINT = "https://go.udacity.com";

    @GET("/xyz-reader-json")
    Observable<List<Article>> loadArticles();
}
