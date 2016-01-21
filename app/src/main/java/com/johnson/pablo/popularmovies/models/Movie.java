package com.johnson.pablo.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by pablo on 12/7/15.
 */
public class Movie implements Parcelable {

    private int id;
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("backdrop_path")
    private String backDropPath;
    private Double popularity;
    @SerializedName("original_title")
    private String title;
    @SerializedName("vote_average")
    private Double voteAverage;
    @SerializedName("vote_count")
    private long voteCount;
    private String originalTitle;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("video")
    private boolean hasVideo;
    @SerializedName("homepage")
    private String homepage;
    @SerializedName("genre_ids")
    int[] genres;
    private transient String strGenres;
    private List<Video> videos;
    private List<Review> reviews;

    public Movie() {
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackDropPath() {
        return backDropPath;
    }

    public void setBackDropPath(String backDropPath) {
        this.backDropPath = backDropPath;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public boolean isHasVideo() {
        return hasVideo;
    }

    public void setHasVideo(boolean hasVideo) {
        this.hasVideo = hasVideo;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(long voteCount) {
        this.voteCount = voteCount;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public int[] getGenres() {
        return genres;
    }

    public void setGenres(int[] genres) {
        this.genres = genres;
    }

    public String getStrGenres() {
        return strGenres;
    }

    public void setStrGenres(String strGenres) {
        this.strGenres = strGenres;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
        dest.writeString(this.posterPath);
        dest.writeString(this.backDropPath);
        dest.writeDouble(this.popularity);
        dest.writeString(this.title);
        dest.writeDouble(this.voteAverage);
        dest.writeLong(this.voteCount);
        dest.writeString(this.originalTitle);
        dest.writeString(this.originalLanguage);
        dest.writeByte(hasVideo ? (byte) 1 : (byte) 0);
        dest.writeString(this.homepage);
        dest.writeIntArray(genres);
        dest.writeString(strGenres);
        dest.writeList(videos);
        dest.writeList(reviews);
    }

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.posterPath = in.readString();
        this.backDropPath = in.readString();
        this.popularity = in.readDouble();
        this.title = in.readString();
        this.voteAverage = in.readDouble();
        this.voteCount = in.readLong();
        this.originalTitle = in.readString();
        this.originalLanguage = in.readString();
        this.hasVideo = in.readByte() != 0;
        this.homepage = in.readString();
        this.genres = in.createIntArray();
        this.strGenres = in.readString();
        this.videos = in.readArrayList(null);
        this.reviews = in.readArrayList(null);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
