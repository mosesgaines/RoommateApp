package com.example.roommateapp.model;

import static com.example.roommateapp.model.HelperMethods.getNextUserId;
import static com.example.roommateapp.model.HelperMethods.incrementUserId;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

//Class for using and updating User data in Firebase
public class User {
    //private fields used to add info to database
    private long userID;
    private String name;
    private String email;
    private ArrayList<String> groupIdList;
    private static final String TAG = "User";
    private DocumentReference userRef;
    private Map<String, Object> userData;
    //private Location location;

    //empty constructor for firebase
    public User() {}

    //Initializer method, sets id and name
    public User (String name, String email) {
        //Initialize User and get count of all users for userID
        userData  = new HashMap<>();
        this.name = name;
        this.email = email;
        this.userID = getNextUserId();
        incrementUserId();
        this.groupIdList = new ArrayList<String>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userRef = db.collection("users").document(String.valueOf(userID));
        Query query = db.collection("users");
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Count fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    Log.d(TAG, "Count: " + snapshot.getCount());
                    userData.put("groups", groupIdList);
                    userData.put("name", name);
                    userData.put("email", email);

                    db.collection("users").document(String.valueOf(userID)).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
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

                } else {
                    Log.d(TAG, "Count failed: ", task.getException());
                }
            }
        });

        //Location stuff once figured out
    }

    public User (long userID, DocumentSnapshot user, DocumentReference ref) {
        this.userID = userID;
        this.groupIdList = new ArrayList<String>();
        this.userRef = ref;

        //convert info into name, list of groups, email
        userData = user.getData();
        assert userData != null;
        name = (String) userData.get("name");
        email = (String) userData.get("email");
        String groupIdString = userData.get("groups").toString();
        Log.d(TAG, "groupId data: " + groupIdString);
        groupIdList = parseStringToList(groupIdString);


        //Convert info into name, list of groups, email

    }
    //Getter methods
    public long getUserID () {
        return this.userID;
    }

    public String getName () {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public ArrayList<String> getGroups() { return this.groupIdList; }

    //Method to edit the email of the user
    public void changeEmail(String email) {
        //Change email and update in db
        this.email = email;

        this.userRef.update("email", this.email).addOnSuccessListener(new OnSuccessListener<Void>() {
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
