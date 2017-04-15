package com.tobi_ace.popularmovies.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by abdulgafar on 4/10/17.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie";

    private static final String TMDB_BASE_IMAGE_URL = "https://image.tmdb.org/t/p";

    private static final String API_KEY = ""; //API key goes here

    private static final String API_KEY_PARAM = "api_key";



    public static URL buildUrl(String requestType){
        Uri uri = null;
        switch (requestType){
            case Constants.TOP_RATED:
                uri = Uri.parse(TMDB_BASE_URL).buildUpon()
                        .appendPath(Constants.TOP_RATED)
                        .appendQueryParameter(API_KEY_PARAM,API_KEY)
                        .build();
                break;
            case Constants.POPULAR:
                uri = Uri.parse(TMDB_BASE_URL).buildUpon()
                        .appendPath(Constants.POPULAR)
                        .appendQueryParameter(API_KEY_PARAM,API_KEY)
                        .build();
                break;
        }
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String buildAbsolutePosterPath(String relativePosterPath){

        Uri imageUri = Uri.parse(TMDB_BASE_IMAGE_URL).buildUpon()
                .appendPath("w780")
                .appendPath(relativePosterPath.substring(1,relativePosterPath.length()








                )).build();
        return imageUri.toString();

    }

    public static String getResponseFromUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
