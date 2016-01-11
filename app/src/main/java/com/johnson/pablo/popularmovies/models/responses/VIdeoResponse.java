package com.johnson.pablo.popularmovies.models.responses;

import com.johnson.pablo.popularmovies.models.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pablo on 11/01/16.
 */
public class VideoResponse {

    private int id;
    private List<Video> results = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Video> getResults() {
        return results;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }
}
