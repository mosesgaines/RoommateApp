package com.example.roommateapp.model;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.roommateapp.R;
import com.example.roommateapp.databinding.LoginFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

//Class for using and updating Group data in Firebase
public class Group {
    private static final String TAG = "Group";
    private long groupID;
    private ArrayList<User> userList;
    private ArrayList<String> userIDList;
    private ArrayList<TaskList> taskList;
    private ArrayList<String> taskIDList;
    private String name;
    private Map<String, Object> groupData = new HashMap<>();
    private DocumentReference groupRef;
    //Initializer method, sets Id as well as name and initializes Lists
    public void initializeGroup (String groupName) {
        //Initialize database and get count of all groups for groupID
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("groups");
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Count fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    groupID = snapshot.getCount();
                    Log.d(TAG, "Count: " + snapshot.getCount());
                } else {
                    Log.d(TAG, "Count failed: ", task.getException());
                }
            }
        });

        //Initialize fields for db
        this.userList = new ArrayList<User>();
        this.taskList = new ArrayList<TaskList>();
        this.userIDList = new ArrayList<String>();
        this.taskIDList = new ArrayList<String>();
        this.name = groupName;
        groupData.put("groupID", this.groupID);
        groupData.put("lists", this.taskIDList);
        groupData.put("users", this.userIDList);
        groupData.put("name", this.name);
        //Add group to db
        db.collection("groups").document(String.valueOf(this.groupID)).set(this.groupData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
        this.groupRef = db.collection("groups").document(String.valueOf(this.groupID));
    }
    //Methods for adding to and removing from Lists
    public void addUser (User user) {
        //Add user to user list and update group in db
        this.userList.add(user);
        this.userIDList.add(String.valueOf(user.getUserID()));
        this.groupRef.update("users", userIDList).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
    public void addTaskList (TaskList taskList) {
        //Add task to task list and update group in db
        this.taskList.add(taskList);
        this.taskIDList.add(String.valueOf(taskList.getID()));
        this.groupRef.update("lists", taskIDList).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });;
    }
    public void removeUser (User user) {
        //Remove user from user list and update group in db
        this.userList.remove(user);
        this.userIDList.remove(String.valueOf(user.getUserID()));
        this.groupRef.update("users", userIDList).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });;
    }
    public void removeTaskList (TaskList taskList) {
        //remove tasklist from list and update group in db
        this.taskList.remove(taskList);
        this.taskIDList.remove(String.valueOf(taskList.getID()));
        this.groupRef.update("lists", taskIDList).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });;
    }
    //Getter methods
    public long getID() {
        return this.groupID;
    }
    public String getName() {
        return this.name;
    }
    //Method to edit the name of the group
    public void changeName(String name) {
        //Change name and update group in db
        this.name = name;
        this.groupRef.update("name", this.name).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });;
    }
}
