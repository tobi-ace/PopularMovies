package com.tobi_ace.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tobi_ace.popularmovies.R;
import com.tobi_ace.popularmovies.models.Movie;

import java.util.ArrayList;

/**
 * Created by abdulgafar on 4/9/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private static final String TAG = MovieAdapter.class.getSimpleName()
            ;
    private ArrayList<Movie> movies;

    private MovieAdapterOnClickHandler mClickHandler;

    public MovieAdapter(ArrayList<Movie> movies, MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
        this.movies = movies;
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutForlistItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutForlistItem, parent , false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Context context = holder.ivMoviePoster.getContext();
        Movie movie = movies.get(position);
        Picasso.with(context).load(movie.getPosterPath()).into(holder.ivMoviePoster);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView ivMoviePoster;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ivMoviePoster = (ImageView) itemView.findViewById(R.id.iv_image_poster);
        }

        @Override
        public void onClick(View v) {
           Movie movieClicked = movies.get(getAdapterPosition());
            Log.i(TAG, "onBindViewHolder: Mo"+movieClicked.getOriginalTitle());
            mClickHandler.onClick(movieClicked);
        }
    }

}
