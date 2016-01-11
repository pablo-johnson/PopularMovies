package com.johnson.pablo.popularmovies;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by Pablo on 10/01/16.
 */
public class PopularMoviesApplication extends Application {

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher(Context context) {
        PopularMoviesApplication application = (PopularMoviesApplication) context.getApplicationContext();
        return application.refWatcher;
    }
}
