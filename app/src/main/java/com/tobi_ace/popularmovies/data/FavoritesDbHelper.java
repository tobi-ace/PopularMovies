package com.tobi_ace.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tobi_ace.popularmovies.data.FavoritesContract.FavoritesEntry;

/**
 * Created by abdulgafar on 5/7/17.
 */

public class FavoritesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorites.db";

    private static final int VERSION = 1;

    public FavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = " CREATE TABLE " + FavoritesEntry.TABLE_NAME + " ( "
                + FavoritesEntry._ID + " INTEGER PRIMARY KEY, "
                + FavoritesEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, "
                + FavoritesEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
                + FavoritesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, "
                + FavoritesEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, "
                + FavoritesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, "
                + FavoritesEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, "
                + FavoritesEntry.COLUMN_RLEASE_DATE + " TEXT NOT NULL "
                + " ); ";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoritesEntry.TABLE_NAME);
        onCreate(db);
    }
}
