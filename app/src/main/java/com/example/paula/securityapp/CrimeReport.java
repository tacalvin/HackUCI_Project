package com.example.paula.securityapp;

import com.sun.jersey.api.client.GenericType;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created by Explo on 1/14/2017.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class CrimeReport {
    public static final GenericType<List<CrimeReport>> LIST_TYPE = new GenericType<List<CrimeReport>>() {};

    private String category;
    private String date;
    private String dayofweek;
    private double x;
    private double y;
    private String time;

    @JsonCreator
    public CrimeReport(@JsonProperty("category") String category,
                       @JsonProperty("date") String date,
                       @JsonProperty("dayofweek") String dayofweek,
                       @JsonProperty("x") double x,
                       @JsonProperty("y") double y,
                       @JsonProperty("time") String time)
    {
        this.category = category;
        this.date = date;
        this.dayofweek = dayofweek;
        this.x = x;
        this.y = y;
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getDayofweek() {
        return dayofweek;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getTime() {
        return time;
    }
}
