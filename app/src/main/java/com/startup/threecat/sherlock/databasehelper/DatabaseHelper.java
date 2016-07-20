package com.startup.threecat.sherlock.databasehelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dell on 15-Jul-16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Sherlock.db";
    private static SQLiteDatabase dbWriter = null;
    private static SQLiteDatabase dbReader = null;
    private static DatabaseHelper databaseHelper = null;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLStatement.CREATE_TABLE_PERSONS);
        db.execSQL(SQLStatement.CREATE_TABLE_MOVEMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQLStatement.DELETE_TABLE_PERSONS);
        db.execSQL(SQLStatement.DELETE_TABLE_MOVEMENTS);
        onCreate(db);
    }

    /**
     * use pattern singleton yo acess database
     * Only create 1 object can access to database
     * Get Database writer to write into database
     * @param context
     * @return : 1 object of SQLiteDatabase
     */
    public static SQLiteDatabase getDbWriter(Context context) {
        if(dbWriter == null) {
            if(databaseHelper == null) {
                databaseHelper = new DatabaseHelper(context);
            }
            dbWriter = databaseHelper.getWritableDatabase();
        }
        return dbWriter;
    }

    /**
     * use pattern singleton yo acess database
     * Only create 1 object can access to database
     * Get Database reader to write into database
     * @param context
     * @return : 1 object of SQLiteDatabase
     */
    public static SQLiteDatabase getDbReader(Context context) {
        if(dbReader == null) {
            if(databaseHelper == null) {
                databaseHelper = new DatabaseHelper(context);
            }
            dbReader = databaseHelper.getReadableDatabase();
        }
        return dbReader;
    }
}
