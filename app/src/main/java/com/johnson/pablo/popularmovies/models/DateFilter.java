package com.johnson.pablo.popularmovies.models;

import java.io.Serializable;

/**
 * Created by pablo on 12/7/15.
 */
public class DateFilter implements Serializable {

    private String maximum;
    private String minimum;

    public String getMaximum() {
        return maximum;
    }

    public void setMaximum(String maximum) {
        this.maximum = maximum;
    }

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }
}
