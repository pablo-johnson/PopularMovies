package com.johnson.pablo.popularmovies.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.johnson.pablo.popularmovies.R;
import com.johnson.pablo.popularmovies.helpers.MovieApi;
import com.johnson.pablo.popularmovies.models.MovieResponse;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private Call<MovieResponse> callMovies;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_main, container, false);
        getTopRatedMovies();
        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        if (callMovies != null) {
            callMovies.cancel();
        }
        super.onPause();
    }

    public void getTopRatedMovies() {
        callMovies = MovieApi.get().getRetrofitService().getTopRated();
        callMovies.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Response<MovieResponse> response) {
                Log.e("Pablo", "Size: " + response.body().getResults().size());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Pablo",t.getMessage());
            }
        });
    }
}
