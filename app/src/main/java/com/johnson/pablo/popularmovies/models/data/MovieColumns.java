package com.johnson.pablo.popularmovies.models.data;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by Pablo on 26/01/16.
 */
public interface MovieColumns {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    public static final String _ID = "_id";

    @DataType(DataType.Type.TEXT)
    public static final String OVERVIEW = "overview";
    @DataType(DataType.Type.TEXT)
    public static final String RELEASE_DATE = "releaseDate";
    @DataType(DataType.Type.TEXT)
    public static final String POSTER_PATH = "posterPath";
    @DataType(DataType.Type.TEXT)
    public static final String BACK_DROP_PATH = "backDropPath";
    @DataType(DataType.Type.REAL)
    public static final String POPULARITY = "popularity";
    @DataType(DataType.Type.TEXT)
    public static final String TITLE = "title";
    @DataType(DataType.Type.REAL)
    public static final String VOTE_AVERAGE = "voteAverage";
    @DataType(DataType.Type.INTEGER)
    public static final String VOTE_COUNT = "voteCount";
    @DataType(DataType.Type.TEXT)
    public static final String ORIGINAL_TITLE = "originalTitle";
    @DataType(DataType.Type.INTEGER)
    public static final String HAS_VIDEO = "hasVideo";
    @DataType(DataType.Type.TEXT)
    public static final String STR_GENRES = "strGenres";
    @DataType(DataType.Type.TEXT)
    public static final String ADDED_DATE = "addedDate";
    @DataType(DataType.Type.INTEGER)
    public static final String IS_FAVORITE = "isFavorite";

}
