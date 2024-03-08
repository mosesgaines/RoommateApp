package com.example.roommateapp.model;

import java.util.ArrayList;
import java.util.List;

//Class for using and updating Group data in Firebase
public class Group {
    private int groupID;
    private ArrayList<User> userList;
    private ArrayList<TaskList> taskList;
    private String name;
    //Initializer method, sets Id as well as name and initializes Lists
    public void initializeGroup (String groupName) {
        //some way to set id here
        this.userList = new ArrayList<User>();
        this.taskList = new ArrayList<TaskList>();
        this.name = groupName;
    }
    //Methods for adding to and removing from Lists
    public void addUser (User user) {
        this.userList.add(user);
    }
    public void addTaskList (TaskList taskList) {
        this.taskList.add(taskList);
    }
    public void removeUser (User user) {
        this.userList.remove(user);
    }
    public void removeTaskList (TaskList taskList) {
        this.taskList.remove(taskList);
    }
    //Getter methods
    public int getID() {
        return this.groupID;
    }
    public String getName() {
        return this.name;
    }
    //Method to edit the name of the group
    public void changeName(String name) {
        this.name = name;
    }
    //Will be used to save data to firebase
    //public void saveToFirebase()
}
