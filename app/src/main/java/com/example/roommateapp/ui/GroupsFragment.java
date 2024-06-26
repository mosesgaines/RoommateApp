package com.example.roommateapp.ui;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.roommateapp.ui.MainActivity.getCurrUser;
import static com.example.roommateapp.ui.MainActivity.setCurrUser;
import static com.google.firebase.firestore.FieldPath.documentId;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommateapp.GroupsRVAdapter;
import com.example.roommateapp.R;
import com.example.roommateapp.GroupsLVAdapter;
import com.example.roommateapp.databinding.GroupsFragmentBinding;
import com.example.roommateapp.model.Group;
import com.example.roommateapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GroupsFragment extends Fragment {

    private GroupsFragmentBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ArrayList<Group> mUserList;
    private final String TAG = "GroupsFragment";
    private RecyclerView groupRV;
    private RecyclerView.LayoutManager mLayoutManager;
    private ConnectivityManager connectivityManager;

    private ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            try {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
                    Toast.makeText(getContext(), "Please connect to a network in order for the app" +
                            " to work.", Toast.LENGTH_SHORT).show();
                }
            } catch (NullPointerException e) {

            }
        }
        @Override
        public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities);
            final boolean unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED);
        }
    };
    private NetworkRequest networkRequest = new NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mUserList = new ArrayList<>();
        connectivityManager =
                (ConnectivityManager) getSystemService(getContext(), ConnectivityManager.class);
        assert connectivityManager != null;
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
        try {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
                Toast.makeText(getContext(), "Please connect to a network in order for the app" +
                        " to work.", Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {}
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = GroupsFragmentBinding.inflate(inflater, container, false);
        groupRV = binding.groupsList;
        refreshView();
        return binding.getRoot();


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.signoutButton.setOnClickListener(e -> {

            signOut();

            NavHostFragment.findNavController(GroupsFragment.this)
                    .navigate(R.id.action_GroupsFragment_to_LoginFragment);
        });

        binding.addButton.setOnClickListener(e -> {

            addGroup(binding.newGroup.getText().toString());
            binding.newGroup.setText("");

        });
//
//        binding.listButton.setOnClickListener(e -> NavHostFragment.findNavController(GroupsFragment.this).navigate(R.id.action_GroupsFragment_to_ListFragment));
    }

    @Override
    public void onPause() {
        super.onPause();
        binding = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUserList = new ArrayList<Group>();
        binding = null;
        groupRV.setAdapter(null);
        connectivityManager.unregisterNetworkCallback(networkCallback);
//        refreshView();
    }

    private void signOut() {
        mAuth.signOut();
        Toast.makeText(getContext(), "Successful Sign Out.",
                Toast.LENGTH_SHORT).show();
    }

    private void loadDataInListview() {

        // below line is use to get data from Firebase

        // firestore using collection in android.
        ArrayList<String> currUserGroups = getCurrUserGroups();
        for (String currUserGroupId : currUserGroups) {
            DocumentReference groupRef = db.collection("groups").document(currUserGroupId);

            groupRef.get().addOnCompleteListener(taskDocumentSnapshot -> {
                    if (taskDocumentSnapshot.isSuccessful()) {
                        DocumentSnapshot group = taskDocumentSnapshot.getResult();
                        if (group.exists()) {
                            Group currUserGroup = new Group (Long.parseLong(group.getId()), group, groupRef);
                            mUserList.add(currUserGroup);
                        }
                    } else {
                        Log.d(TAG, "Error getting document: ", taskDocumentSnapshot.getException());
                    }

//                GroupsLVAdapter adapter = new GroupsLVAdapter(getActivity().getApplicationContext(), mUserList, this);
                GroupsRVAdapter adapter = new GroupsRVAdapter(mUserList, this);
                // after passing this array list to our adapter
                // class we are setting our adapter to our list view.
                groupRV.setAdapter(adapter);
                mLayoutManager = new LinearLayoutManager(getContext());
                groupRV.setLayoutManager(mLayoutManager);
            });
        }

        /* db.collection("groups").get()
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
                        GroupsLVAdapter adapter = new GroupsLVAdapter(getActivity().getApplicationContext(), mUserList, this);
                        // after passing this array list to our adapter
                        // class we are setting our adapter to our list view.
                        groupLV.setAdapter(adapter);
                    } else {
                        // if the snapshot is empty we are displaying a toast message.
                        Toast.makeText(GroupsFragment.this.getContext(), "No data found in Database", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {

                    // we are displaying a toast message

                    // when we get any error from Firebase.

                    Toast.makeText(GroupsFragment.this.getContext(), "Fail to load data..", Toast.LENGTH_SHORT).show();
                }); */
    }

    private void addGroup(String name) {
        //Gets current User, makes a new Group and adds it to the User
        User user = getCurrUser();
        Group newGroup = new Group(name);
        user.addGroup(newGroup);
        newGroup.addUser(user);
        refreshView();
    }

    private ArrayList<String> getCurrUserGroups() {
        User user = getCurrUser();
        return user.getGroups();
    }

    public void refreshView() {
        mUserList = new ArrayList<>();
        loadDataInListview();
    }






}