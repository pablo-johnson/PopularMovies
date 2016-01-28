package com.johnson.pablo.popularmovies.models.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by Pablo on 26/01/16.
 */
@Database(version = PopularMoviesDataBase.VERSION)
public final class PopularMoviesDataBase {

    public static final int VERSION = 2;

    @Table(MovieColumns.class)
    public static final String MOVIES = "movies";
    @Table(VideoColumns.class)
    public static final String VIDEOS = "videos";
    @Table(ReviewColumns.class)
    public static final String REVIEWS = "reviews";
}
