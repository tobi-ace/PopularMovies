package com.tobi_ace.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.tobi_ace.popularmovies.MovieAdapter.MovieAdapterOnClickHandler;
import com.tobi_ace.popularmovies.utils.Constants;
import com.tobi_ace.popularmovies.utils.NetworkUtils;
import com.tobi_ace.popularmovies.utils.PopularMoviesJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private final String POPULAR = Constants.POPULAR;
    private final String TOP_RATED = Constants.TOP_RATED;
    private final String SORT_TYPE_EXTRA = "sort_type";
    private final int MOVIES_LOADER = 11;


    private MovieAdapter adapter;
    private RecyclerView recyclerView;
    private TextView errorText;
    private ProgressBar progressBar;
    private GridLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        errorText = (TextView) findViewById(R.id.tv_error_text);
        layoutManager = new GridLayoutManager(this, 2);

        recyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        progressBar.setVisibility(View.INVISIBLE);
        getSupportLoaderManager().initLoader(MOVIES_LOADER, null, this);
        loadMovies(POPULAR);
    }

    private void prepareData(ArrayList<Movie> movies) {
        adapter = new MovieAdapter(movies,MainActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);errorText.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void loadMovies(String sortType) {

        Bundle bundle = new Bundle();
        bundle.putString(SORT_TYPE_EXTRA, sortType);

        LoaderManager manager = getSupportLoaderManager();
        Loader<ArrayList<Movie>> loader = manager.getLoader(MOVIES_LOADER);

        if (loader == null) {
            manager.initLoader(MOVIES_LOADER, bundle, this);
        } else {
            manager.restartLoader(MOVIES_LOADER, bundle, this);
        }

    }

    private void showErrorMessageView() {
        recyclerView.setVisibility(View.INVISIBLE);
        errorText.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
        intent.putExtra(Constants.MOVIE_EXTRA,movie);
        startActivity(intent);
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, final Bundle args) {
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
                    Log.e(TAG, "onStartLoading: Started Loading");
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
                    Log.e(TAG, "loadInBackground: " + JsonResponse);
                    fetchedMovies = PopularMoviesJsonUtils.getMovieDataFromJson(JsonResponse);
                    Log.e(TAG, "loadInBackground: movie" + fetchedMovies.get(0).getOriginalTitle());
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
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> movies) {
        progressBar.setVisibility(View.INVISIBLE);
        if (movies != null) {
            prepareData(movies);
        } else {
            showErrorMessageView();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

    }

    class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            ArrayList<Movie> fetchedMovies = null;
            String sortType = params[0];
            URL movieRequsetUrl= NetworkUtils.buildUrl(sortType);
            try {
                String JsonResponse = NetworkUtils.getResponseFromUrl(movieRequsetUrl);

                fetchedMovies = PopularMoviesJsonUtils.getMovieDataFromJson(JsonResponse);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return fetchedMovies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            progressBar.setVisibility(View.INVISIBLE);
            if (movies != null){
                prepareData(movies);
            }else{
                showErrorMessageView();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_show_most_popular){
            loadMovies(POPULAR);
            return true;
        }else if (itemId == R.id.action_show_top_rated){
            loadMovies(TOP_RATED);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager.setSpanCount(4);
        } else {
            layoutManager.setSpanCount(3);
        }
    }
}
