package com.startup.threecat.sherlock.model;

import java.io.Serializable;

/**
 * This class contain info of movement of person
 */
public class Movement implements Serializable {

    private String id;
    private String location;
    private String movementNote;
    private String time;

    public Movement(String id, String location, String movementNote, String time) {
        this.id = id;
        this.location = location;
        this.movementNote = movementNote;
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public String getMovementNote() {
        return movementNote;
    }

    public String getTime() {
        return time;
    }

    public String getId() {
        return id;
    }
}
