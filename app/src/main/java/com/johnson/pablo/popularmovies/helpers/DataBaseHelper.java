package com.johnson.pablo.popularmovies.helpers;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.johnson.pablo.popularmovies.models.Movie;
import com.johnson.pablo.popularmovies.models.Review;
import com.johnson.pablo.popularmovies.models.Video;
import com.johnson.pablo.popularmovies.models.data.MovieColumns;
import com.johnson.pablo.popularmovies.models.data.PopularMoviesProvider;
import com.johnson.pablo.popularmovies.models.data.ReviewColumns;
import com.johnson.pablo.popularmovies.models.data.VideoColumns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pablo on 26/01/16.
 */
public class DataBaseHelper {

    private static DataBaseHelper instance;
    private String MOVIE_KEY = "moviePreferenceKey_%d";
    private static final String POPULAR_MOVIES_STORAGE = "lpjaaa20060554fpf*p";

    private DataBaseHelper() {

    }

    public static DataBaseHelper get() {
        if (instance == null) {
            instance = new DataBaseHelper();
        }
        return instance;
    }

    public List<Movie> getMoviesFromCursor(Context context, Cursor movieCursor) {
        List<Movie> movies = new ArrayList<>();
        while (movieCursor.moveToNext()) {
            Movie movie = getMovieFromCursor(movieCursor);
            movies.add(movie);
            movie.setVideos(getVideos(context, movie.getId()));
            movie.setReviews(getReviews(context, movie.getId()));
        }
        return movies;
    }

    public List<Review> getReviews(Context context, int movieId) {
        Cursor reviewCursor = context.getContentResolver()
                .query(PopularMoviesProvider.REVIEWS.withMovieId(movieId), null, null, null, null);
        List<Review> reviews = new ArrayList<>();
        if (reviewCursor != null) {
            while (reviewCursor.moveToNext()) {
                Review review = new Review();
                review.setId(reviewCursor.getString(reviewCursor.getColumnIndex(ReviewColumns.REVIEW_ID)));
                review.setAuthor(reviewCursor.getString(reviewCursor.getColumnIndex(ReviewColumns.AUTHOR)));
                review.setContent(reviewCursor.getString(reviewCursor.getColumnIndex(ReviewColumns.CONTENT)));
                review.setUrl(reviewCursor.getString(reviewCursor.getColumnIndex(ReviewColumns.URL)));
                reviews.add(review);
            }
            reviewCursor.close();
        }
        return reviews;
    }

    @NonNull
    public List<Video> getVideos(Context context, int movieId) {
        Cursor videoCursor = context.getContentResolver()
                .query(PopularMoviesProvider.VIDEOS.withMovieId(movieId), null, null, null, null);
        List<Video> videos = new ArrayList<>();
        if (videoCursor != null) {
            while (videoCursor.moveToNext()) {
                Video video = new Video();
                video.setId(videoCursor.getString(videoCursor.getColumnIndex(VideoColumns.VIDEO_ID)));
                video.setKey(videoCursor.getString(videoCursor.getColumnIndex(VideoColumns.KEY)));
                video.setName(videoCursor.getString(videoCursor.getColumnIndex(VideoColumns.NAME)));
                video.setSite(videoCursor.getString(videoCursor.getColumnIndex(VideoColumns.SITE)));

                videos.add(video);
            }
            videoCursor.close();
        }
        return videos;
    }

    private Movie getMovieFromCursor(Cursor movieCursor) {
        Movie movie = new Movie();
        movie.setId(movieCursor.getInt(movieCursor.getColumnIndex(MovieColumns._ID)));
        movie.setOverview(movieCursor.getString(movieCursor.getColumnIndex(MovieColumns.OVERVIEW)));
        movie.setReleaseDate(movieCursor.getString(movieCursor.getColumnIndex(MovieColumns.RELEASE_DATE)));
        movie.setPosterPath(movieCursor.getString(movieCursor.getColumnIndex(MovieColumns.POSTER_PATH)));
        movie.setBackDropPath(movieCursor.getString(movieCursor.getColumnIndex(MovieColumns.BACK_DROP_PATH)));
        movie.setPopularity(movieCursor.getDouble(movieCursor.getColumnIndex(MovieColumns.POPULARITY)));
        movie.setTitle(movieCursor.getString(movieCursor.getColumnIndex(MovieColumns.TITLE)));
        movie.setVoteAverage(movieCursor.getDouble(movieCursor.getColumnIndex(MovieColumns.VOTE_AVERAGE)));
        movie.setVoteCount(movieCursor.getInt(movieCursor.getColumnIndex(MovieColumns.VOTE_COUNT)));
        movie.setOriginalTitle(movieCursor.getString(movieCursor.getColumnIndex(MovieColumns.ORIGINAL_TITLE)));
        movie.setHasVideo(movieCursor.getInt(movieCursor.getColumnIndex(MovieColumns.HAS_VIDEO)) > 0);
        movie.setStrGenres(movieCursor.getString(movieCursor.getColumnIndex(MovieColumns.STR_GENRES)));
        movie.setFavoriteAddedDate(movieCursor.getString(movieCursor.getColumnIndex(MovieColumns.ADDED_DATE)));
        return movie;
    }

    private long insertMovieToFavorites(Context context, ContentValues movieContentValues) {
        Uri uri = context.getContentResolver().insert(PopularMoviesProvider.MOVIES.MOVIES_URI, movieContentValues);
        SharedPreferences sharedPreferences = context.getSharedPreferences(POPULAR_MOVIES_STORAGE, Context.MODE_PRIVATE);
        int movieId = movieContentValues.getAsInteger(MovieColumns._ID);
        String movieKey = String.format(MOVIE_KEY, movieId);
        sharedPreferences.edit().putInt(movieKey, movieId).apply();
        return ContentUris.parseId(uri);
    }

    public long insertMovieToFavorites(Context context, Movie movie) {
        long movieId = insertMovieToFavorites(context, Movie.getContentValue(movie));

        if (!movie.getReviews().isEmpty()) {
            insertReviews(context, movie);
        }
        if (!movie.getVideos().isEmpty()) {
            insertVideos(context, movie);
        }
        return movieId;
    }

    private void insertReviews(Context context, Movie movie) {
        ContentValues[] contentValues = new ContentValues[movie.getReviews().size()];
        for (int i = 0; i < movie.getReviews().size(); i++) {
            contentValues[i] = Review.getContentValue(movie.getReviews().get(i), movie.getId());
        }
        context.getContentResolver().bulkInsert(PopularMoviesProvider.REVIEWS.REVIEWS_URI, contentValues);
    }

    private void insertVideos(Context context, Movie movie) {
        ContentValues[] contentValues = new ContentValues[movie.getVideos().size()];
        for (int i = 0; i < movie.getVideos().size(); i++) {
            contentValues[i] = Video.getContentValue(movie.getVideos().get(i), movie.getId());
        }
        context.getContentResolver().bulkInsert(PopularMoviesProvider.VIDEOS.VIDEOS_URI, contentValues);
    }

    public int deleteMovieFromFavorites(Context context, long movieId) {
        context.getContentResolver().delete(PopularMoviesProvider.VIDEOS.withMovieId(movieId), null, null);
        context.getContentResolver().delete(PopularMoviesProvider.REVIEWS.withMovieId(movieId), null, null);
        int number = context.getContentResolver().delete(PopularMoviesProvider.MOVIES.withId(movieId), null, null);
        if (number > 0) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(POPULAR_MOVIES_STORAGE, Context.MODE_PRIVATE);
            String movieKey = String.format(MOVIE_KEY, movieId);
            sharedPreferences.edit().remove(movieKey).apply();
        }
        return number;
    }

    public boolean isMovieSavedAsFavorite(Context context, long movieId) {
        String movieKey = String.format(MOVIE_KEY, movieId);
        SharedPreferences sharedPreferences = context.getSharedPreferences(POPULAR_MOVIES_STORAGE, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(movieKey, 0) > 0;
    }
}
