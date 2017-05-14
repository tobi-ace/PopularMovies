package com.tobi_ace.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tobi_ace.popularmovies.adapters.MovieAdapter;
import com.tobi_ace.popularmovies.adapters.MovieAdapter.MovieAdapterOnClickHandler;
import com.tobi_ace.popularmovies.models.Movie;
import com.tobi_ace.popularmovies.utils.Constants;
import com.tobi_ace.popularmovies.utils.NetworkUtils;
import com.tobi_ace.popularmovies.utils.PopularMoviesJsonUtils;

import java.net.URL;
import java.util.ArrayList;

import static com.tobi_ace.popularmovies.data.FavoritesContract.FavoritesEntry;

public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String POPULAR = Constants.POPULAR;
    private static final String TOP_RATED = Constants.TOP_RATED;
    private static final String FAVORITE = Constants.FAVORITE;
    private static final String SORT_TYPE_EXTRA = "sort_type";
    private static final String BUNDLE_RECYCLER_LAYOUT = "recycler_view";
    private static final String SORT_ORDER_KEY = "sort_order";


    private static final int MOVIES_LOADER = 11;
    private static final int FAVORITES_LOADER = 12;

    private String mSortOrder;

    private MovieAdapter adapter;
    private RecyclerView recyclerView;
    private TextView errorText;
    private ProgressBar progressBar;
    private GridLayoutManager layoutManager;


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(BUNDLE_RECYCLER_LAYOUT)) {

                Parcelable savedRecylerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
                layoutManager.onRestoreInstanceState(savedRecylerLayoutState);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, layoutManager.onSaveInstanceState());
        outState.putString(SORT_ORDER_KEY, mSortOrder);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean orienrtationIsPotrait = this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        int GRID_SPAN_COUNT = orienrtationIsPotrait ? 2 : 4;

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        errorText = (TextView) findViewById(R.id.tv_error_text);
        layoutManager = new GridLayoutManager(this, GRID_SPAN_COUNT);

        recyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        progressBar.setVisibility(View.INVISIBLE);
        getSupportLoaderManager().initLoader(MOVIES_LOADER, null, this);

        if (savedInstanceState != null) {

            String sortType = savedInstanceState.getString(SORT_ORDER_KEY);
            if (sortType.equals(FAVORITE)) {
                loadFavoriteMovies();
            } else {
                loadMovies(sortType);
            }
        } else {
            loadMovies(POPULAR);
        }
    }

    private void prepareData(ArrayList<Movie> movies) {
        progressBar.setVisibility(View.INVISIBLE);
        adapter = new MovieAdapter(movies, MainActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        errorText.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void loadMovies(String sortType) {

        mSortOrder = sortType;
        Bundle bundle = new Bundle();
        bundle.putString(SORT_TYPE_EXTRA, sortType);

        LoaderManager manager = getSupportLoaderManager();
        Loader loader = manager.getLoader(MOVIES_LOADER);

        if (loader == null) {
            manager.initLoader(MOVIES_LOADER, bundle, this);
        } else {
            manager.restartLoader(MOVIES_LOADER, bundle, this);
        }

    }

    private void loadFavoriteMovies() {

        mSortOrder = FAVORITE;
        LoaderManager manager = getSupportLoaderManager();
        Loader loader = manager.getLoader(MOVIES_LOADER);

        if (loader == null) {
            manager.initLoader(FAVORITES_LOADER, null, this);
        } else {
            manager.restartLoader(FAVORITES_LOADER, null, this);
        }
    }

    private void showErrorMessageView() {
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        errorText.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(MainActivity.this, MoviesDetailActivity.class);
        intent.putExtra(Constants.MOVIE_EXTRA, movie);
        startActivity(intent);
    }

    @Override
    public Loader onCreateLoader(int id, final Bundle args) {

        switch (id) {
            case MOVIES_LOADER:

                return new AsyncTaskLoader<ArrayList<Movie>>(this) {
                    ArrayList<Movie> movies;

                    @Override
                    protected void onStartLoading() {
                        if (args == null) {
                            return;
                        }
                        progressBar.setVisibility(View.VISIBLE);

                        if (movies != null) {
                            deliverResult(movies);
                        } else {
                            forceLoad();
                        }
                    }

                    @Override
                    public ArrayList<Movie> loadInBackground() {
                        ArrayList<Movie> fetchedMovies = null;
                        String sortType = args.getString(SORT_TYPE_EXTRA);
                        URL movieRequsetUrl = NetworkUtils.buildUrl(sortType);
                        String JsonResponse = null;
                        try {
                            JsonResponse = NetworkUtils.getResponseFromUrl(movieRequsetUrl);
                            fetchedMovies = PopularMoviesJsonUtils.getMoviesFromJson(JsonResponse);
                            return fetchedMovies;
                        } catch (Exception e) {
                            Log.e(TAG, "loadInBackground: Unable to get movies");
                            e.printStackTrace();
                            return null;
                        }

                    }

                    @Override
                    public void deliverResult(ArrayList<Movie> data) {
                        movies = data;
                        super.deliverResult(data);
                    }
                };


            case FAVORITES_LOADER:

                return new AsyncTaskLoader<Cursor>(this) {

                    Cursor cursor;

                    @Override
                    protected void onStartLoading() {

                        progressBar.setVisibility(View.VISIBLE);

                        if (cursor != null) {
                            deliverResult(cursor);
                        } else {
                            forceLoad();
                        }

                    }

                    @Override
                    public Cursor loadInBackground() {

                        try {
                            return getContentResolver().query(
                                    FavoritesEntry.CONTENT_URI,
                                    null,
                                    null,
                                    null,
                                    FavoritesEntry._ID
                            );
                        } catch (Exception e) {
                            Log.e(TAG, "loadInBackground: Unable to load data");
                            e.printStackTrace();
                            return null;
                        }

                    }

                    @Override
                    public void deliverResult(Cursor data) {

                        cursor = data;

                        super.deliverResult(data);
                    }
                };


        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

        int id = loader.getId();
        ArrayList<Movie> movies;
        switch (id) {
            case MOVIES_LOADER:

                movies = (ArrayList<Movie>) data;
                progressBar.setVisibility(View.INVISIBLE);
                if (movies != null) {
                    prepareData(movies);
                } else {
                    showErrorMessageView();
                }

                break;
            case FAVORITES_LOADER:

                Cursor cursor = (Cursor) data;
                movies = new ArrayList<Movie>();

                while (cursor.moveToNext()) {
                    Movie movie = new Movie();
                    movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(FavoritesEntry.COLUMN_MOVIE_TITLE)));
                    movie.setId(cursor.getInt(cursor.getColumnIndex(FavoritesEntry.COLUMN_MOVIE_ID)));
                    movie.setPosterPath(cursor.getString(cursor.getColumnIndex(FavoritesEntry.COLUMN_POSTER_PATH)));
                    movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(FavoritesEntry.COLUMN_BACKDROP_PATH)));
                    movie.setOverview(cursor.getString(cursor.getColumnIndex(FavoritesEntry.COLUMN_OVERVIEW)));
                    movie.setOverallRating(cursor.getString(cursor.getColumnIndex(FavoritesEntry.COLUMN_VOTE_AVERAGE)));
                    movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(FavoritesEntry.COLUMN_RLEASE_DATE)));
                    movie.setFavorite(true);
                    movies.add(movie);
                }

                if (movies != null) {
                    prepareData(movies);
                } else {
                    showErrorMessageView();
                }
                ;
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_show_most_popular) {
            loadMovies(POPULAR);
            return true;
        } else if (itemId == R.id.action_show_top_rated) {
            loadMovies(TOP_RATED);
        } else if (itemId == R.id.action_show_favorites) {
            loadFavoriteMovies();
        }
        return super.onOptionsItemSelected(item);
    }


}
