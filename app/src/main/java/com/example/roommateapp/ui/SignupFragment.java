package com.example.roommateapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.roommateapp.databinding.LoginFragmentBinding;
import com.example.roommateapp.databinding.SignupFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupFragment extends Fragment {

    private SignupFragmentBinding binding;
    private FirebaseAuth mAuth;

    private static final String TAG = "SignupFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

    }
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = SignupFragmentBinding.inflate(inflater, container, false);
//        Log.d(TAG, "onCreateView(LayoutInflater, ViewGroup, Bundle) called");
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.exitButton.setOnClickListener(e -> NavHostFragment.findNavController(SignupFragment.this)
                .navigate(R.id.action_SignupFragment_to_LoginFragment));

        binding.doneButton.setOnClickListener(e -> {
            String email = binding.email.getText().toString();
            String password = binding.password.getText().toString();
            String confirmPassword = binding.passwordConfirm.getText().toString();
            if (password.equals(confirmPassword)) {
                createAccount(email, password);
            } else {
                Toast.makeText(getContext(), "Passwords do not match.",
                        Toast.LENGTH_SHORT).show();
            }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = binding.email.getText().toString();
        if (TextUtils.isEmpty(email)) {
            binding.email.setError("Required.");
            valid = false;
        } else {
            binding.email.setError(null);
        }

        String password = binding.password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            binding.password.setError("Required.");
            valid = false;
        } else {
            binding.password.setError(null);
        }

        String confirmPassword = binding.passwordConfirm.getText().toString();
        if (TextUtils.isEmpty(confirmPassword)) {
            binding.passwordConfirm.setError("Required.");
            valid = false;
        } else {
            binding.passwordConfirm.setError(null);
        }

        return valid;
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity(), task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, go to login page
                            NavHostFragment.findNavController(SignupFragment.this)
                                    .navigate(R.id.action_SignupFragment_to_LoginFragment);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

        });
    }

}