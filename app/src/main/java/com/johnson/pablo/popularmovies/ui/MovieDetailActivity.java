package com.johnson.pablo.popularmovies.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        if (toolbar != null) {
            ViewCompat.setElevation(toolbar, getResources().getDimension(R.dimen.toolbar_elevation));
            //mToolbar.setNavigationOnClickListener(new Navig);

            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
                ab.setDisplayHomeAsUpEnabled(true);
                ab.setDisplayShowHomeEnabled(true);
            }
        }

        Movie movie = getIntent().getParcelableExtra(MOVIE);

        MovieDetailFragment startFragment = MovieDetailFragment.newInstance(movie);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment, startFragment)
                .commit();
    }

    @Override
    public void onFragmentInteraction() {

    }

    @Override
    public boolean isTwoPanel() {
        return false;
    }
}
