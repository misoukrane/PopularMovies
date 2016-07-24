package com.mohamedisoukrane.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mohamed on 7/10/16.
 */
public class Movie implements Parcelable {

    protected int id;
    protected String title;
    protected String posterPath;
    protected String overview;
    protected String releaseDate;
    protected double userRating;

    public Movie(int id, String title, String posterPath, String overview, double userRating, String releaseDate) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.overview = overview;
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        this.title = in.readString();
        this.posterPath = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.userRating= in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeDouble(userRating);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
