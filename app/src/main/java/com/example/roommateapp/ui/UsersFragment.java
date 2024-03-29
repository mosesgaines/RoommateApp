package com.example.roommateapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.roommateapp.GroupsLVAdapter;
import com.example.roommateapp.databinding.UsersFragmentBinding;
import com.example.roommateapp.model.Group;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private ArrayList<Group> mUserList;
    ListView userLV;

    private static final String TAG = "UsersFragment";
    private static String userID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mUserList = new ArrayList<>();
//        mUserList.add("applesssssssssssssssssssssssssssssssssssssssssssssssssssssss");

//        mUserAdapter = new UsersLVAdapter(getActivity(), mUserList);
//        userLV.setAdapter(mUserAdapter);


    }

    private void loadDatainListview() {

        // below line is use to get data from Firebase

        // firestore using collection in android.

        db.collection("groups").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // after getting the data we are calling on success method
                    // and inside this method we are checking if the received
                    // query snapshot is empty or not.
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // if the snapshot is not empty we are hiding
                        // our progress bar and adding our data in a list.
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            // after getting this list we are passing
                            // that list to our object class.
                            Group group = d.toObject(Group.class);
                            // after getting data from Firebase we are
                            // storing that data in our array list
                            mUserList.add(group);
                        }
                        // after that we are passing our array list to our adapter class.
//                        GroupsLVAdapter adapter = new GroupsLVAdapter(getActivity().getApplicationContext(), mUserList, this);
                        // after passing this array list to our adapter
                        // class we are setting our adapter to our list view.
//                        userLV.setAdapter(adapter);
                    } else {
                        // if the snapshot is empty we are displaying a toast message.
                        Toast.makeText(UsersFragment.this.getContext(), "No data found in Database", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {

                    // we are displaying a toast message

                    // when we get any error from Firebase.

                    Toast.makeText(UsersFragment.this.getContext(), "Fail to load data..", Toast.LENGTH_SHORT).show();
                });
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = UsersFragmentBinding.inflate(inflater, container, false);

        userLV = binding.usersList;
        loadDatainListview();


//        users.
//        User test = new User(0);
//        mUserList.add(test);
//        getTestUser();
//        loadDatainListview();
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, mUserList);
//        CollectionReference users = db.collection("users").get();
//        userLV.setAdapter(adapter);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        binding.usersList.setOnClickListener(e -> NavHostFragment.findNavController(UsersFragment.this).navigate(R.id.action_UsersFragment_to_GroupsFragment));
//        binding.createButton.setOnClickListener(e -> create());
//        binding.deleteButton.setOnClickListener(e -> delete());
//        binding.updateButton.setOnClickListener(e -> update());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

//    private void getTestUser() {
////        binding.userText.setText(db.collection("users").document("test").get().getResult().get("first").toString());
////        db.collection("users").document("test").get().getResult().get("first");
////        db.collection("users").document("test").get().toString()
//        DocumentReference docRef = db.collection("users").document("0");
//        ArrayAdapter<User> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, mUserList);
////        String result = "";
//        docRef.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                DocumentSnapshot document = task.getResult();
//                if (document.exists()) {
//                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
////                    String text = document.get("first").toString() + " " +
////                            document.get("last").toString() + ": " +
////                            document.get("born").toString();
////                    binding.userText.setText(text);
////                    mUserList.add(document.get());
//                    userLV.setAdapter(adapter);
//
//                } else {
//                    Log.d(TAG, "No such document");
//                }
//            } else {
//                Log.d(TAG, "get failed with ", task.getException());
//            }
//        });
//    }

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