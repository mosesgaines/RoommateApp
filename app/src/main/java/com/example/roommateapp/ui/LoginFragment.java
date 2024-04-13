package com.example.roommateapp.ui;

import static com.example.roommateapp.ui.MainActivity.setCurrUser;

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
import com.example.roommateapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class LoginFragment extends Fragment {

    private LoginFragmentBinding binding;
    private FirebaseAuth mAuth;
    private static final String TAG = "LoginFragment";


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

//    @Override
//    public void onStop() {
//        super.onStop();
//        binding = null;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        binding = null;
//    }

    @Override
    public void onPause() {
        super.onPause();
        binding = null;
        Log.d(TAG, "onPause() called");
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        super.onCreateView()
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        Log.d(TAG, "onDestroyView() called");
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
                            //Get user info from db
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            CollectionReference usersRef = db.collection("users");
                            Query query = usersRef.whereEqualTo("email", email);
                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot user : task.getResult()) {
                                            Log.d(TAG, user.getId() + " => " + user.getData());
                                            User userGet = new User(Long.parseLong(user.getId()), user, usersRef.document(user.getId()));
                                            setCurrUser(userGet);
                                        }

                                        NavHostFragment.findNavController(LoginFragment.this)
                                                .navigate(R.id.action_LoginFragment_to_GroupsFragment);
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });

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