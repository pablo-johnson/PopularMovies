package com.johnson.pablo.popularmovies.models.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by Pablo on 27/01/16.
 */
public interface VideoColumns {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    public static final String _ID = "_id";

    @DataType(DataType.Type.TEXT)
    public static final String VIDEO_ID = "videoId";

    @DataType(DataType.Type.INTEGER)
    public static final String MOVIE_ID = "movieId";

    @DataType(DataType.Type.TEXT)
    public static final String SITE = "site";

    @DataType(DataType.Type.TEXT)
    public static final String NAME = "name";

    @DataType(DataType.Type.TEXT)
    public static final String KEY = "key";

}
