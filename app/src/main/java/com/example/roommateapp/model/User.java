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

//Class for using and updating User data in Firebase
public class User {
    //private fields used to add info to database
    private long userID;
    private String name;
    private ArrayList<String> groupIdList;
    private static final String TAG = "User";
    private DocumentReference userRef;
    private Map<String, Object> userData;
    //private Location location;

    //Initializer method, sets id and name
    public void initializeUser (String name) {
        //Initialize User and get count of all users for userID
        userData  = new HashMap<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("users");
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Count fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    userID = snapshot.getCount();
                    Log.d(TAG, "Count: " + snapshot.getCount());
                } else {
                    Log.d(TAG, "Count failed: ", task.getException());
                }
            }
        });
        //Initialize User data and add User to db
        this.groupIdList = new ArrayList<String>();
        this.name = name;
        userData.put("groups", this.groupIdList);
        userData.put("name", this.name);

        db.collection("users").document(String.valueOf(this.userID)).set(this.userData).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        this.userRef = db.collection("users").document(String.valueOf(this.userID));

        //Location stuff once figured out
    }
    //Getter methods
    public long getUserID () {
        return this.userID;
    }

    public String getName () {
        return this.name;
    }

    //Method to edit the name of the user
    public void changeName(String name) {
        //Change User name and update in db
        this.name = name;
        this.userRef.update("name", this.name).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void addGroup(Group group) {
        //Add group and update in db
        this.groupIdList.add(String.valueOf(group.getID()));
        this.userRef.update("groups", this.groupIdList).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void removeGroup(Group group) {
        //Remove group and update in db
        this.groupIdList.remove(String.valueOf(group.getID()));
        this.userRef.update("groups", this.groupIdList).addOnSuccessListener(new OnSuccessListener<Void>() {
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


}
