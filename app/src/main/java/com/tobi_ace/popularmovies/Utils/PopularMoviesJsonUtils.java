package com.tobi_ace.popularmovies.Utils;

import android.util.Log;

import com.tobi_ace.popularmovies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by abdulgafar on 4/10/17.
 */
/*
* Original Title
* Movie poster image thumbanail
* A plot synopsis (overview)
* user rating (vote average)
* release date
* */
public class PopularMoviesJsonUtils {

    private static final String TMDB_MOVIES_ARRAY = "results";
    private static final String TMDB_ORIGINAL_TILTE = "original_title";
    private static final String TMDB_POSTER_PATH = "poster_path";
    private static final String TMDB_OVERVIEW = "overview";
    private static final String TMDB_VOTE_AVERAGE = "vote_average";
    private static final String TMDB_RELEASE_DATE = "release_date";
    private static final String TAG = PopularMoviesJsonUtils.class.getSimpleName();


    public static ArrayList<Movie> getMovieDataFromJson(String rawJSON) throws JSONException {



        ArrayList<Movie> fetchedMovies = new ArrayList<>();

        JSONObject movieObject = new JSONObject(rawJSON);
        JSONArray  moviesArray = movieObject.getJSONArray(TMDB_MOVIES_ARRAY);

        for (int i = 0; i < moviesArray.length(); i++) {

            JSONObject tempMovieObject = moviesArray.getJSONObject(i);

            String originalTitle = tempMovieObject.getString(TMDB_ORIGINAL_TILTE);
            String posterPath = tempMovieObject.getString(TMDB_POSTER_PATH);
            String overview = tempMovieObject.getString(TMDB_OVERVIEW);
            String overall_rating = tempMovieObject.getString(TMDB_VOTE_AVERAGE);
            String releaseDate = tempMovieObject.getString(TMDB_RELEASE_DATE);

            Movie currentMovie = new Movie(originalTitle,posterPath,overview,overall_rating,releaseDate);
            Log.i(TAG, "getMovieDataFromJson: "+currentMovie.getPosterPath()
                    +" movie created");
                fetchedMovies.add(currentMovie);


        }
        Log.i(TAG, "getMovieDataFromJson: "+ fetchedMovies.size());
        return fetchedMovies;
    }

}
