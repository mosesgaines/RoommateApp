package com.example.roommateapp.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import android.util.Log;
import android.widget.Toast;

import com.example.roommateapp.R;
import com.example.roommateapp.databinding.LoginFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.android.gms.tasks.OnCompleteListener;


public class LoginFragment extends Fragment {

    private LoginFragmentBinding binding;
    private FirebaseAuth mAuth;
    private static final String TAG = "MainActivity";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = LoginFragmentBinding.inflate(inflater, container, false);
        Log.d(TAG, "onCreateView(LayoutInflater, ViewGroup, Bundle) called");
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.loginButton.setOnClickListener(e ->
                signIn(binding.email.getText().toString(), binding.password.getText().toString()));

        binding.signupText.setOnClickListener(e -> NavHostFragment.findNavController(LoginFragment.this)
                .navigate(R.id.action_LoginFragment_to_SignupFragment));


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

        return valid;
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(getContext(), "Success!",
                                    Toast.LENGTH_SHORT).show();
                            NavHostFragment.findNavController(LoginFragment.this)
                                    .navigate(R.id.action_LoginFragment_to_GroupsFragment);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}