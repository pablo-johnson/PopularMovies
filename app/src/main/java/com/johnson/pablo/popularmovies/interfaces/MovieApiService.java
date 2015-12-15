package com.johnson.pablo.popularmovies.interfaces;

import com.johnson.pablo.popularmovies.BuildConfig;
import com.johnson.pablo.popularmovies.models.MovieResponse;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by pablo on 12/7/15.
 */
public interface MovieApiService {
    @GET("movie/top_rated?api_key=" + BuildConfig.MOVIE_DB_API_KEY+ "&page")
    Call<MovieResponse> getTopRated(@Query("page") int page);
}
