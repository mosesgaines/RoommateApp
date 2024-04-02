package com.example.roommateapp.model;

import static com.example.roommateapp.model.HelperMethods.getNextGroupId;
import static com.example.roommateapp.model.HelperMethods.incrementGroupId;
import static com.example.roommateapp.model.HelperMethods.parseStringToList;

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
import com.google.firebase.firestore.DocumentSnapshot;
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

    public Group() {}
    //Initializer method, sets Id as well as name and initializes Lists
    public Group (String groupName) {
        //Initialize database and get count of all groups for groupID
        this.name = groupName;
        this.userList = new ArrayList<User>();
        this.taskList = new ArrayList<TaskList>();
        this.userIDList = new ArrayList<String>();
        this.taskIDList = new ArrayList<String>();
        this.groupID = getNextGroupId();
        incrementGroupId();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("groups");
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Count fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    Log.d(TAG, "Count: " + snapshot.getCount());
                    //Initialize fields for db

                    groupData.put("lists", taskIDList);
                    groupData.put("users", userIDList);
                    groupData.put("name", name);
                    //Add group to db
                    db.collection("groups").document(String.valueOf(groupID)).set(groupData).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                    groupRef = db.collection("groups").document(String.valueOf(groupID));
                }
                else {
                    Log.d(TAG, "Count failed: ", task.getException());
                }
            }
        });
    }

    public Group (long groupId, DocumentSnapshot group, DocumentReference ref) {
        this.groupID = groupId;
        this.userList = new ArrayList<User>();
        this.taskList = new ArrayList<TaskList>();
        this.userIDList = new ArrayList<String>();
        this.taskIDList = new ArrayList<String>();
        this.groupRef = ref;

        //convert info into name, list of groups, email
        groupData = group.getData();
        assert groupData != null;
        name = (String) groupData.get("name");
        String userIdString = groupData.get("users").toString();
        Log.d(TAG, "userId data: " + userIdString);
        userIDList = parseStringToList(userIdString);
        String listIDString = groupData.get("lists").toString();
        Log.d(TAG, "listId data: " + listIDString);
        taskIDList = parseStringToList(listIDString);

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

    public ArrayList<String> getUserIDList() {
        return this.userIDList;
    }

    public ArrayList<String> getTaskIDList() {
        return this.taskIDList;
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
