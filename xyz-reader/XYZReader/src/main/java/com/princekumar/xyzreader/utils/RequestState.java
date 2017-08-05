package com.princekumar.xyzreader.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by princ on 05-08-2017.
 */


public class RequestState {

    public static final int IDLE = 0;
    public static final int LOADING = 1;
    public static final int COMPLETED = 2;
    public static final int ERROR = 3;

    @Retention(RetentionPolicy.CLASS)
    @IntDef({IDLE, LOADING, COMPLETED, ERROR})
    public @interface   State { }
}