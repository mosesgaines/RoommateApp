package com.example.roommateapp.model;

import java.util.ArrayList;

public class TaskList {
    private int listID;
    private String name;
    private ArrayList<String> items;
    //Initializer method, sets Id as well as name and initializes List
    public void InitializeList(String name) {
        //some way to set id
        this.name = name;
        items = new ArrayList<String>();
    }
    //Methods for adding to and removing from List
    public void addItem (String item) {
        items.add(item);
    }

    public void removeItem(String item) {
        items.remove(item);
    }
    //Method to edit name of the TaskList
    public void changeName(String name) {
        this.name = name;
    }
    //Getter methods
    public String getName() {
        return this.name;
    }

    public int getID() {
        return this.listID;
    }

    //Will be used to save data to firebase
    //public void saveToFirebase()


}
