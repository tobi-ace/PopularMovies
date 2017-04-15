package com.tobi_ace.popularmovies;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.tobi_ace.popularmovies.utils.NetworkUtils;

/**
 * Created by abdulgafar on 4/9/17.
 */

 public class Movie implements Parcelable {

    private String originalTitle;
    private String posterPath;
    private String overview;
    private String overallRating;
    private String releaseDate;
    private Bitmap posterBitmap;

    public Bitmap getPosterBitmap() {
        return posterBitmap;
    }

    public void setPosterBitmap(Bitmap posterBitmap) {
        this.posterBitmap = posterBitmap;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getOverallRating() {
        return overallRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Movie(String originalTitle, String relativePosterPath, String overview, String overallRating, String releaseDate) {
        this.originalTitle =  originalTitle;
        this.posterPath = NetworkUtils.buildAbsolutePosterPath(relativePosterPath);
        this.overview = overview;
        this.overallRating = overallRating;
        this.releaseDate  = releaseDate;
    }

    public Movie(Parcel parcel){
        originalTitle = parcel.readString();
        posterPath = parcel.readString();
        overview = parcel.readString();
        overallRating = parcel.readString();
        releaseDate = parcel.readString();
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(originalTitle);
        parcel.writeString(posterPath);
        parcel.writeString(overview);
        parcel.writeString(overallRating);
        parcel.writeString(releaseDate);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[0];
        }
    };
}
