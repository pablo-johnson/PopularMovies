package com.johnson.pablo.popularmovies.ui;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.johnson.pablo.popularmovies.PopularMoviesApplication;
import com.johnson.pablo.popularmovies.R;
import com.johnson.pablo.popularmovies.adapters.MoviesRecyclerAdapter;
import com.johnson.pablo.popularmovies.adapters.ReviewsAdapter;
import com.johnson.pablo.popularmovies.helpers.MovieApi;
import com.johnson.pablo.popularmovies.interfaces.OnFragmentInteractionListener;
import com.johnson.pablo.popularmovies.listeners.EndlessScrollListener;
import com.johnson.pablo.popularmovies.managers.MyLinearLayoutManager;
import com.johnson.pablo.popularmovies.models.Movie;
import com.johnson.pablo.popularmovies.models.responses.ReviewResponse;
import com.johnson.pablo.popularmovies.models.responses.VideoResponse;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    @Bind(R.id.movieImage)
    ImageView movieImage;
    @Bind(R.id.movieReleaseDate)
    TextView movieReleaseDate;
    @Bind(R.id.movieSynopsis)
    TextView movieSynopsis;
    @Bind(R.id.movieVoteAverage)
    TextView movieVoteAverage;
    @Bind(R.id.movieGenres)
    TextView movieGenres;
    @Bind(R.id.reviewsGrid)
    RecyclerView reviewsList;
    FloatingActionButton fabButton;
    private Call<VideoResponse> callVideos;
    private Call<ReviewResponse> callReviews;

    public MovieDetailFragment() {
        //setRetainInstance(true);
    }

    public static MovieDetailFragment newInstance(Movie movie) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", movie);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, contentView);
        final Movie movie = getArguments().getParcelable("movie");
        setupCalls(movie);
        setUpUI(movie);
        setupReviewRecyclerView(movie);
        fabButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return contentView;
    }

    private void setupReviewRecyclerView(final Movie movie) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        reviewsList.setLayoutManager(linearLayoutManager);
        final ReviewsAdapter reviewsAdapter = new ReviewsAdapter(getActivity(), null);
        reviewsList.setAdapter(reviewsAdapter);

        callReviews = MovieApi.get().getRetrofitService().getMovieReviews(movie.getId());
        callReviews.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Response<ReviewResponse> response) {
                if (response != null && response.body() != null && response.body().getResults() != null) {
                    movie.setReviews(response.body().getResults());
                    reviewsAdapter.addReviews(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void setupCalls(final Movie movie) {
        callVideos = MovieApi.get().getRetrofitService().getMovieVideos(movie.getId());
        callVideos.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Response<VideoResponse> response) {
                if (response.body() != null && response.body().getResults() != null) {
                    movie.setVideos(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void setUpUI(Movie movie) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bundle bundle = getArguments();
            String imageTransitionName = bundle.getString("IMAGE_TRANSITION_NAME");
            movieImage.setTransitionName(imageTransitionName);
        }
        Glide.with(this)
                .load(movie.getPosterPath())
                .crossFade()
                .placeholder(R.color.movie_poster_placeholder)
                .into(movieImage);
        mListener.loadToolbarImage(movie.getBackDropPath());
        movieSynopsis.setText(movie.getOverview());
        movieVoteAverage.setText(movie.getVoteAverage().toString() + "/10");
        movieReleaseDate.setText(movie.getReleaseDate());
        movieGenres.setText(movie.getStrGenres());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                getActivity().supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        if (callVideos != null) {
            callVideos.cancel();
        }
        if (callReviews != null) {
            callReviews.cancel();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = PopularMoviesApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
