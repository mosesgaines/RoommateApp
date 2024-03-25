package com.example.roommateapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.roommateapp.R;
import com.example.roommateapp.databinding.GroupsFragmentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class GroupsFragment extends Fragment {

    private GroupsFragmentBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


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

        binding = GroupsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.signoutButton.setOnClickListener(e -> {

            signOut();

            NavHostFragment.findNavController(GroupsFragment.this)
                    .navigate(R.id.action_GroupsFragment_to_LoginFragment);
        });

        binding.usersButton.setOnClickListener(e -> {
            NavHostFragment.findNavController(GroupsFragment.this).navigate(R.id.action_GroupsFragment_to_UsersFragment);
        });
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