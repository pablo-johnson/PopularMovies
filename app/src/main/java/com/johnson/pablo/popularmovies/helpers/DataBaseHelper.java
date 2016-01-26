package com.johnson.pablo.popularmovies.helpers;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;

import com.johnson.pablo.popularmovies.models.Movie;
import com.johnson.pablo.popularmovies.models.data.MovieColumns;
import com.johnson.pablo.popularmovies.models.data.PopularMoviesProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    public List<Movie> getMoviesFromCursor(Cursor movieCursor) {
        List<Movie> movies = new ArrayList<>();
        while (movieCursor.moveToNext()) {
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
            movies.add(movie);
        }
        return movies;
    }

    public long inserMovieToFavorites(Context context, ContentValues movieContentValues) {
        Uri uri = context.getContentResolver().insert(PopularMoviesProvider.MOVIES.MOVIES_URI, movieContentValues);
        SharedPreferences sharedPreferences = context.getSharedPreferences(POPULAR_MOVIES_STORAGE, Context.MODE_PRIVATE);
        int movieId = movieContentValues.getAsInteger(MovieColumns._ID);
        String movieKey = String.format(MOVIE_KEY, movieId);
        sharedPreferences.edit().putInt(movieKey, movieId).apply();
        return ContentUris.parseId(uri);
    }

    public int deleteMovieFromFavorites(Context context, long movieId) {
        int number = context.getContentResolver().delete(PopularMoviesProvider.MOVIES.withId(movieId), null, null);
        SharedPreferences sharedPreferences = context.getSharedPreferences(POPULAR_MOVIES_STORAGE, Context.MODE_PRIVATE);
        String movieKey = String.format(MOVIE_KEY, movieId);
        sharedPreferences.edit().remove(movieKey).apply();
        return number;
    }

    public boolean isMovieSavedAsFavorite(Context context, long movieId) {
        String movieKey = String.format(MOVIE_KEY, movieId);
        SharedPreferences sharedPreferences = context.getSharedPreferences(POPULAR_MOVIES_STORAGE, Context.MODE_PRIVATE);
        if (sharedPreferences.getInt(movieKey, 0) > 0) {
            return true;
        }
        return false;
    }
}
