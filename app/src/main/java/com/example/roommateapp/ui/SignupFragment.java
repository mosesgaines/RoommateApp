package com.example.roommateapp.ui;

import static androidx.core.content.ContextCompat.getSystemService;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
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

import com.example.roommateapp.R;
import com.example.roommateapp.databinding.SignupFragmentBinding;
import com.example.roommateapp.model.User;
import com.google.firebase.auth.FirebaseAuth;

public class SignupFragment extends Fragment {

    private SignupFragmentBinding binding;
    private FirebaseAuth mAuth;
    private static final String TAG = "SignupFragment";
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
            String name = binding.fullName.getText().toString();
            String confirmPassword = binding.passwordConfirm.getText().toString();
            if (password.equals(confirmPassword)) {
                createAccount(name, email, password);

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
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }

    private boolean validateForm() {
        boolean valid = true;

        String name = binding.fullName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            binding.fullName.setError("Required.");
            valid = false;
        } else {
            binding.fullName.setError(null);
        }

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

    private void createAccount(String name, String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity(), task -> {
                        if (task.isSuccessful()) {
                            //Create new User in the database
                            User newUser = new User(name, email);
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