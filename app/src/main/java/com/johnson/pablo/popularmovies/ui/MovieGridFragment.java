package com.johnson.pablo.popularmovies.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.johnson.pablo.popularmovies.R;
import com.johnson.pablo.popularmovies.adapters.MoviesRecyclerAdapter;
import com.johnson.pablo.popularmovies.helpers.MovieApi;
import com.johnson.pablo.popularmovies.listeners.EndlessScrollListener;
import com.johnson.pablo.popularmovies.models.Movie;
import com.johnson.pablo.popularmovies.models.MovieResponse;
import com.johnson.pablo.popularmovies.models.Sort;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieGridFragment extends Fragment implements MoviesRecyclerAdapter.OnMovieClickListener {

    private Call<MovieResponse> callMovies;
    @Bind(R.id.moviesGrid)
    RecyclerView moviesRecyclerView;
    Sort defSort = Sort.fromString("popularity.desc");

    private int columnNumber;


    public MovieGridFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_movie_grid, container, false);
        columnNumber = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) ? 2 : 3;
        ButterKnife.bind(this, contentView);
        setUpRecyclerView();
        return contentView;
    }

    private void setUpRecyclerView() {
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), columnNumber);
        moviesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        moviesRecyclerView.setLayoutManager(mGridLayoutManager);
        final MoviesRecyclerAdapter moviesRecyclerAdapter = new MoviesRecyclerAdapter(MovieGridFragment.this, null);
        moviesRecyclerAdapter.setListener(this);
        moviesRecyclerView.addOnScrollListener(new EndlessScrollListener(mGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getMovies(moviesRecyclerAdapter, ++page, defSort, false);
            }
        });
        moviesRecyclerView.setAdapter(moviesRecyclerAdapter);

        getMovies(moviesRecyclerAdapter, 1, defSort, false);
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

    public void getMovies(Sort sort) {
        defSort = sort;
        getMovies((MoviesRecyclerAdapter) moviesRecyclerView.getAdapter(), 1, defSort, true);
    }

    private void getMovies(final MoviesRecyclerAdapter adapter, int page, Sort sort, boolean clearMovies) {
        if (clearMovies) {
            adapter.clear();
        }
        callMovies = MovieApi.get().getRetrofitService().getTopRated(page, sort);
        callMovies.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Response<MovieResponse> response) {
                adapter.add(response.body().getResults());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Pablo", t.getMessage());
            }
        });
    }

    @Override
    public void onMovieClicked(@NonNull Movie movie, View view, int position) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, DetailMovieFragment.newInstance(movie))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onFavoredClicked(@NonNull Movie movie, int position) {

    }
}
