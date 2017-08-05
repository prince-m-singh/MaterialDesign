package com.princekumar.xyzreader.utils;

import com.ryanharter.auto.value.moshi.MoshiAdapterFactory;
import com.squareup.moshi.JsonAdapter;

/**
 * Created by princ on 05-08-2017.
 */

@MoshiAdapterFactory
public abstract class MyAdapterFactory implements JsonAdapter.Factory {
    public static JsonAdapter.Factory create() {
        return new AutoValueMoshi_MyAdapterFactory();
    }
}