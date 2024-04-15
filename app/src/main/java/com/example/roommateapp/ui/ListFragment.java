package com.example.roommateapp.ui;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.roommateapp.ui.MainActivity.getCurrGroup;
import static com.example.roommateapp.ui.MainActivity.getCurrList;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommateapp.GroupsLVAdapter;
import com.example.roommateapp.ListsLVAdapter;
import com.example.roommateapp.ListsRVAdapter;
import com.example.roommateapp.R;
import com.example.roommateapp.UserLVAdapter;
import com.example.roommateapp.databinding.ListFragmentBinding;
import com.example.roommateapp.model.Group;
import com.example.roommateapp.model.TaskList;
import com.example.roommateapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private ListFragmentBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ArrayList<TaskList> mTaskList;
    private RecyclerView listRV;
    private RecyclerView.LayoutManager mLayoutManager;
    private final String TAG = "ListFragment";
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
        mTaskList = new ArrayList<>();
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

        binding = ListFragmentBinding.inflate(inflater, container, false);
        listRV = binding.listsList;
        mLayoutManager = new LinearLayoutManager(getContext());
        listRV.setLayoutManager(mLayoutManager);
        refreshView();
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.groupsButton.setOnClickListener(e -> NavHostFragment.findNavController(ListFragment.this).navigate(R.id.action_ListFragment_to_GroupsFragment));
        binding.signoutButton.setOnClickListener(e -> {

            signOut();

            NavHostFragment.findNavController(ListFragment.this)
                    .navigate(R.id.action_ListFragment_to_LoginFragment);
        });

        binding.addButton.setOnClickListener(e -> {
            addList(binding.newList.getText().toString());
            binding.newList.setText("");
            refreshView();
        });

        binding.gpsButton.setOnClickListener(e -> NavHostFragment.findNavController(ListFragment.this).navigate(R.id.action_ListFragment_to_MapsFragment));
//
//        binding.usersButton.setOnClickListener(e -> NavHostFragment.findNavController(GroupsFragment.this).navigate(R.id.action_GroupsFragment_to_UsersFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        listRV.setAdapter(null);
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }

    private void signOut() {
        mAuth.signOut();
        Toast.makeText(getContext(), "Successful Sign Out.",
                Toast.LENGTH_SHORT).show();
    }

    private void loadDatainListview() {

        // below line is use to get data from Firebase

        // firestore using collection in android.

        ArrayList<String> currGroupLists = getCurrGroupLists();
        for (String currGroupListId : currGroupLists) {
            DocumentReference listRef = db.collection("lists").document(currGroupListId);

            listRef.get().addOnCompleteListener(taskDocumentSnapshot -> {
                if (taskDocumentSnapshot.isSuccessful()) {
                    DocumentSnapshot list = taskDocumentSnapshot.getResult();
                    if (list.exists()) {
                        TaskList currGroupList = new TaskList (Long.parseLong(list.getId()), list, listRef);
                        mTaskList.add(currGroupList);
                    }
                } else {
                    Log.d(TAG, "Error getting document: ", taskDocumentSnapshot.getException());
                }

//                ListsLVAdapter adapter = new ListsLVAdapter(getActivity().getApplicationContext(), mTaskList, this);
                ListsRVAdapter adapter = new ListsRVAdapter(mTaskList, this);
                // after passing this array list to our adapter
                // class we are setting our adapter to our list view.
//                listLV.setAdapter(adapter);
                listRV.setAdapter(adapter);
            });
        }
        /*
        // working on this so that I can filter out lists for specific group clicked
        // can delete if we get it working another way
        db.collection("groups").get().addOnSuccessListener(snapshots -> {
            if (!snapshots.isEmpty()) {
                // if the snapshot is not empty we are hiding
                // our progress bar and adding our data in a list.
                List<DocumentSnapshot> groups = snapshots.getDocuments();
                for (DocumentSnapshot d : groups) {
                    Group group = d.toObject(Group.class);
                    ArrayList<String> lists;
                }
            }
        });

        db.collection("lists").get()
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
                            TaskList tList = d.toObject(TaskList.class);
                            // after getting data from Firebase we are
                            // storing that data in our array list
                            mTaskList.add(tList);

//                            for (String task : tList.getItems()) {
//                                mTaskList.add(task);
//                            }
                        }
                        // after that we are passing our array list to our adapter class.
                        ListsLVAdapter adapter = new ListsLVAdapter(getActivity().getApplicationContext(), mTaskList, this);
                        // after passing this array list to our adapter
                        // class we are setting our adapter to our list view.
                        listLV.setAdapter(adapter);
//                        listLV.setAdapter(testAdapter);
                    } else {
                        // if the snapshot is empty we are displaying a toast message.
                        Toast.makeText(ListFragment.this.getContext(), "No data found in Database", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {

                    // we are displaying a toast message

                    // when we get any error from Firebase.

                    Toast.makeText(ListFragment.this.getContext(), "Fail to load data..", Toast.LENGTH_SHORT).show();
                });
                */

    }

    private ArrayList<String> getCurrGroupLists() {
        Group currGroup = getCurrGroup();
        return currGroup.getTaskIDList();
    }

    private void addList(String name) {
        Group currGroup = getCurrGroup();
        TaskList list = new TaskList(name);
        currGroup.addTaskList(list);
        refreshView();
    }

    public void refreshView() {
        mTaskList = new ArrayList<>();
        loadDatainListview();
    }
}
