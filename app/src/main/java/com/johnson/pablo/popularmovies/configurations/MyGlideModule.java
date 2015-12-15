package com.johnson.pablo.popularmovies.configurations;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.bumptech.glide.module.GlideModule;
import com.johnson.pablo.popularmovies.BuildConfig;
import com.johnson.pablo.popularmovies.helpers.OkHttpSingletonClass;

import java.io.InputStream;

/**
 * Created by Pablo on 12/12/15.
 */
public class MyGlideModule implements GlideModule {

    /**
     * Lazily apply options to a {@link GlideBuilder} immediately before the Glide singleton is
     * created.
     * <p/>
     * <p>
     * This method will be called once and only once per implementation.
     * </p>
     *
     * @param context An Application {@link Context}.
     * @param builder The {@link GlideBuilder} that will be used to create Glide.
     */
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

    }

    /**
     * Lazily register components immediately after the Glide singleton is created but before any requests can be
     * started.
     * <p/>
     * <p>
     * This method will be called once and only once per implementation.
     * </p>
     *
     * @param context An Application {@link Context}.
     * @param glide   The newly created Glide singleton.
     */
    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(String.class, InputStream.class, new ImageLoader.Factory());
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(OkHttpSingletonClass.getOkHttpClient()));
    }

    private static class ImageLoader extends BaseGlideUrlLoader<String> {

        public ImageLoader(Context context) {
            super(context);
        }

        @Override
        protected String getUrl(String imagePath, int width, int height) {
            String url = buildPosterUrl(imagePath, width);
            return url;
        }

        public static class Factory implements ModelLoaderFactory<String, InputStream> {
            @Override
            public StreamModelLoader<String> build(Context context, GenericLoaderFactory factories) {
                return new ImageLoader(context);
            }

            @Override
            public void teardown() {
            }
        }

        public static String buildPosterUrl(@NonNull String imagePath, int width) {
            String widthPath;
            if (width <= 92) {
                widthPath = "w92";
            } else if (width <= 154) {
                widthPath = "w154";
            } else if (width <= 240) {
                widthPath = "w185";
            } else if (width <= 400) {
                widthPath = "w342";
            } else if (width <= 560) {
                widthPath = "w500";
            } else {
                widthPath = "w780";
            }
            return BuildConfig.IMAGE_URL + widthPath + imagePath;
        }
    }
}
