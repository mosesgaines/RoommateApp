package com.example.roommateapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.roommateapp.R;
import com.example.roommateapp.UserLVAdapter;
import com.example.roommateapp.databinding.EditTaskFragmentBinding;
import com.example.roommateapp.databinding.UsersFragmentBinding;
import com.example.roommateapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditTaskFragment extends Fragment {

    private EditTaskFragmentBinding binding;
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

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = EditTaskFragmentBinding.inflate(inflater, container, false);

        binding.signoutButton.setOnClickListener(e -> {

            signOut();

            NavHostFragment.findNavController(EditTaskFragment.this)
                    .navigate(R.id.action_EditTaskFragment_to_LoginFragment);
        });

        binding.tasksButton.setOnClickListener(e -> NavHostFragment.findNavController(EditTaskFragment.this).navigate(R.id.action_EditTaskFragment_to_ListItemFragment));

        /** Set edit text to whatever the name in the DB is **/
        binding.taskName.setText("Task Name in DB");

        /** Make button save name change **/


        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        binding.usersList.setOnClickListener(e -> NavHostFragment.findNavController(UsersFragment.this).navigate(R.id.action_UsersFragment_to_GroupsFragment));
//        binding.createButton.setOnClickListener(e -> create());
//        binding.deleteButton.setOnClickListener(e -> delete());
//        binding.updateButton.setOnClickListener(e -> update());
    }

    private void signOut() {
        mAuth.signOut();
        Toast.makeText(getContext(), "Successful Sign Out.",
                Toast.LENGTH_SHORT).show();
    }
}