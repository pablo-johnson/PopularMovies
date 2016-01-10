package com.johnson.pablo.popularmovies.models.responses;

import com.johnson.pablo.popularmovies.models.Genre;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pablo on 10/01/16.
 */
public class GenreResponse {

    private List<Genre> genres = new ArrayList<>();

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}
