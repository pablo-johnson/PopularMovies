package com.johnson.pablo.popularmovies.helpers;

import android.database.Cursor;

import com.johnson.pablo.popularmovies.models.Movie;
import com.johnson.pablo.popularmovies.models.data.MovieColumns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pablo on 26/01/16.
 */
public class DataBaseHelper {

    private static DataBaseHelper instance;

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
}
