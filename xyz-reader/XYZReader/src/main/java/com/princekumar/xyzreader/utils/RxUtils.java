package com.princekumar.xyzreader.utils;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by princ on 05-08-2017.
 */

public class RxUtils {

    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
