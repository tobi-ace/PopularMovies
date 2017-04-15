package com.tobi_ace.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tobi_ace.popularmovies.Utils.Constants;

public class MovieDetailActivity extends AppCompatActivity {

        ImageView ivDetailImage;
        TextView tvMovieTitle;
        TextView tvReleaseDate;
        TextView tvPlotSynopsis;
        TextView tvUserRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ivDetailImage = (ImageView) findViewById(R.id.iv_detail_image) ;
        tvMovieTitle = (TextView) findViewById(R.id.tv_movie_title);
        tvReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        tvPlotSynopsis = (TextView) findViewById(R.id.tv_plot_synopsis);
        tvUserRating = (TextView) findViewById(R.id.tv_user_rating);


        Movie theMovie =  getIntent().getParcelableExtra(Constants.MOVIE_EXTRA);
        Picasso.with(MovieDetailActivity.this).load(theMovie.getPosterPath()).into(ivDetailImage);
        tvMovieTitle.setText(theMovie.getOriginalTitle());
        tvReleaseDate.setText(theMovie.getReleaseDate());
        tvPlotSynopsis.setText(theMovie.getOverview());
        tvUserRating.setText(theMovie.getOverallRating()+"/10");

    }
}
