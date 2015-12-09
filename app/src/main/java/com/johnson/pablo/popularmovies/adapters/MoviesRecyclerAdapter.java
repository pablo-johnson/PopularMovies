package com.johnson.pablo.popularmovies.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.johnson.pablo.popularmovies.BuildConfig;
import com.johnson.pablo.popularmovies.R;
import com.johnson.pablo.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pablo on 12/8/15.
 */
public class MoviesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final Fragment mFragment;
    @NonNull
    private final List<Movie> mMovies;
    private final LayoutInflater mInflater;

    public MoviesRecyclerAdapter(@NonNull Fragment fragment, @NonNull List<Movie> movies) {
        mFragment = fragment;
        mMovies = movies;
        mInflater = LayoutInflater.from(mFragment.getContext());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieHolder(mInflater.inflate(R.layout.movie_grid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e("Pablo", "" + buildPosterUrl(mMovies.get(position).getPosterPath(), ((MovieHolder) holder).movieImage.getWidth()));
        Picasso.with(mFragment.getContext())
                .load(buildPosterUrl(mMovies.get(position).getPosterPath(), ((MovieHolder) holder).movieImage.getWidth()))
                .into(((MovieHolder) holder).movieImage);
    }

    public static String buildPosterUrl(@NonNull String imagePath, int width) {
        String widthPath;

        if (width <= 92) {
            widthPath = "w92";
        } else if (width <= 154) {
            widthPath = "w154";
        } else if (width <= 185) {
            widthPath = "w185";
        } else if (width <= 342) {
            widthPath = "w342";
        } else if (width <= 500) {
            widthPath = "w500";
        } else {
            widthPath = "w780";
        }

        //Timber.v("buildPosterUrl: widthPath=" + widthPath);
        return BuildConfig.IMAGE_URL + widthPath + imagePath;
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    final class MovieHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.movieImage)
        ImageView movieImage;

        public MovieHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
