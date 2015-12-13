package com.johnson.pablo.popularmovies.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.florent37.glidepalette.GlidePalette;
import com.johnson.pablo.popularmovies.BuildConfig;
import com.johnson.pablo.popularmovies.R;
import com.johnson.pablo.popularmovies.models.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pablo on 12/8/15.
 */
public class MoviesRecyclerAdapter extends EndlessAdapter<Movie, MoviesRecyclerAdapter.MovieHolder> {

    @NonNull
    private final Fragment mFragment;
    private static float realWidth;

    public MoviesRecyclerAdapter(@NonNull Fragment fragment, @NonNull List<Movie> movies, int width) {
        super(fragment.getActivity(), movies == null ? new ArrayList<Movie>() : movies);
        mFragment = fragment;
        realWidth = width;
    }

    @Override
    protected MovieHolder onCreateItemHolder(ViewGroup parent, int viewType) {
        return new MovieHolder(mInflater.inflate(R.layout.movie_grid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        Glide.with(mFragment)
                .load(movie.getPosterPath())
                .crossFade()
                .listener(GlidePalette.with(movie.getPosterPath())
                        .use(GlidePalette.Profile.VIBRANT)
                        .intoBackground(((MovieHolder) holder).movieTitle)
                        .intoTextColor(((MovieHolder) holder).movieTitle))
                .placeholder(R.color.movie_poster_placeholder)
                .into(((MovieHolder) holder).movieImage);

        ((MovieHolder) holder).movieTitle.setText(movie.getTitle());
    }

    final class MovieHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.movieImage)
        ImageView movieImage;
        @Bind(R.id.movieTitle)
        TextView movieTitle;


        public MovieHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
