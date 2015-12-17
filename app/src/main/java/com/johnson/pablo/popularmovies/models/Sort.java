package com.johnson.pablo.popularmovies.models;

import java.io.Serializable;

/**
 * Created by Pablo on 16/12/15.
 */
public enum Sort implements Serializable {

    POPULARITY("popularity.desc", "Popularity"),
    VOTE_AVERAGE("vote_average.desc", "Vote Average"),
    VOTE_COUNT("vote_count.desc", "Vote Count");

    private final String value;
    private final String displayValue;

    Sort(String value, String displayValue) {
        this.value = value;
        this.displayValue = displayValue;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getDisplayValue(){
        return displayValue;
    }

    public static Sort fromString(String value) {
        if (value != null) {
            for (Sort sort : Sort.values()) {
                if (value.equalsIgnoreCase(sort.value)) {
                    return sort;
                }
            }
        }
        throw new IllegalArgumentException("No constant found with text " + value);
    }
}
