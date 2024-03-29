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
import com.example.roommateapp.databinding.EditListFragmentBinding;
import com.example.roommateapp.databinding.UsersFragmentBinding;
import com.example.roommateapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditListFragment extends Fragment {

    private EditListFragmentBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private static final String TAG = "UsersFragment";
    private static String userID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = EditListFragmentBinding.inflate(inflater, container, false);

        /**  set text with the name of the list that was clicked on **/
        binding.listName.setText("List name from DB");

        /** make update button change the name in the DB **/


        binding.signoutButton.setOnClickListener(e -> {

            signOut();

            NavHostFragment.findNavController(EditListFragment.this)
                    .navigate(R.id.action_EditListFragment_to_LoginFragment);
        });

        binding.listsButton.setOnClickListener(e -> NavHostFragment.findNavController(EditListFragment.this).navigate(R.id.action_EditListFragment_to_ListFragment));



        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void signOut() {
        mAuth.signOut();
        Toast.makeText(getContext(), "Successful Sign Out.",
                Toast.LENGTH_SHORT).show();
    }
}