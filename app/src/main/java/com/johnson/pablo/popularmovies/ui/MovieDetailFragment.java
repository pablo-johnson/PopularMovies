package com.johnson.pablo.popularmovies.ui;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.johnson.pablo.popularmovies.PopularMoviesApplication;
import com.johnson.pablo.popularmovies.R;
import com.johnson.pablo.popularmovies.interfaces.OnFragmentInteractionListener;
import com.johnson.pablo.popularmovies.models.Movie;
import com.squareup.leakcanary.RefWatcher;

import butterknife.Bind;
import butterknife.ButterKnife;

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
    FloatingActionButton fabButton;

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
        if (getArguments() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Bundle bundle = getArguments();
                String imageTransitionName = bundle.getString("IMAGE_TRANSITION_NAME");
                movieImage.setTransitionName(imageTransitionName);
            }
            Movie movie = getArguments().getParcelable("movie");
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
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = PopularMoviesApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
