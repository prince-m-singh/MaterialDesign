package com.princekumar.xyzreader.localdata;

import android.content.Context;
import android.content.SharedPreferences;

import com.princekumar.xyzreader.injections.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by princ on 05-08-2017.
 */

@Singleton
public class PreferencesHelper {
    public static final String FILE_NAME = "xyz_preferences";

    private final SharedPreferences preferences;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }
}
