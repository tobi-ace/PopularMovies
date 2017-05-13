package com.tobi_ace.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tobi_ace.popularmovies.R;
import com.tobi_ace.popularmovies.models.Review;

import java.util.ArrayList;

/**
 * Created by abdulgafar on 5/1/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {
    private static final String TAG = ReviewAdapter.class.getSimpleName();

    private ArrayList<Review> reviews;

    public ReviewAdapter(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    interface ReviewAdapterOnClickHandler {
        void onClick(Review review);
    }

    @Override
    public ReviewAdapter.ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutForlistItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutForlistItem, parent, false);
        return new ReviewAdapter.ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewAdapterViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.tvAuthor.setText(review.getAuthor());
        holder.tvContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    class ReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvAuthor;
        private TextView tvContent;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
        }

        @Override
        public void onClick(View v) {
            Review reviewClicked = reviews.get(getAdapterPosition());
        }
    }
}
