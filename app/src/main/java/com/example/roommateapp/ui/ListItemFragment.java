package com.example.roommateapp.ui;

import static com.example.roommateapp.ui.MainActivity.getCurrList;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.roommateapp.ListItemLVAdapter;
import com.example.roommateapp.ListItemRVAdapter;
import com.example.roommateapp.R;
import com.example.roommateapp.databinding.ListItemFragmentBinding;
import com.example.roommateapp.model.TaskList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListItemFragment} factory method to
 * create an instance of this fragment.
 */
public class ListItemFragment extends Fragment {

    private ListItemFragmentBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ArrayList<String> mTasks;
    private RecyclerView listRV;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mTasks = new ArrayList<>();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = ListItemFragmentBinding.inflate(inflater, container, false);
//        listLV = binding.taskList;
        listRV = binding.taskList;
        refreshView();
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.listsButton.setOnClickListener(e -> NavHostFragment.findNavController(ListItemFragment.this).navigate(R.id.action_ListItemFragment_to_ListFragment));
        binding.signoutButton.setOnClickListener(e -> {

            signOut();

            NavHostFragment.findNavController(ListItemFragment.this)
                    .navigate(R.id.action_ListItemFragment_to_LoginFragment);

        });

        binding.addButton.setOnClickListener(e -> {
            addItem(binding.newTask.getText().toString());
            binding.newTask.setText("");
        });

//
//        binding.usersButton.setOnClickListener(e -> NavHostFragment.findNavController(GroupsFragment.this).navigate(R.id.action_GroupsFragment_to_UsersFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        refreshView();
        binding = null;
        listRV.setAdapter(null);
    }

    private void signOut() {
        mAuth.signOut();
        Toast.makeText(getContext(), "Successful Sign Out.",
                Toast.LENGTH_SHORT).show();
    }

    private void loadDatainListview() {

        // below line is use to get data from Firebase

        // firestore using collection in android.

        /** hardcoded for now, this is where you could set the adapter to the specific list items
         *  for the list that was clicked on **/

        ArrayList<String> currListItems = getCurrListItems();
        for (String item : currListItems) {
            mTasks.add(item);
        }

//        ListItemLVAdapter adapter = new ListItemLVAdapter(getActivity().getApplicationContext(), mTasks, this);
        ListItemRVAdapter adapter1 = new ListItemRVAdapter(mTasks, this);
        // after passing this array list to our adapter
        // class we are setting our adapter to our list view.
//        listLV.setAdapter(adapter);
        listRV.setAdapter(adapter1);
        mLayoutManager = new LinearLayoutManager(getContext());
        listRV.setLayoutManager(mLayoutManager);
        /*
        db.collection("lists").whereEqualTo("name", "list").get()
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
                            mTasks.addAll(tList.getItems());
                        }
                        // after that we are passing our array list to our adapter class.
                        ListItemLVAdapter adapter = new ListItemLVAdapter(getActivity().getApplicationContext(), mTasks, this);
                        // after passing this array list to our adapter
                        // class we are setting our adapter to our list view.
                        listLV.setAdapter(adapter);
//                        listLV.setAdapter(testAdapter);
                    } else {
                        // if the snapshot is empty we are displaying a toast message.
                        Toast.makeText(ListItemFragment.this.getContext(), "No data found in Database", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {

                    // we are displaying a toast message

                    // when we get any error from Firebase.

                    Toast.makeText(ListItemFragment.this.getContext(), "Fail to load data..", Toast.LENGTH_SHORT).show();
                });

         */
    }

    private ArrayList<String> getCurrListItems() {
        TaskList currList = getCurrList();
        return currList.getItems();
    }

    public void refreshView() {
        mTasks = new ArrayList<>();
        loadDatainListview();
    }

    private void addItem(String item) {
        TaskList currList = getCurrList();
        currList.addItem(item);
        refreshView();
    }
}