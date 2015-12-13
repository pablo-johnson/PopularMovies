package com.johnson.pablo.popularmovies.helpers;

import com.johnson.pablo.popularmovies.BuildConfig;
import com.johnson.pablo.popularmovies.interfaces.MovieApiService;
import com.squareup.okhttp.OkHttpClient;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by pablo on 12/7/15.
 */
public class MovieApi {

    private static MovieApi instance;
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
            OkHttpClient client = OkHttpSingleTonClass.getOkHttpClient();
            //Add and interceptor to log all the requests and responses
            //client.interceptors().add(new LoggingInterceptor());
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            service = retrofit.create(MovieApiService.class);
        }
        return service;
    }

}
