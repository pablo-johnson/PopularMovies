package com.johnson.pablo.popularmovies.helpers;

import com.johnson.pablo.popularmovies.interfaces.MovieApiService;
import com.squareup.okhttp.OkHttpClient;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by pablo on 12/7/15.
 */
public class MovieApi {

    private static MovieApi instance;
    public static final String ENDPOINT_URL = "https://api.themoviedb.org/3/";
    private MovieApiService service;


    private MovieApi() {
    }

    public static MovieApi get() {
        if (instance == null) {
            instance = new MovieApi();
        }
        return instance;
    }

    public MovieApiService getRetrofitService() {
        if (service == null) {
            OkHttpClient client = new OkHttpClient();
            //Add and interceptor to log all the requests and responses
            client.interceptors().add(new LoggingInterceptor());
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ENDPOINT_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            service = retrofit.create(MovieApiService.class);
        }
        return service;
    }

}
