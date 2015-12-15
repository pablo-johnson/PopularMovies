package com.johnson.pablo.popularmovies.helpers;

import android.os.Environment;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;

/**
 * Created by Pablo on 12/12/15.
 */
public class OkHttpSingletonClass {
    private static OkHttpClient okHttpClient;

    private OkHttpSingletonClass() {
    }

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
            createCacheForOkHTTP();
        }
        return okHttpClient;
    }

    private static void createCacheForOkHTTP() {
        Cache cache = null;
        cache = new Cache(getDirectory(), 1024 * 1024 * 10);
        okHttpClient.setCache(cache);
    }

    public static File getDirectory() {
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "PM" + File.separator);
        root.mkdirs();
        final String fname = "PM.ok";
        final File sdImageMainDirectory = new File(root, fname);
        return sdImageMainDirectory;
    }
}
