package com.startup.threecat.sherlock.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell on 15-Jul-16.
 */
public class Person implements Serializable{

    public static final int MALE = 1;
    public static final int FEMALE = 2;
    private String id;
    private String name;
    private int age;
    private float height;
    private int gender;
    private String hairColor;
    private String address;
    private String comment;
    private ArrayList<Movement> movements;
    private String urlImage;

    public Person() {

    }
    public Person(String id, String name, int age,
                  float height, int gender) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.height = height;
        this.gender = gender;
    }

    public Person(String id, String name, int age, float height,
                  int gender, ArrayList<Movement> movements) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.height = height;
        this.gender = gender;
        this.movements = movements;
    }

    public Person(String id, String name, int age, float height, int gender, String hairColor,
                  String address, String comment, ArrayList<Movement> movements) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.height = height;
        this.gender = gender;
        this.hairColor = hairColor;
        this.address = address;
        this.comment = comment;
        this.movements = movements;
    }

    public void copy(Person fromPerson) {
        this.id = fromPerson.getId();
        this.name = fromPerson.getName();
        this.age = fromPerson.getAge();
        this.height = fromPerson.getHeight();
        this.hairColor = fromPerson.getHairColor();
        this.address = fromPerson.getAddress();
        this.comment = fromPerson.getComment();
        this.urlImage = fromPerson.getUrlImage();
        this.movements = fromPerson.getMovements();
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public float getHeight() {
        return height;
    }

    public int isGender() {
        return gender;
    }

    public String getHairColor() {
        return hairColor;
    }

    public String getAddress() {
        return address;
    }

    public String getComment() {
        return comment;
    }

    public ArrayList<Movement> getMovements() {
        return movements;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String url) {
        this.urlImage = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setMovements(ArrayList<Movement> movements) {
        this.movements = movements;
    }

}
