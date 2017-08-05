package com.princekumar.xyzreader.localdata;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import rx.schedulers.Schedulers;

/**
 * Created by princ on 05-08-2017.
 */

public class DatabaseHelper {
    private final BriteDatabase briteDb;

    public DatabaseHelper(DbOpenHelper dbOpenHelper) {
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        briteDb = sqlBrite.wrapDatabaseHelper(dbOpenHelper, Schedulers.io());
    }

    public BriteDatabase getBriteDb() {
        return briteDb;
    }
}
