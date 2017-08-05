package com.princekumar.xyzreader.remote;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.princekumar.xyzreader.utils.MyAdapterFactory;
import com.squareup.moshi.Moshi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by princ on 05-08-2017.
 */

public class ServiceFactory {
    public static <T> T createFrom(Class<T> serviceClass, String endpoint) {

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        Moshi moshi = new Moshi.Builder()
                .add(MyAdapterFactory.create())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endpoint)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build();

        return retrofit.create(serviceClass);
    }
}
