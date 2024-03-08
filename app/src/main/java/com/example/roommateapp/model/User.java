package com.example.roommateapp.model;
//Class for using and updating User data in Firebase
public class User {
    //private fields used to add info to database
    private int userID;
    private String name;
    //private Location location;

    //Initializer method, sets id and name
    public void initializeUser (String name) {
        //some way to set id here
        this.name = name;
        //Location stuff once figured out
    }
    //Getter methods
    public int getUserID () {
        return this.userID;
    }

    public String getName () {
        return this.name;
    }

    //Method to edit the name of the user
    public void changeName(String name) {
        this.name = name;
    }

    //Will be used to save data to firebase
    //public void saveToFirebase()
}
