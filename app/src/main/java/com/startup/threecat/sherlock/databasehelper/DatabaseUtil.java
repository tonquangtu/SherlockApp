package com.startup.threecat.sherlock.databasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.startup.threecat.sherlock.model.Movement;
import com.startup.threecat.sherlock.model.Person;

import java.util.ArrayList;

/**
 * Created by Dell on 15-Jul-16.
 */
public class DatabaseUtil {

    public static final int INDEX_ID_PERSON = 0;
    public static final int INDEX_NAME_PERSON = 1;
    public static final int INDEX_AGE_PERSON = 2;
    public static final int INDEX_HEIGHT_PERSON = 3;
    public static final int INDEX_GENDER_PERSON = 4;
    public static final int INDEX_HAIR_COLOR_PERSON = 5;
    public static final int INDEX_ADDRESS_PERSON = 6;
    public static final int INDEX_COMMENT_PERSON = 7;
    public static final int INDEX_PATH_IMAGE_PERSON = 8;
    public static final int INDEX_LOCATION = 1;
    public static final int INDEX_MOVEMENT_NOTE = 2;
    public static final int INDEX_TIME_MOVEMENT = 3;


    public static ArrayList<Person> getAllPerson(Context context) {

       SQLiteDatabase dbReader = DatabaseHelper.getDbReader(context);
       String sqlGetAllPerson = SQLStatement.SQL_GET_ALL_PERSON;
       Cursor cursor = dbReader.rawQuery(sqlGetAllPerson, new String [] {"2"});
       ArrayList<Person> persons = new ArrayList<>();
       if(cursor != null) {
           Person tempPerson;
           cursor.moveToFirst();
           while(!cursor.isAfterLast()) {

               String id = cursor.getString(INDEX_ID_PERSON);
               String name = cursor.getString(INDEX_NAME_PERSON);
               int age = cursor.getInt(INDEX_AGE_PERSON);
               float height = cursor.getFloat(INDEX_HEIGHT_PERSON);
               int gender = cursor.getInt(INDEX_GENDER_PERSON);
               String hairColor = cursor.getString(INDEX_HAIR_COLOR_PERSON);
               String address = cursor.getString(INDEX_ADDRESS_PERSON);
               String comment = cursor.getString(INDEX_COMMENT_PERSON);
               String path = cursor.getString(INDEX_PATH_IMAGE_PERSON);
               ArrayList<Movement> movements = getMovements(context, id);

               tempPerson= new Person(id, name, age,height, gender,
                       hairColor, address, comment, movements);
               tempPerson.setUrlImage(path);
               persons.add(tempPerson);
                cursor.moveToNext();
           }
           cursor.close();
       }
        return persons;
   }

    public static ArrayList<Movement> getMovements(Context context, String idPerson) {

        SQLiteDatabase dbReader = DatabaseHelper.getDbReader(context);
        String [] args = {idPerson};
        Cursor cursor = dbReader.rawQuery(SQLStatement.SQL_GET_MOVEMENTS_OF_PERSON, args);
        ArrayList<Movement> movements = new ArrayList<>();
        Movement movement;
        if(cursor != null) {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                String id = cursor.getString(0);
                String location = cursor.getString(1);
                String movementNote = cursor.getString(2);
                String time = cursor.getString(3);
                movement = new Movement(id, location, movementNote, time);
                movements.add(movement);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return movements;
    }

    public static boolean addPerson(Context context, Person person) {

        SQLiteDatabase dbWriter = DatabaseHelper.getDbWriter(context);
        ContentValues values = new ContentValues();

        values.put(SQLStatement.ID_PERSONS, person.getId());
        values.put(SQLStatement.NAME_PERSONS, person.getName());
        values.put(SQLStatement.AGE_PERSONS, person.getAge());
        values.put(SQLStatement.HEIGHT_PERSONS, person.getHeight());
        values.put(SQLStatement.GENDER_PERSONS, person.isGender());
        values.put(SQLStatement.HAIR_COLOR_PERSONS, person.getHairColor());
        values.put(SQLStatement.ADDRESS_PERSONS, person.getAddress());
        values.put(SQLStatement.COMMENT_PERSONS, person.getComment());
        values.put(SQLStatement.PATH_IMAGE_PERSONS, person.getUrlImage());

        if(dbWriter.insert(SQLStatement.TABLE_PERSONS, null, values) != -1) {
            return true;
        }else {
            return false;
        }
    }

    public static boolean updateInfoPerson(Context context, Person person) {

        ContentValues upValue = new ContentValues();
        SQLiteDatabase database = DatabaseHelper.getDbWriter(context);

        upValue.put(SQLStatement.NAME_PERSONS, person.getName());
        upValue.put(SQLStatement.AGE_PERSONS, person.getAge());
        upValue.put(SQLStatement.HEIGHT_PERSONS, person.getHeight());
        upValue.put(SQLStatement.GENDER_PERSONS, person.isGender());
        upValue.put(SQLStatement.HAIR_COLOR_PERSONS, person.getHairColor());
        upValue.put(SQLStatement.ADDRESS_PERSONS, person.getAddress());
        upValue.put(SQLStatement.COMMENT_PERSONS, person.getComment());
        upValue.put(SQLStatement.PATH_IMAGE_PERSONS, person.getUrlImage());

        String [] args = new String [] {person.getId()};
        int numChange = database.update(SQLStatement.TABLE_PERSONS, upValue, SQLStatement.SQL_UPDATE_PERSONS, args);
        return (numChange == 1);
    }

    public static boolean addMovementForPerson(Context context, String idPerson, Movement movement) {

        SQLiteDatabase database = DatabaseHelper.getDbWriter(context);
        ContentValues values = new ContentValues();
        values.put(SQLStatement.ID_MOVEMENT_MOVEMENTS, movement.getId());
        values.put(SQLStatement.ID_PERSON_MOVEMENTS, idPerson);
        values.put(SQLStatement.LOCATION_MOVEMENTS, movement.getLocation());
        values.put(SQLStatement.MOVEMENT_NOTE_MOVEMENTS, movement.getMovementNote());
        values.put(SQLStatement.TIME_MOVEMENTS, movement.getTime());

        if(database.insert(SQLStatement.TABLE_MOVEMENTS, null, values) != 1) {
            return true;
        }else {
            return false;
        }
    }

    public static boolean deleteMovement(Context context, Movement movement) {

        SQLiteDatabase database = DatabaseHelper.getDbWriter(context);
        int numDelete = database.delete(SQLStatement.TABLE_MOVEMENTS,
                SQLStatement.DELETE_ROW_MOVEMENT, new String [] {movement.getId()});
        if(numDelete == 1) {
            return true;
        }else {
            return false;
        }
    }

    public static boolean deletePerson(Context context, Person person) {

        ArrayList<Movement> movements = person.getMovements();
        for(Movement movement : movements) {
            deleteMovement(context, movement);
        }

        SQLiteDatabase database = DatabaseHelper.getDbWriter(context);
        int numDel =  database.delete(SQLStatement.TABLE_PERSONS,
                SQLStatement.DELETE_PERSON, new String [] {person.getId()});
        return numDel == 1 ? true : false;
    }


}
