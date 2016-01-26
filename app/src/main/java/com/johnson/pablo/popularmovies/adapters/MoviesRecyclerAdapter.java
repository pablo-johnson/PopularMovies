package com.johnson.pablo.popularmovies.adapters;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.florent37.glidepalette.BitmapPalette;
import com.github.florent37.glidepalette.GlidePalette;
import com.johnson.pablo.popularmovies.R;
import com.johnson.pablo.popularmovies.helpers.DataBaseHelper;
import com.johnson.pablo.popularmovies.models.Movie;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pablo on 12/8/15.
 */
public class MoviesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater mInflater;
    private final Map<Integer, String> mGenresMap;
    @NonNull
    protected List<Movie> mMovies;
    @NonNull
    private final Fragment mFragment;
    @NonNull
    private OnMovieClickListener mListener;

    public interface OnMovieClickListener {
        void onMovieClicked(@NonNull final Movie movie, View view, int position);

        void onFavoredClicked(@NonNull final Movie movie, int position, boolean isFavored);
    }

    public MoviesRecyclerAdapter(@NonNull Fragment fragment, List<Movie> movies, Map<Integer, String> genresMap) {
        mMovies = movies;
        mFragment = fragment;
        mInflater = LayoutInflater.from(fragment.getContext());
        mGenresMap = genresMap;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
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
                        .intoCallBack(new BitmapPalette.CallBack() {
                            @Override
                            public void onPaletteLoaded(Palette palette) {
                                Palette.Swatch swatchPalette = palette.getVibrantSwatch();
                                if (swatchPalette != null) {
                                    ((MovieHolder) holder).movieDataContainer
                                            .setBackgroundColor(swatchPalette.getRgb());
                                    ((MovieHolder) holder).movieTitle.setTextColor(swatchPalette.getTitleTextColor());
                                    ((MovieHolder) holder).movieGenres.setTextColor(swatchPalette.getTitleTextColor());
                                }
                            }
                        }))
                .placeholder(R.color.movie_poster_placeholder)
                .into(imageView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setTransitionName("transition_image_" + position);
            ((MovieHolder) holder).movieTitle.setTransitionName("transition_title_" + position);
        }

        if ((movie.getStrGenres() == null) && (movie.getGenres().length > 0)) {
            StringBuilder genres = new StringBuilder();
            for (int genreKey : movie.getGenres()) {
                genres.append(mGenresMap.get(genreKey));
                genres.append(", ");
            }
            movie.setStrGenres(genres.substring(0, genres.length() - 2));
        }
        if (movie.isFavorite() == null) {
            movie.setFavorite(DataBaseHelper.get()
                    .isMovieSavedAsFavorite(mFragment.getContext(), movie.getId()));
        }

        ((MovieHolder) holder).movieFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFavoredClicked(movie, position, movie.isFavorite());
            }
        });
        ((MovieHolder) holder).movieGenres.setText(movie.getStrGenres());
        ((MovieHolder) holder).movieTitle.setText(movie.getTitle());
        ((MovieHolder) holder).movieFavButton.setImageResource(movie.isFavorite() ?
                android.R.drawable.star_big_on :
                android.R.drawable.star_big_off);

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieHolder(mInflater.inflate(R.layout.item_movie_grid, parent, false));
    }

    public void clear() {
        if (!mMovies.isEmpty()) {
            mMovies.clear();
            notifyDataSetChanged();
        }
    }

    public void add(@NonNull List<Movie> newItems) {
        if (!newItems.isEmpty()) {
            int currentSize = mMovies.size();
            int amountInserted = newItems.size();

            mMovies.addAll(newItems);
            notifyItemRangeInserted(currentSize, amountInserted);
        }
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
        @Bind(R.id.movieDataContainer)
        View movieDataContainer;
        @Bind(R.id.movieFavButton)
        ImageView movieFavButton;
        @Bind(R.id.movieGenres)
        TextView movieGenres;


        public MovieHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
