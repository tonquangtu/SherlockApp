package com.startup.threecat.sherlock.model;

import java.io.Serializable;

/**
 * This class contain info of movement of person
 */
public class Movement implements Serializable {

    private String location;
    private String movementNote;

    public Movement(String location, String movementNote) {
        this.location = location;
        this.movementNote = movementNote;
    }

    public String getLocation() {
        return location;
    }

    public String getMovementNote() {
        return movementNote;
    }
}
