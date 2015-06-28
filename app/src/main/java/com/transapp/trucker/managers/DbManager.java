package com.transapp.trucker.managers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.transapp.trucker.db.FeedReaderDbHelper;

/**
 * Created by Miljan on 6/29/2015.
 */
public class DbManager {
    private static final String TAG = "DbManager";

    private SQLiteDatabase db;
    private FeedReaderDbHelper mDbHelper;
    private static DbManager dbManager;

    private DbManager(Context context) {
        mDbHelper = new FeedReaderDbHelper(context);
        db = mDbHelper.getWritableDatabase();
    }

    public static DbManager getInstance(Context context) {

        if (dbManager == null) {
            dbManager = new DbManager(context);
        }
        return dbManager;
    }

    public SQLiteOpenHelper getDbHelper() {
        return mDbHelper;
    }
}
