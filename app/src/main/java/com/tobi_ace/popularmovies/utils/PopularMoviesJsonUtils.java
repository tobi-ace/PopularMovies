package com.tobi_ace.popularmovies.utils;

import com.tobi_ace.popularmovies.models.Movie;
import com.tobi_ace.popularmovies.models.Review;
import com.tobi_ace.popularmovies.models.Trailer;

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

    private static final String TAG = PopularMoviesJsonUtils.class.getSimpleName();


    //Movies JSON keys
    private static final String TMDB_RESULTS_ARRAY = "results";
    private static final String TMDB_ORIGINAL_TILTE = "original_title";
    private static final String TMDB_POSTER_PATH = "poster_path";
    private static final String TMDB_BACKDROP_PATH = "backdrop_path";
    private static final String TMDB_OVERVIEW = "overview";
    private static final String TMDB_VOTE_AVERAGE = "vote_average";
    private static final String TMDB_RELEASE_DATE = "release_date";

    //REVIEWS JSON keys
    private static final String TMDB_MOVIE_ID = "id";
    private static final String REVIEW_AUTHOR = "author";
    private static final String REVIEW_CONTENT = "content";
    private static final String REVIEW_URL = "url";
    private static final String REVIEW_COUNT = "total_results";

    //VIDEO JSON KEYS
    private static final String VIDEO_ID = "id";
    private static final String VIDEO_NAME = "name";
    private static final String VIDEO_SITE = "site";
    private static final String VIDEO_KEY = "key";


    public static ArrayList<Movie> getMoviesFromJson(String rawJSON) throws JSONException {

        ArrayList<Movie> fetchedMovies = new ArrayList<>();

        JSONObject movieObject = new JSONObject(rawJSON);
        JSONArray moviesArray = movieObject.getJSONArray(TMDB_RESULTS_ARRAY);

        for (int i = 0; i < moviesArray.length(); i++) {

            JSONObject tempMovieObject = moviesArray.getJSONObject(i);
            Movie currentMovie = new Movie();

            String originalTitle = tempMovieObject.getString(TMDB_ORIGINAL_TILTE);
            currentMovie.setOriginalTitle(originalTitle);

            String posterPath = tempMovieObject.getString(TMDB_POSTER_PATH);
            currentMovie.setPosterPath(NetworkUtils.buildAbsoluteImagePath(posterPath));

            String overview = tempMovieObject.getString(TMDB_OVERVIEW);
            currentMovie.setOverview(overview);

            String overall_rating = tempMovieObject.getString(TMDB_VOTE_AVERAGE);
            currentMovie.setOverallRating(overall_rating);

            String releaseDate = tempMovieObject.getString(TMDB_RELEASE_DATE);
            currentMovie.setReleaseDate(releaseDate);

            int id = tempMovieObject.getInt(TMDB_MOVIE_ID);
            currentMovie.setId(id);

            String backDropPath = tempMovieObject.getString(TMDB_BACKDROP_PATH);
            currentMovie.setBackdropPath(NetworkUtils.buildAbsoluteImagePath(backDropPath));

            currentMovie.setFavorite(false);

            fetchedMovies.add(currentMovie);


        }
        return fetchedMovies;
    }

    public static ArrayList<Review> getReviewsFromJson(String rawJSON) throws JSONException {
        ArrayList<Review> fetchedReviews = new ArrayList<>();

        JSONObject reviewObject = new JSONObject(rawJSON);
        JSONArray reviewsArray = reviewObject.getJSONArray(TMDB_RESULTS_ARRAY);


        for (int i = 0; i < reviewsArray.length(); i++) {

            JSONObject tempReviewObject = reviewsArray.getJSONObject(i);

            String author = tempReviewObject.getString(REVIEW_AUTHOR);
            String content = tempReviewObject.getString(REVIEW_CONTENT);
            String url = tempReviewObject.getString(REVIEW_URL);

            Review currentReview = new Review(author, content, url);

            fetchedReviews.add(currentReview);


        }
        return fetchedReviews;
    }

    public static ArrayList<Trailer> getTrailersFromJson(String rawJSON) throws JSONException {
        ArrayList<Trailer> fetchedTrailers = new ArrayList<>();

        JSONObject trailerObject = new JSONObject(rawJSON);
        JSONArray trailersArray = trailerObject.getJSONArray(TMDB_RESULTS_ARRAY);

        for (int i = 0; i < trailersArray.length(); i++) {

            JSONObject tempTrailerObject = trailersArray.getJSONObject(i);

            String id = tempTrailerObject.getString(VIDEO_ID);
            String name = tempTrailerObject.getString(VIDEO_NAME);
            String site = tempTrailerObject.getString(VIDEO_SITE);
            String key = tempTrailerObject.getString(VIDEO_KEY);

            Trailer currentTrailer = new Trailer(id, name, site, key);

            fetchedTrailers.add(currentTrailer);


        }
        return fetchedTrailers;
    }
}
