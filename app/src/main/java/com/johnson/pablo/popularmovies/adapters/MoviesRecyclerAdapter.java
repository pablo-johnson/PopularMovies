package com.johnson.pablo.popularmovies.adapters;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.florent37.glidepalette.GlidePalette;
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

    public interface OnMovieClickListener {
        void onMovieClicked(@NonNull final Movie movie, View view, int position);

        void onFavoredClicked(@NonNull final Movie movie, int position);
    }

    @NonNull
    private final Fragment mFragment;
    @NonNull
    private OnMovieClickListener mListener;

    public MoviesRecyclerAdapter(@NonNull Fragment fragment, List<Movie> movies) {
        super(fragment.getActivity(), movies == null ? new ArrayList<Movie>() : movies);
        mFragment = fragment;
    }

    @Override
    protected MovieHolder onCreateItemHolder(ViewGroup parent, int viewType) {
        return new MovieHolder(mInflater.inflate(R.layout.item_movie_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Movie movie = mMovies.get(position);
        ((MovieHolder) holder).movieContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onMovieClicked(movie, view, position);
            }
        });
        ImageView imageView = ((MovieHolder) holder).movieImage;
        Glide.with(mFragment)
                .load(movie.getPosterPath())
                .crossFade()
                .listener(GlidePalette.with(movie.getPosterPath())
                        .use(GlidePalette.Profile.VIBRANT)
                        .intoBackground(((MovieHolder) holder).movieTitle)
                        .intoTextColor(((MovieHolder) holder).movieTitle))
                .placeholder(R.color.movie_poster_placeholder)
                .into(imageView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setTransitionName("transition_image_" + position);
            ((MovieHolder) holder).movieTitle.setTransitionName("transition_title_" + position);
        }

        ((MovieHolder) holder).movieTitle.setText(movie.getTitle());
    }

    public void setListener(@NonNull OnMovieClickListener listener) {
        this.mListener = listener;
    }

    final class MovieHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.movieImage)
        ImageView movieImage;
        @Bind(R.id.movieTitle)
        TextView movieTitle;
        @Bind(R.id.movieItemContainer)
        View movieContainer;


        public MovieHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
