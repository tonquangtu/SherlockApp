package com.startup.threecat.sherlock.databasehelper;

/**
 * Created by Dell on 15-Jul-16.
 */
public class SQLStatement {

    public static final String TABLE_PERSONS = "Persons";
    public static final String TABLE_MOVEMENTS = "Movements";
    public static final String ID_PERSONS = "id";
    public static final String NAME_PERSONS = "name";
    public static final String AGE_PERSONS = "age";
    public static final String HEIGHT_PERSONS = "height";
    public static final String GENDER_PERSONS = "gender";
    public static final String HAIR_COLOR_PERSONS = "hair_color";
    public static final String ADDRESS_PERSONS = "address";
    public static final String COMMENT_PERSONS = "comment";
    public static final String PATH_IMAGE_PERSONS = "path_image";
    public static final String LOCATION_MOVEMENTS= "location";
    public static final String MOVEMENT_NOTE_MOVEMENTS = "movement_note";
    public static final String TIME_MOVEMENTS = "time";
    public static final String ID_PERSON_MOVEMENTS = "id_person";
    public static final String ID_MOVEMENT_MOVEMENTS = "id_movement";

    public static final String CREATE_TABLE_PERSONS = "CREATE TABLE " +
            TABLE_PERSONS + "(id TEXT NOT NULL PRIMARY KEY," +
            "name TEXT NOT NULL," +
            "age INTEGER NOT NULL," +
            "height FLOAT NOT NULL," +
            "gender INTEGER," +
            "hair_color TEXT," +
            "address TEXT," +
            "comment TEXT," +
            "path_image TEXT);";

    public static final String CREATE_TABLE_MOVEMENTS = "CREATE TABLE " +
            TABLE_MOVEMENTS + "(id_movement TEXT NOT NULL PRIMARY KEY," +
            "id_person TEXT NOT NULL," +
            "location TEXT NOT NULL," +
            "movement_note TEXT NOT NULL," +
            "time TEXT NOT NULL);";

    public static String DELETE_TABLE_PERSONS = "DROP TABLE IF EXISTS " + TABLE_PERSONS;
    public static String DELETE_TABLE_MOVEMENTS = "DROP TABLE IF EXISTS " + TABLE_MOVEMENTS;

    public static String SQL_GET_ALL_PERSON = "SELECT * FROM " + TABLE_PERSONS + " LIMIT ?";
    public static String SQL_GET_MOVEMENTS_OF_PERSON = "SELECT id_movement, location, movement_note, time FROM Movements WHERE id_person = ?";
    public static final String SQL_UPDATE_PERSONS = "id = ?";
    public static final String DELETE_ROW_MOVEMENT = "id_movement = ?";
    public static final String DELETE_PERSON = "id = ?";
}
