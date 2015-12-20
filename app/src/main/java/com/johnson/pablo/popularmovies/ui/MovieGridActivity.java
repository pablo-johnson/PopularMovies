package com.johnson.pablo.popularmovies.ui;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.johnson.pablo.popularmovies.R;
import com.johnson.pablo.popularmovies.adapters.SortSpinnerAdapter;
import com.johnson.pablo.popularmovies.interfaces.OnFragmentInteractionListener;
import com.johnson.pablo.popularmovies.models.Sort;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieGridActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    @Bind(R.id.sortSpinner)
    Spinner sortSpinner;
    @Nullable
    @Bind(R.id.movieDetailContainer)
    View movieDetailContainer;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_grid);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sortSpinner.setAdapter(new SortSpinnerAdapter(this, Sort.values()));
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MovieGridFragment movieGridFragment = (MovieGridFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
                movieGridFragment.getMovies((Sort) sortSpinner.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mTwoPane = movieDetailContainer != null;
    }

    @Override
    public void onFragmentInteraction() {

    }

    @Override
    public boolean isTwoPanel() {
        Log.e("Pablo", "mTwoPane " + mTwoPane);
        return mTwoPane;
    }
}
