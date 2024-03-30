package com.example.roommateapp.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelperMethods {

    private static long nextUserId;

    private static long nextGroupId;

    private static long nextListId;

    private static DocumentReference userIdRef;

    private static DocumentReference groupIdRef;

    private static DocumentReference listIdRef;

    private static final String TAG = "HelperMethods";

    public static ArrayList<String> parseStringToList(String input) {
        ArrayList<String> list = new ArrayList<>();
        Pattern pattern = Pattern.compile("-?\\d+");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String number = matcher.group();
            list.add(number);
        }

        return list;
    }

    public static void incrementUserId() {
        nextUserId++;

        userIdRef.update("nextId", String.valueOf(nextUserId)).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public static void incrementGroupId() {
        nextGroupId++;

        groupIdRef.update("nextId", String.valueOf(nextGroupId)).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public static void incrementListId() {
        nextListId++;

        listIdRef.update("nextId", String.valueOf(nextListId)).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public static long getNextUserId() {
        return nextUserId;
    }

    public static long getNextGroupId() {
        return nextGroupId;
    }

    public static long getNextListId() {
        return nextListId;
    }

    public static void initializeIds() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userIdRef = db.collection("ids").document("user");
        userIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot id = task.getResult();
                    nextUserId = Long.parseLong((String) id.getData().get("nextId"));
                    if (id.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + id.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        groupIdRef = db.collection("ids").document("group");
        groupIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot id = task.getResult();
                    nextGroupId = Long.parseLong((String)id.getData().get("nextId"));
                    if (id.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + id.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        listIdRef = db.collection("ids").document("list");
        listIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot id = task.getResult();
                    nextListId = Long.parseLong((String)id.getData().get("nextId"));
                    if (id.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + id.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
}
