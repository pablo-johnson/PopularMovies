package com.johnson.pablo.popularmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.johnson.pablo.popularmovies.PopularMoviesApplication;
import com.johnson.pablo.popularmovies.R;
import com.johnson.pablo.popularmovies.adapters.MoviesRecyclerAdapter;
import com.johnson.pablo.popularmovies.helpers.DataBaseHelper;
import com.johnson.pablo.popularmovies.helpers.MovieApi;
import com.johnson.pablo.popularmovies.interfaces.OnFragmentInteractionListener;
import com.johnson.pablo.popularmovies.listeners.EndlessScrollListener;
import com.johnson.pablo.popularmovies.models.Genre;
import com.johnson.pablo.popularmovies.models.Movie;
import com.johnson.pablo.popularmovies.models.Sort;
import com.johnson.pablo.popularmovies.models.data.PopularMoviesProvider;
import com.johnson.pablo.popularmovies.models.responses.GenreResponse;
import com.johnson.pablo.popularmovies.models.responses.MovieResponse;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

import static android.support.v4.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieGridFragment extends Fragment implements MoviesRecyclerAdapter.OnMovieClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CURSOR_LOADER_ID = 0;
    private Call<MovieResponse> callMovies;
    private Call<GenreResponse> callGenres;
    @Bind(R.id.moviesGrid)
    RecyclerView moviesRecyclerView;
    Sort defSort = Sort.fromString("popularity.desc");

    private int columnNumber;
    private OnFragmentInteractionListener mListener;
    private int mPage = 1;
    private String STATE_CURRENT_PAGE = "stateCurrentPage";
    private String STATE_CURRENT_SORT = "stateCurrentSort";
    private HashMap<Integer, String> genresMap;
    private MoviesRecyclerAdapter moviesRecyclerAdapter;


    public MovieGridFragment() {
    }

    public static MovieGridFragment newInstance() {
        return new MovieGridFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        genresMap = new HashMap<>();
        callGenres = MovieApi.get().getRetrofitService().getMovieGenres();
        callGenres.enqueue(new Callback<GenreResponse>() {
            @Override
            public void onResponse(Response<GenreResponse> response) {
                for (Genre genre : response.body().getGenres()) {
                    genresMap.put(genre.getId(), genre.getName());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Pablo", t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        View contentView = inflater.inflate(R.layout.fragment_movie_grid, container, false);
        columnNumber = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) ? 2 : 3;
        ButterKnife.bind(this, contentView);
        setUpGridView();
        return contentView;
    }

    private void setUpGridView() {
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), columnNumber);
        moviesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        moviesRecyclerView.setLayoutManager(mGridLayoutManager);
        moviesRecyclerAdapter = new MoviesRecyclerAdapter(MovieGridFragment.this, new ArrayList<Movie>(), genresMap);
        moviesRecyclerAdapter.setListener(this);
        moviesRecyclerView.addOnScrollListener(new EndlessScrollListener(mGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!Sort.FAVORITES.name().equals(defSort.name())) {
                    getMovies(moviesRecyclerAdapter, ++page, defSort, false);
                    mPage = page;
                }
            }
        });
        moviesRecyclerView.setAdapter(moviesRecyclerAdapter);
    }

    @Override
    public void onPause() {
        if (callMovies != null) {
            callMovies.cancel();
        }
        if (callGenres != null) {
            callGenres.cancel();
        }
        super.onPause();
    }

    public void getMovies(Sort sort) {
        defSort = sort;
        getMovies((MoviesRecyclerAdapter) moviesRecyclerView.getAdapter(), 1, defSort, true);
    }

    private void getMovies(final MoviesRecyclerAdapter adapter, int page, Sort sort, boolean clearMovies) {
        if (!Sort.FAVORITES.name().equals(sort.name())) {
            if (clearMovies) {
                adapter.clear();
            }
            callMovies = MovieApi.get().getRetrofitService().discoverMovies(page, sort);
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
        } else {
            adapter.clear();
            getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
        }
    }

    @Override
    public void onMovieClicked(@NonNull Movie movie, View cardView, int position) {
        if (mListener.isTwoPanel()) {
            showDetailFragment(movie, cardView);
        } else {
            startDetailActivity(movie, cardView);
        }
    }

    private void showDetailFragment(@NonNull Movie movie, View cardView) {
        getActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);
        ImageView movieImage = (ImageView) cardView.findViewById(R.id.movieImage);
        MovieDetailFragment detailMovieFragment = MovieDetailFragment.newInstance(movie);
        Bundle bundle = detailMovieFragment.getArguments();
        FragmentTransaction ft = getFragmentManager().beginTransaction()
                .replace(R.id.movieDetailContainer, detailMovieFragment);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition changeTransform = TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_image_transform);

            // Setup exit transition on first fragment
            setSharedElementReturnTransition(changeTransform);

            // Setup enter transition on second fragment
            detailMovieFragment.setSharedElementEnterTransition(changeTransform);

            bundle.putString("IMAGE_TRANSITION_NAME", movieImage.getTransitionName());

            ft.addSharedElement(movieImage, movieImage.getTransitionName());
        }
        ft.commit();
    }

    private void startDetailActivity(@NonNull Movie movie, View cardView) {
        ImageView movieImage = (ImageView) cardView.findViewById(R.id.movieImage);
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.MOVIE, movie);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.putExtra("IMAGE_TRANSITION_NAME", movieImage.getTransitionName());
            Pair<View, String> p1 = Pair.create((View) movieImage, movieImage.getTransitionName());
            ActivityOptionsCompat options = makeSceneTransitionAnimation(getActivity(), p1);
            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onFavoredClicked(@NonNull Movie movie, int position, boolean isFavored) {
        DataBaseHelper.get().inserMovieToFavorites(getContext(), Movie.getMovieContentValue(movie));
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_CURRENT_SORT, defSort);
        outState.putInt(STATE_CURRENT_PAGE, mPage);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = PopularMoviesApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), PopularMoviesProvider.MOVIES.MOVIES_URI,
                null,
                null,
                null,
                null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (Sort.FAVORITES.name().equals(defSort.name())) {
            moviesRecyclerAdapter.clear();
            List<Movie> movies = DataBaseHelper.get().getMoviesFromCursor(data);
            moviesRecyclerAdapter.add(movies);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        moviesRecyclerAdapter.clear();
    }
}
