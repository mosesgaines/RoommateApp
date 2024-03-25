package com.example.roommateapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.roommateapp.R;
import com.example.roommateapp.UsersLVAdapter;
import com.example.roommateapp.databinding.UsersFragmentBinding;
import com.example.roommateapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsersFragment extends Fragment {

    private UsersFragmentBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private UsersLVAdapter mUserAdapter;
    private ArrayList<User> mUserList;
    ListView userLV ;

    private static final String TAG = "UsersFragment";
    private static String userID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
//        mUserList = new ArrayList<User>();
//        userLV = binding.usersList;
//        mUserAdapter = new UsersLVAdapter(getActivity(), mUserList);
//        userLV.setAdapter(mUserAdapter);



    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = UsersFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.createButton.setOnClickListener(e -> create());
        binding.deleteButton.setOnClickListener(e -> delete());
        binding.updateButton.setOnClickListener(e -> update());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Deletes test user in the database
    private void delete() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document("test")
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User successfully deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting user", e);
                    }
                });
    }

    //Updates the test user in the database
    private void update() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference testRef = db.collection("users").document("test");
        testRef.update("first", "Updated").addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot updated"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    //Creates a new test user in the database
    private void create() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Test");
        user.put("last", "User");
        user.put("born", 2002);

        // Add a new document with a generated ID
        db.collection("users").document("test")
                .set(user)
                .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: test"))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }
}