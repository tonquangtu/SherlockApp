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
    public static final int INDEX_MOVEMENT = 2;


    public static ArrayList<Person> getAllPerson(Context context) {

       SQLiteDatabase dbReader = DatabaseHelper.getDbReader(context);
       String sqlGetAllPerson = SQLStatement.SQL_GET_ALL_PERSON;
       Cursor cursor = dbReader.rawQuery(sqlGetAllPerson, null);
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
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            String location = cursor.getString(0);
            String movementNote = cursor.getString(1);
            movement = new Movement(location, movementNote);
            movements.add(movement);
            cursor.moveToNext();
        }
        cursor.close();
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


}
