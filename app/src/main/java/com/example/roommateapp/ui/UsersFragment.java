package com.example.roommateapp.ui;

import static com.example.roommateapp.ui.MainActivity.getCurrGroup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.roommateapp.GroupsLVAdapter;
import com.example.roommateapp.R;
import com.example.roommateapp.UserLVAdapter;
import com.example.roommateapp.databinding.UsersFragmentBinding;
import com.example.roommateapp.model.Group;
import com.example.roommateapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class UsersFragment extends Fragment {

    private UsersFragmentBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ArrayList<User> mUserList;
    ListView userLV;

    private static final String TAG = "UsersFragment";
    private static String userID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mUserList = new ArrayList<>();
    }

    private void loadDatainListview() {

        // below line is use to get data from Firebase

        // firestore using collection in android.

        ArrayList<String> currGroupUsers = getCurrGroupUsers();
        for (String currGroupUserId : currGroupUsers) {
            DocumentReference userRef = db.collection("users").document(currGroupUserId);

            userRef.get().addOnCompleteListener(taskDocumentSnapshot -> {
                if (taskDocumentSnapshot.isSuccessful()) {
                    DocumentSnapshot user = taskDocumentSnapshot.getResult();
                    if (user.exists()) {
                        User currGroupUser = new User (Long.parseLong(user.getId()), user, userRef);
                        mUserList.add(currGroupUser);
                    }
                } else {
                    Log.d(TAG, "Error getting document: ", taskDocumentSnapshot.getException());
                }

                UserLVAdapter adapter = new UserLVAdapter(getActivity().getApplicationContext(), mUserList, this);
                // after passing this array list to our adapter
                // class we are setting our adapter to our list view.
                userLV.setAdapter(adapter);
            });
        }

       /* db.collection("users").get()
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
                            User user = d.toObject(User.class);
                            // after getting data from Firebase we are
                            // storing that data in our array list
                            mUserList.add(user);
                        }
                        // after that we are passing our array list to our adapter class.
                        UserLVAdapter adapter = new UserLVAdapter(getActivity().getApplicationContext(), mUserList);
                        // after passing this array list to our adapter
                        // class we are setting our adapter to our list view.
                        userLV.setAdapter(adapter);
                    } else {
                        // if the snapshot is empty we are displaying a toast message.
                        Toast.makeText(UsersFragment.this.getContext(), "No data found in Database", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {

                    // we are displaying a toast message

                    // when we get any error from Firebase.

                    Toast.makeText(UsersFragment.this.getContext(), "Fail to load data..", Toast.LENGTH_SHORT).show();
                }); */
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = UsersFragmentBinding.inflate(inflater, container, false);

        userLV = binding.usersList;
        loadDatainListview();

        binding.signoutButton.setOnClickListener(e -> {

            signOut();

            NavHostFragment.findNavController(UsersFragment.this)
                    .navigate(R.id.action_UserFragment_to_LoginFragment);
        });

        binding.groupsButton.setOnClickListener(e -> NavHostFragment.findNavController(UsersFragment.this).navigate(R.id.action_UsersFragment_to_GroupsFragment));

        /** Set edit text to whatever the name in the DB is **/
        binding.groupName.setText(getCurrGroupName());

        /** Make button save name change **/
        binding.saveButton.setOnClickListener(e -> {
            changeCurrGroupName(binding.groupName.getText().toString());
            binding.groupName.setText(getCurrGroupName());
        });

        binding.addButton.setOnClickListener(e -> {
            addNewUser(binding.newList.getText().toString());
            binding.newList.setText("");
        });


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
        mUserList = new ArrayList<User>();
        binding = null;
    }

    private void signOut() {
        mAuth.signOut();
        Toast.makeText(getContext(), "Successful Sign Out.",
                Toast.LENGTH_SHORT).show();
    }

    private ArrayList<String> getCurrGroupUsers() {
        Group currGroup = getCurrGroup();
        return currGroup.getUserIDList();
    }

    private String getCurrGroupName() {
        Group currGroup = getCurrGroup();
        return currGroup.getName();
    }

    private void changeCurrGroupName(String name) {
        Group currGroup = getCurrGroup();
        currGroup.changeName(name);
    }

    private void addNewUser(String email) {
        Group currGroup = getCurrGroup();
        CollectionReference userRef = db.collection("users");

        Query userQuery = userRef.whereEqualTo("email", email);
        userQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot user : task.getResult()) {
                        User newUser = new User(Long.parseLong(user.getId()), user, userRef.document(user.getId()));
                        currGroup.addUser(newUser);
                    }
                    refreshView();
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void refreshView() {
        mUserList = new ArrayList<>();
        loadDatainListview();
    }
}