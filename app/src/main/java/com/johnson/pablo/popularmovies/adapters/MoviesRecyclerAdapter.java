package com.johnson.pablo.popularmovies.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.picassopalette.BitmapPalette;
import com.github.florent37.picassopalette.PicassoPalette;
import com.johnson.pablo.popularmovies.BuildConfig;
import com.johnson.pablo.popularmovies.R;
import com.johnson.pablo.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

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
        realWidth = width / 2;
        setHasStableIds(true);
    }

    @Override
    protected MovieHolder onCreateItemHolder(ViewGroup parent, int viewType) {
        return new MovieHolder(mInflater.inflate(R.layout.movie_grid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e("Pablo", position + " " + buildPosterUrl(mMovies.get(position).getPosterPath()));
        Movie movie = mMovies.get(position);
        Log.e("Pablo", "id " + movie.getId());
        String url = buildPosterUrl(movie.getPosterPath());
        Picasso.with(mFragment.getContext())
                .load(url)
                .placeholder(R.color.movie_poster_placeholder)
                .into(((MovieHolder) holder).movieImage, PicassoPalette.with(url, ((MovieHolder) holder).movieImage)
                        .use(PicassoPalette.Profile.MUTED_DARK)
                        .intoBackground(((MovieHolder) holder).movieTitle)
                        .intoTextColor(((MovieHolder) holder).movieTitle));


        ((MovieHolder) holder).movieTitle.setText(movie.getTitle());
    }

    public static String buildPosterUrl(@NonNull String imagePath) {
        String widthPath;
        if (realWidth <= 92) {
            widthPath = "w92";
        } else if (realWidth <= 154) {
            widthPath = "w154";
        } else if (realWidth <= 240) {
            widthPath = "w185";
        } else if (realWidth <= 400) {
            widthPath = "w342";
        } else if (realWidth <= 560) {
            widthPath = "w500";
        } else {
            widthPath = "w780";
        }
        return BuildConfig.IMAGE_URL + widthPath + imagePath;
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
