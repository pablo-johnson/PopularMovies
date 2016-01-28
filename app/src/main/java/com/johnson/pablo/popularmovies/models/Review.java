package com.johnson.pablo.popularmovies.models;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.johnson.pablo.popularmovies.models.data.ReviewColumns;
import com.johnson.pablo.popularmovies.models.data.VideoColumns;

/**
 * Created by Pablo on 11/01/16.
 */
public class Review implements Parcelable {

    private String id;
    private String author;
    private String content;
    private String url;

    public Review() {
    }

    protected Review(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }

    public static ContentValues getContentValue(Review review, long movieId) {
        ContentValues reviewContentValues = new ContentValues();
        reviewContentValues.put(ReviewColumns.REVIEW_ID, review.getId());
        reviewContentValues.put(ReviewColumns.MOVIE_ID, movieId);
        reviewContentValues.put(ReviewColumns.AUTHOR, review.getAuthor());
        reviewContentValues.put(ReviewColumns.CONTENT, review.getContent());
        reviewContentValues.put(ReviewColumns.URL, review.getUrl());

        return reviewContentValues;
    }
}
