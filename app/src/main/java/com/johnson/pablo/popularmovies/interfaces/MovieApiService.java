package com.johnson.pablo.popularmovies.interfaces;

import com.johnson.pablo.popularmovies.BuildConfig;
import com.johnson.pablo.popularmovies.models.Sort;
import com.johnson.pablo.popularmovies.models.responses.GenreResponse;
import com.johnson.pablo.popularmovies.models.responses.MovieResponse;
import com.johnson.pablo.popularmovies.models.responses.ReviewResponse;
import com.johnson.pablo.popularmovies.models.responses.VideoResponse;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by pablo on 12/7/15.
 */
public interface MovieApiService {
    @GET("discover/movie?api_key=" + BuildConfig.MOVIE_DB_API_KEY)
    Call<MovieResponse> discoverMovies(@Query("page") int page, @Query("sort_by") Sort sort);

    @GET("genre/movie/list?api_key=" + BuildConfig.MOVIE_DB_API_KEY)
    Call<GenreResponse> getMovieGenres();

    @GET("movie/{movieId}/videos?api_key=" + BuildConfig.MOVIE_DB_API_KEY)
    Call<VideoResponse> getMovieVideos(@Path("movieId") long movieId);

    @GET("movie/{movieID}/reviews?api_key=" + BuildConfig.MOVIE_DB_API_KEY)
    Call<ReviewResponse> getMovieReviews(@Path("movieID") long movieID);

    @GET("movie/{id}/reviews?api_key=" + BuildConfig.MOVIE_DB_API_KEY)
    Call<ReviewResponse> getReviews(@Path("id") int id);
}
