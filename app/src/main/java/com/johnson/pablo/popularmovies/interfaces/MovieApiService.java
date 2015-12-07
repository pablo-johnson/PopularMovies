package com.johnson.pablo.popularmovies.interfaces;

import com.johnson.pablo.popularmovies.models.MovieResponse;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by pablo on 12/7/15.
 */
public interface MovieApiService {
    @GET("movie/top_rated?api_key=ae1ed8c56e48f6061aba267ce5c3665b")
    Call<MovieResponse> getTopRated();
}
