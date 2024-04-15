package com.example.roommateapp;

import org.junit.Test;

import static org.junit.Assert.*;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.roommateapp.model.Group;
import com.example.roommateapp.model.User;
import com.example.roommateapp.ui.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GroupCreateTest {
    @Test
    public void GroupCreate() {
        Group testGroup = new Group("testGroup", 0, 0);
        MainActivity.setCurrGroup(testGroup);
        assertEquals(testGroup, MainActivity.getCurrGroup());


    }
}
