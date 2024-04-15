package com.example.roommateapp.ui;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.roommateapp.ui.MainActivity.getCurrList;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.roommateapp.R;
import com.example.roommateapp.UserLVAdapter;
import com.example.roommateapp.databinding.EditListFragmentBinding;
import com.example.roommateapp.databinding.UsersFragmentBinding;
import com.example.roommateapp.model.TaskList;
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
    private ConnectivityManager connectivityManager;

    private ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            Log.d(TAG, "onLost() called");
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

        binding = EditListFragmentBinding.inflate(inflater, container, false);

        /**  set text with the name of the list that was clicked on **/
        binding.listName.setText(currListName());

        /** make update button change the name in the DB **/
        binding.saveButton.setOnClickListener(e -> {
            changeListName(binding.listName.getText().toString());
        });

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
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }

    private void signOut() {
        mAuth.signOut();
        Toast.makeText(getContext(), "Successful Sign Out.",
                Toast.LENGTH_SHORT).show();
    }

    private String currListName() {
        TaskList currList = getCurrList();
        return currList.getName();
    }

    private void changeListName(String name) {
        TaskList currList = getCurrList();
        currList.changeName(name);
    }
}