package com.johnson.pablo.popularmovies.models.responses;

import com.google.gson.annotations.SerializedName;
import com.johnson.pablo.popularmovies.models.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pablo on 11/01/16.
 */
public class ReviewResponse {

    private int id;
    private int page;
    private List<Review> results = new ArrayList<>();
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("total_results")
    private int totalResults;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }
}
