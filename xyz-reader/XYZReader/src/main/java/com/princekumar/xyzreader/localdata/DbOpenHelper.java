package com.princekumar.xyzreader.localdata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by princ on 05-08-2017.
 */

public class DbOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "xyzreader.db";
    public static final int DATABASE_VERSION = 1;

    public DbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.beginTransaction();

        try {
            db.execSQL(ArticlesDatabase.Table.CREATE);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ArticlesDatabase.Table.TABLE_NAME);
        onCreate(db);
    }
}
