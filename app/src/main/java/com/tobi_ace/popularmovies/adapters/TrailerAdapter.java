package com.tobi_ace.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tobi_ace.popularmovies.R;
import com.tobi_ace.popularmovies.models.Trailer;

import java.util.ArrayList;

/**
 * Created by abdulgafar on 5/1/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {
    private static final String TAG = TrailerAdapter.class.getSimpleName();
    private ArrayList<Trailer> trailers;

    private TrailerAdapter.TrailerAdapterOnClickHandler mClickHandler;

    public TrailerAdapter(ArrayList<Trailer> trailers, TrailerAdapter.TrailerAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
        this.trailers = trailers;
    }

    public interface TrailerAdapterOnClickHandler {
        void onClick(Trailer trailer);
    }

    @Override
    public TrailerAdapter.TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutForlistItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutForlistItem, parent, false);
        return new TrailerAdapter.TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.TrailerAdapterViewHolder holder, int position) {
        Context context = holder.ivTrailerPoster.getContext();
        Trailer trailer = trailers.get(position);
        Picasso.with(context).load(trailer.getThumbnailUrl()).into(holder.ivTrailerPoster);
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public void setTrailers(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
    }

    class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivTrailerPoster;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ivTrailerPoster = (ImageView) itemView.findViewById(R.id.iv_trailer_poster);
        }

        @Override
        public void onClick(View v) {
            Trailer trailerClicked = trailers.get(getAdapterPosition());
            mClickHandler.onClick(trailerClicked);
        }
    }
}
