package com.johnson.pablo.popularmovies.models.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by Pablo on 27/01/16.
 */
public interface ReviewColumns {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    public static final String _ID = "_id";

    @DataType(DataType.Type.TEXT)
    public static final String REVIEW_ID = "reviewId";

    @DataType(DataType.Type.INTEGER)
    public static final String MOVIE_ID = "movieId";

    @DataType(DataType.Type.TEXT)
    public static final String AUTHOR = "author";

    @DataType(DataType.Type.TEXT)
    public static final String CONTENT = "content";

    @DataType(DataType.Type.TEXT)
    public static final String URL = "url";

}
