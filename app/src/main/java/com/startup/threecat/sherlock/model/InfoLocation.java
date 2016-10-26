package com.startup.threecat.sherlock.model;

import java.io.Serializable;

/**
 * Created by Dell on 21-Jul-16.
 */
public class InfoLocation implements Serializable {

    private String name;
    private String vicinity;

    public InfoLocation(String name, String vicinity) {
        this.name = name;
        this.vicinity = vicinity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }
}
