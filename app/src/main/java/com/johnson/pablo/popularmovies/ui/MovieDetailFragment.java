package com.johnson.pablo.popularmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.johnson.pablo.popularmovies.R;
import com.johnson.pablo.popularmovies.adapters.ExtrasAdapter;
import com.johnson.pablo.popularmovies.helpers.DataBaseHelper;
import com.johnson.pablo.popularmovies.helpers.MovieApi;
import com.johnson.pablo.popularmovies.interfaces.OnFragmentInteractionListener;
import com.johnson.pablo.popularmovies.models.Movie;
import com.johnson.pablo.popularmovies.models.Review;
import com.johnson.pablo.popularmovies.models.Video;
import com.johnson.pablo.popularmovies.models.responses.ReviewResponse;
import com.johnson.pablo.popularmovies.models.responses.VideosResponse;

import java.util.ArrayList;
import java.util.List;

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
    @Bind(R.id.reviewsList)
    RecyclerView reviewsListView;
    @Bind(R.id.trailersList)
    RecyclerView trailerListView;
    FloatingActionButton fabButton;
    private Call<VideosResponse> callVideos;
    private Call<ReviewResponse> callReviews;
    private int TRAILER_ITEM_HEIGHT = 90;
    private int REVIEW_ITEM_HEIGHT = 103;
    private boolean isFavorite;

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
        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        final Movie movie = getArguments().getParcelable("movie");
        setUpUI(movie);
        showTrailers(movie, isFavorite);
        showReviews(movie, isFavorite);
    }

    private void setUpUI(final Movie movie) {
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

        fabButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        isFavorite = DataBaseHelper.get().isMovieSavedAsFavorite(getActivity(), movie.getId());
        if (isFavorite) {
            fabButton.setImageResource(android.R.drawable.star_big_on);
        } else {
            fabButton.setImageResource(android.R.drawable.star_big_off);
        }
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DataBaseHelper.get().isMovieSavedAsFavorite(getActivity(), movie.getId())) {
                    if (DataBaseHelper.get().insertMovieToFavorites(getActivity(), movie) > 0) {
                        fabButton.setImageResource(android.R.drawable.star_big_on);
                        movie.setFavorite(true);
                    }
                } else {
                    if (DataBaseHelper.get().deleteMovieFromFavorites(getActivity(), movie.getId()) > 0) {
                        fabButton.setImageResource(android.R.drawable.star_big_off);
                        movie.setFavorite(false);
                    }
                }
            }
        });
    }

    private void showReviews(final Movie movie, boolean isFavorite) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        reviewsListView.setLayoutManager(linearLayoutManager);
        final ExtrasAdapter reviewsAdapter = new ExtrasAdapter(getActivity(), null, ExtrasAdapter.REVIEW_TYPE);
        reviewsListView.setAdapter(reviewsAdapter);
        reviewsAdapter.setExtraListener(extraClickListener);

        if (isFavorite) {
            if (movie.getReviews().isEmpty()) {
                movie.setReviews(DataBaseHelper.get().getReviews(getContext(), movie.getId()));
            }
            reviewsAdapter.addItems(movie.getReviews());

            ViewGroup.LayoutParams lp = reviewsListView.getLayoutParams();
            lp.height = (int) (REVIEW_ITEM_HEIGHT * Resources.getSystem().getDisplayMetrics().density * movie.getReviews().size());
            reviewsListView.setLayoutParams(lp);
            reviewsListView.setVisibility(View.VISIBLE);
            ButterKnife.findById(getView(), R.id.reviewsLabel).setVisibility(View.VISIBLE);
        } else {
            callReviews = MovieApi.get().getRetrofitService().getReviews(movie.getId());
            callReviews.enqueue(new Callback<ReviewResponse>() {
                @Override
                public void onResponse(Response<ReviewResponse> response) {
                    if ((response != null) && (response.body() != null) && (response.body().getResults() != null)
                            && !response.body().getResults().isEmpty()) {
                        movie.setReviews(response.body().getResults());
                        reviewsAdapter.addItems(movie.getReviews());

                        reviewsListView.setVisibility(View.VISIBLE);
                        ButterKnife.findById(getView(), R.id.reviewsLabel).setVisibility(View.VISIBLE);
                        ViewGroup.LayoutParams lp = reviewsListView.getLayoutParams();
                        lp.height = (int) (REVIEW_ITEM_HEIGHT * Resources.getSystem().getDisplayMetrics().density * movie.getReviews().size());
                        reviewsListView.setLayoutParams(lp);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e("Pablo", t.getLocalizedMessage());
                }
            });
        }
    }

    private void showTrailers(final Movie movie, boolean isFavorite) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        trailerListView.setLayoutManager(linearLayoutManager);
        final ExtrasAdapter trailersAdapter = new ExtrasAdapter(getActivity(), null, ExtrasAdapter.TRAILER_TYPE);
        trailerListView.setAdapter(trailersAdapter);
        trailersAdapter.setExtraListener(extraClickListener);

        if (isFavorite) {
            if (movie.getVideos().isEmpty()) {
                movie.setVideos(DataBaseHelper.get().getVideos(getContext(), movie.getId()));
            }
            trailersAdapter.addItems(movie.getVideos());

            trailerListView.setVisibility(View.VISIBLE);
            ButterKnife.findById(getView(), R.id.trailersLabel).setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams lp = trailerListView.getLayoutParams();
            lp.height = (int) (TRAILER_ITEM_HEIGHT * Resources.getSystem().getDisplayMetrics().density * movie.getVideos().size());
            trailerListView.setLayoutParams(lp);
        } else {
            callVideos = MovieApi.get().getRetrofitService().getMovieVideos(movie.getId());
            callVideos.enqueue(new Callback<VideosResponse>() {
                @Override
                public void onResponse(Response<VideosResponse> response) {
                    if ((response.body() != null) && (response.body().getResults() != null) && !response.body().getResults().isEmpty()) {
                        movie.setVideos(response.body().getResults());
                        trailersAdapter.addItems(movie.getVideos());

                        trailerListView.setVisibility(View.VISIBLE);
                        ButterKnife.findById(getView(), R.id.trailersLabel).setVisibility(View.VISIBLE);
                        ViewGroup.LayoutParams lp = trailerListView.getLayoutParams();
                        lp.height = (int) (TRAILER_ITEM_HEIGHT * Resources.getSystem().getDisplayMetrics().density * movie.getVideos().size());
                        trailerListView.setLayoutParams(lp);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e("Pablo", t.getLocalizedMessage());

                }
            });
        }
    }

    ExtrasAdapter.OnExtraClickListener extraClickListener = new ExtrasAdapter.OnExtraClickListener() {

        @Override
        public void onReviewClicked(@NonNull Review review, View view, int position) {

        }

        @Override
        public void onTrailerClicked(@NonNull Video video, View view, int position) {
            loadVideo(video);
        }
    };


    public void loadVideo(Video video) {
        String url = video.getVideoPath();
        if (url != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            intent.setData(Uri.parse(url));
            startActivity(Intent.createChooser(intent, getString(R.string.chooserIntentTitle)));
        }

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
    }
}
