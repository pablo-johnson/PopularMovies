package com.johnson.pablo.popularmovies.models.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by Pablo on 26/01/16.
 */
@ContentProvider(authority = PopularMoviesProvider.AUTHORITY, database = PopularMoviesDataBase.class)
public class PopularMoviesProvider {

    public static final String AUTHORITY = "com.johnson.pablo.popularmovies.models.data.PopularMoviesProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String MOVIES = "movies";
        String REVIEWS = "reviews";
        String VIDEOS = "videos";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = PopularMoviesDataBase.MOVIES)
    public static class MOVIES {

        @ContentUri(
                path = Path.MOVIES,
                type = "vnd.android.cursor.dir/movie",
                defaultSort = MovieColumns.ADDED_DATE + " ASC")
        public static final Uri MOVIES_URI = buildUri(Path.MOVIES);

        @InexactContentUri(
                name = "MOVIE_ID",
                path = Path.MOVIES + "/#",
                type = "vnd.android.cursor.item/movie",
                whereColumn = MovieColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.MOVIES, String.valueOf(id));
        }
    }

    @TableEndpoint(table = PopularMoviesDataBase.REVIEWS)
    public static class REVIEWS {

        @ContentUri(
                path = Path.REVIEWS,
                type = "vnd.android.cursor.dir/review",
                defaultSort = ReviewColumns._ID + " ASC")
        public static final Uri REVIEWS_URI = buildUri(Path.REVIEWS);

        @InexactContentUri(
                name = "REVIEW_ID",
                path = Path.REVIEWS + "/#",
                type = "vnd.android.cursor.item/review",
                whereColumn = ReviewColumns.MOVIE_ID,
                pathSegment = 1)
        public static Uri withMovieId(long movieId) {
            return buildUri(Path.REVIEWS, String.valueOf(movieId));
        }
    }

    @TableEndpoint(table = PopularMoviesDataBase.VIDEOS)
    public static class VIDEOS {

        @ContentUri(
                path = Path.VIDEOS,
                type = "vnd.android.cursor.dir/video",
                defaultSort = VideoColumns._ID + " ASC")
        public static final Uri VIDEOS_URI = buildUri(Path.VIDEOS);

        @InexactContentUri(
                name = "VIDEO_ID",
                path = Path.VIDEOS + "/#",
                type = "vnd.android.cursor.item/video",
                whereColumn = VideoColumns.MOVIE_ID,
                pathSegment = 1)
        public static Uri withMovieId(long movieId) {
            return buildUri(Path.VIDEOS, String.valueOf(movieId));
        }
    }
}
