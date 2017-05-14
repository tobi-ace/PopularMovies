package com.tobi_ace.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tobi_ace.popularmovies.adapters.ReviewAdapter;
import com.tobi_ace.popularmovies.adapters.TrailerAdapter;
import com.tobi_ace.popularmovies.models.Movie;
import com.tobi_ace.popularmovies.models.Review;
import com.tobi_ace.popularmovies.models.Trailer;
import com.tobi_ace.popularmovies.utils.Constants;
import com.tobi_ace.popularmovies.utils.NetworkUtils;
import com.tobi_ace.popularmovies.utils.PopularMoviesJsonUtils;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;

import static com.tobi_ace.popularmovies.data.FavoritesContract.FavoritesEntry;

public class MoviesDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Pair<ArrayList<Review>, ArrayList<Trailer>>> {

    private static final int TRAILERS_AND_REVIEWS_LOADER = 1998;
    private static final String TAG = MoviesDetailActivity.class.getSimpleName();
    private static final String THE_MOVIE = "the_current_movie";


    private Movie theMovie;

    private ImageView ivDetailImage;
    private ImageView ivBackdrop;
    private TextView tvMovieTitle;
    private TextView tvReleaseDate;
    private TextView tvPlotSynopsis;
    private TextView tvUserRating;
    private TextView errorText;
    private TextView reviewHeading;
    private ProgressBar progressBar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private LinearLayout trailersAndReviewsLayout;

    private RecyclerView trailerRecyclerView;
    private RecyclerView reviewRecyclerView;

    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;

    private LinearLayoutManager trailerLayoutManager;
    private LinearLayoutManager reviewLayoutManager;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(THE_MOVIE, theMovie);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_detail);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (savedInstanceState != null && savedInstanceState.containsKey(THE_MOVIE)) {

            theMovie = savedInstanceState.getParcelable(THE_MOVIE);
        } else {
            Intent intent = getIntent();
            theMovie = null;
            if (intent != null && intent.hasExtra(Constants.MOVIE_EXTRA)) {
                theMovie = intent.getParcelableExtra(Constants.MOVIE_EXTRA);
            }

        }


        settUpViews();

        loadTrailersAndReviews();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (theMovie.isFavorite()) {
            fab.setImageResource(R.drawable.ic_favorite_white_24dp);
        } else {
            fab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!theMovie.isFavorite()) {
                    addMovieToFavourites(theMovie);
                    fab.setImageResource(R.drawable.ic_favorite_white_24dp);
                    Snackbar.make(view, "Added to favourites :-)", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    private void settUpViews() {

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        trailersAndReviewsLayout = (LinearLayout) findViewById(R.id.trailers_and_reviews_layout);

        trailerRecyclerView = (RecyclerView) findViewById(R.id.rv_trailers);
        trailerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        reviewRecyclerView = (RecyclerView) findViewById(R.id.rv_reviews);
        reviewLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        errorText = (TextView) findViewById(R.id.trailer_error_text);
        progressBar = (ProgressBar) findViewById(R.id.trailer_progress_bar);

        reviewHeading = (TextView) findViewById(R.id.review_heading);
        ivBackdrop = (ImageView) findViewById(R.id.backdrop);
        ivDetailImage = (ImageView) findViewById(R.id.details_poster_image);
        tvMovieTitle = (TextView) findViewById(R.id.tv_movie_title);
        tvReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        tvPlotSynopsis = (TextView) findViewById(R.id.tv_plot_synopsis);
        tvUserRating = (TextView) findViewById(R.id.tv_user_rating);


        collapsingToolbarLayout.setTitle(theMovie.getOriginalTitle());

        Picasso.with(this).load(theMovie.getBackdropPath()).into(ivBackdrop);
        Picasso.with(this).load(theMovie.getPosterPath()).into(ivDetailImage);
        tvMovieTitle.setText(theMovie.getOriginalTitle());
        tvReleaseDate.setText(theMovie.getReleaseDate());
        tvPlotSynopsis.setText(theMovie.getOverview());
        tvUserRating.setText(theMovie.getOverallRating() + "/10");

    }

    private void addMovieToFavourites(Movie theMovie) {
        theMovie.setFavorite(true);
        ContentValues cv = new ContentValues();
        cv.put(FavoritesEntry.COLUMN_MOVIE_TITLE, theMovie.getOriginalTitle());
        cv.put(FavoritesEntry.COLUMN_MOVIE_ID, theMovie.getId());
        cv.put(FavoritesEntry.COLUMN_BACKDROP_PATH, theMovie.getBackdropPath());
        cv.put(FavoritesEntry.COLUMN_POSTER_PATH, theMovie.getPosterPath());
        cv.put(FavoritesEntry.COLUMN_OVERVIEW, theMovie.getOverview());
        cv.put(FavoritesEntry.COLUMN_VOTE_AVERAGE, theMovie.getOverallRating());
        cv.put(FavoritesEntry.COLUMN_RLEASE_DATE, theMovie.getOverallRating());

        Uri uri = getContentResolver().insert(FavoritesEntry.CONTENT_URI, cv);
        if (uri != null)
            Toast.makeText(this, "Successfully added to favorite!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
    }

    private void loadTrailersAndReviews() {
        LoaderManager manager = getSupportLoaderManager();
        Loader<ArrayList<Movie>> loader = manager.getLoader(TRAILERS_AND_REVIEWS_LOADER);

        if (loader == null) {
            manager.initLoader(TRAILERS_AND_REVIEWS_LOADER, null, this);
        } else {
            manager.restartLoader(TRAILERS_AND_REVIEWS_LOADER, null, this);
        }
    }

    private void prepareData(ArrayList<Review> reviews, ArrayList<Trailer> trailers) {
        errorText.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        reviewHeading.append(String.valueOf(reviews.size()));
        reviewAdapter = new ReviewAdapter(reviews);
        reviewRecyclerView.setAdapter(reviewAdapter);
        reviewRecyclerView.setHasFixedSize(true);
        reviewRecyclerView.setLayoutManager(reviewLayoutManager);
        reviewRecyclerView.setVisibility(View.VISIBLE);


        trailerAdapter = new TrailerAdapter(trailers, new TrailerAdapter.TrailerAdapterOnClickHandler() {
            @Override
            public void onClick(Trailer trailer) {
                Uri webpage = Uri.parse(trailer.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        trailerRecyclerView.setAdapter(trailerAdapter);
        trailerRecyclerView.setHasFixedSize(true);
        trailerRecyclerView.setVisibility(View.VISIBLE);
        trailerRecyclerView.setLayoutManager(trailerLayoutManager);
    }

    private void showErrorMessageView() {
        progressBar.setVisibility(View.INVISIBLE);
        trailersAndReviewsLayout.setVisibility(View.INVISIBLE);
        errorText.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<Pair<ArrayList<Review>, ArrayList<Trailer>>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Pair<ArrayList<Review>, ArrayList<Trailer>>>(this) {
            Pair<ArrayList<Review>, ArrayList<Trailer>> thePair;

            @Override
            protected void onStartLoading() {
                progressBar.setVisibility(View.VISIBLE);

                if (thePair != null) {
                    deliverResult(thePair);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Pair<ArrayList<Review>, ArrayList<Trailer>> loadInBackground() {
                ArrayList<Review> reviews = null;
                ArrayList<Trailer> trailers = null;

                URL reviewsRequestUrl = null;
                try {
                    reviewsRequestUrl = NetworkUtils.buildReviewUrl(theMovie);
                    URL trailersRequestUrl = NetworkUtils.buildTrailerUrl(theMovie);
                    String reviewsJSON = NetworkUtils.getResponseFromUrl(reviewsRequestUrl);
                    String trailersJSON = NetworkUtils.getResponseFromUrl(trailersRequestUrl);
                    reviews = PopularMoviesJsonUtils.getReviewsFromJson(reviewsJSON);
                    trailers = PopularMoviesJsonUtils.getTrailersFromJson(trailersJSON);
                    Log.e(TAG, "loadInBackground: " + trailers.get(0).getSite());
                    return new Pair<>(reviews, trailers);
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                    return null;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(Pair<ArrayList<Review>, ArrayList<Trailer>> data) {
                thePair = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Pair<ArrayList<Review>, ArrayList<Trailer>>> loader, Pair<ArrayList<Review>, ArrayList<Trailer>> data) {

        if (data == null) {
            showErrorMessageView();
        } else {
            if ((data.first != null) && (data.second != null)) {
                ArrayList<Review> reviews = data.first;
                ArrayList<Trailer> trailers = data.second;
                prepareData(reviews, trailers);
                Log.e(TAG, "onLoadFinished: " + trailers.get(0).getKey());
            } else {
                showErrorMessageView();
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Pair<ArrayList<Review>, ArrayList<Trailer>>> loader) {

    }
}
