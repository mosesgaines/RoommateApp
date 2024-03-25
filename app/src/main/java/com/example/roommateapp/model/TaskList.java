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

public class TaskList {
    private long listID;
    private String name;
    private ArrayList<String> items;
    private Map<String, Object> listData;
    private static final String TAG = "TaskList";
    private DocumentReference taskRef;
    //Initializer method, sets Id as well as name and initializes List
    public void InitializeList(String name) {
        //Initialize database and get count of all lists for listID
        listData  = new HashMap<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("lists");
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Count fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    listID = snapshot.getCount();
                    Log.d(TAG, "Count: " + snapshot.getCount());
                } else {
                    Log.d(TAG, "Count failed: ", task.getException());
                }
            }
        });
        //Initialize data and add List to db
        this.name = name;
        this.items = new ArrayList<String>();
        listData.put("name", this.name);
        listData.put("items", this.items);
        db.collection("lists").document(String.valueOf(this.listID)).set(this.listData).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        this.taskRef = db.collection("lists").document(String.valueOf(this.listID));
    }
    //Methods for adding to and removing from List
    public void addItem (String item) {
        //Adds item to list and updates list in db
        this.items.add(item);
        this.taskRef.update("items", this.items).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void removeItem(String item) {
        //Remove item from list and update list in db
        items.remove(item);
        this.taskRef.update("items", this.items).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    //Method to edit name of the TaskList
    public void changeName(String name) {
        //Change name and update name in db
        this.name = name;
        this.taskRef.update("name", this.name).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    //Getter methods
    public String getName() {
        return this.name;
    }

    public long getID() {
        return this.listID;
    }


}
