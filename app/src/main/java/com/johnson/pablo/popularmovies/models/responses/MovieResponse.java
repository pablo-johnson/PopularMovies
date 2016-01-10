package com.johnson.pablo.popularmovies.models.responses;

import com.johnson.pablo.popularmovies.models.DateFilter;
import com.johnson.pablo.popularmovies.models.Movie;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pablo on 12/7/15.
 */
public class MovieResponse implements Serializable {

    private int page;
    private List<Movie> results;
    private DateFilter dates;
    private int totalPages;
    private int totalResults;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    public DateFilter getDates() {
        return dates;
    }

    public void setDates(DateFilter dates) {
        this.dates = dates;
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
}
