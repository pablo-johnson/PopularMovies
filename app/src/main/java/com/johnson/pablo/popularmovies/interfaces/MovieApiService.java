package com.johnson.pablo.popularmovies.interfaces;

import com.johnson.pablo.popularmovies.BuildConfig;
import com.johnson.pablo.popularmovies.models.MovieResponse;
import com.johnson.pablo.popularmovies.models.Sort;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by pablo on 12/7/15.
 */
public interface MovieApiService {
    @GET("discover/movie?api_key=" + BuildConfig.MOVIE_DB_API_KEY)
    Call<MovieResponse> getTopRated(@Query("page") int page, @Query("sort_by") Sort sort);
}
