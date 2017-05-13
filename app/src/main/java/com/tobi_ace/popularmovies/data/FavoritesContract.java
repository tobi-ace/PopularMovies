package com.tobi_ace.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by abdulgafar on 5/7/17.
 */

public class FavoritesContract {

    public static final String AUTHORITY = "com.tobi_ace.popularmovies";

    public final static Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public final static String PATH_FAVORITES = "favorites";


    public static final class FavoritesEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();


        public static final String TABLE_NAME = "table_of_favorites";
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_MOVIE_ID = "id";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_VOTE_AVERAGE = "overall_rating";
        public static final String COLUMN_RLEASE_DATE = "release_date";


    }
}
