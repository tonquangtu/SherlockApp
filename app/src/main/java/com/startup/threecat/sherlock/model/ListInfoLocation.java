package com.startup.threecat.sherlock.model;

import java.util.ArrayList;

/**
 * Created by Dell on 21-Jul-16.
 */
public class ListInfoLocation {

    public ListInfoLocation(ArrayList<InfoLocation> results) {
        this.results = results;
    }

    private ArrayList<InfoLocation> results;

    public ArrayList<InfoLocation> getResults() {
        return results;
    }

    public void setResults(ArrayList<InfoLocation> results) {
        this.results = results;
    }
}
