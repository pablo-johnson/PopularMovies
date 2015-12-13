package com.johnson.pablo.popularmovies.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.johnson.pablo.popularmovies.R;
import com.johnson.pablo.popularmovies.adapters.MoviesRecyclerAdapter;
import com.johnson.pablo.popularmovies.helpers.MovieApi;
import com.johnson.pablo.popularmovies.models.MovieResponse;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private Call<MovieResponse> callMovies;
    @Bind(R.id.moviesGrid)
    RecyclerView moviesRecyclerView;
    private int columnNumber;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_main, container, false);
        columnNumber = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 3;
        ButterKnife.bind(this, contentView);
        setUpRecyclerView();
        getTopRatedMovies();
        return contentView;
    }

    private void setUpRecyclerView() {
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(),
                columnNumber,
                GridLayoutManager.VERTICAL, false);
        moviesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        moviesRecyclerView.setLayoutManager(mGridLayoutManager);
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
                MoviesRecyclerAdapter moviesRecyclerAdapter = new MoviesRecyclerAdapter(MainActivityFragment.this,
                        response.body().getResults(), moviesRecyclerView.getWidth()/columnNumber);
                moviesRecyclerView.setAdapter(moviesRecyclerAdapter);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Pablo", t.getMessage());
            }
        });
    }
}
