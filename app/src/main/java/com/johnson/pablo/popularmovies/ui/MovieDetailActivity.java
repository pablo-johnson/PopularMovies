package com.johnson.pablo.popularmovies.ui;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.johnson.pablo.popularmovies.R;
import com.johnson.pablo.popularmovies.interfaces.OnFragmentInteractionListener;
import com.johnson.pablo.popularmovies.models.Movie;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Pablo on 19/12/15.
 */
public class MovieDetailActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    public static final String MOVIE = "movie";
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.toolbarImage)
    ImageView toolbarImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        Movie movie = getIntent().getParcelableExtra(MOVIE);

        if (toolbar != null) {
            ViewCompat.setElevation(toolbar, getResources().getDimension(R.dimen.toolbar_elevation));
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    supportFinishAfterTransition();
                }
            });


            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
                ab.setDisplayHomeAsUpEnabled(true);
                ab.setDisplayShowHomeEnabled(true);
            }
        }

        if (collapsingToolbarLayout!=null){
            collapsingToolbarLayout.setTitle(movie.getTitle());
        }


        MovieDetailFragment movieDetailFragment = MovieDetailFragment.newInstance(movie);
        Bundle bundle = movieDetailFragment.getArguments();
        bundle.putAll(getIntent().getExtras());
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment, movieDetailFragment)
                .commit();
    }

    @Override
    public void loadToolbarImage(String path) {
        Glide.with(this)
                .load(path)
                .crossFade()
                .placeholder(R.color.movie_poster_placeholder)
                .into(toolbarImage);
    }

    @Override
    public boolean isTwoPanel() {
        return false;
    }
}
