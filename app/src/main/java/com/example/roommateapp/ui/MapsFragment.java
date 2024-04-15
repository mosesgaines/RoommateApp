package com.example.roommateapp.ui;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.roommateapp.R;
import com.example.roommateapp.databinding.ListFragmentBinding;
import com.example.roommateapp.databinding.MapsFragmentBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private MapsFragmentBinding binding;
    private static final int REQUEST_CODE = 101;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private FirebaseAuth mAuth;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.maps_fragment, container, false);

        binding = MapsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        getCurrentLocation();

        binding.listsButton.setOnClickListener(e -> NavHostFragment.findNavController(MapsFragment.this).navigate(R.id.action_MapsFragment_to_ListFragment));
        binding.signoutButton.setOnClickListener(e -> {

            signOut();

            NavHostFragment.findNavController(MapsFragment.this)
                    .navigate(R.id.action_MapsFragment_to_LoginFragment);
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            Toast.makeText(this.getContext(), "If you deny, you will not be able to see your current location on the map.", Toast.LENGTH_SHORT).show();
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();

        locationTask.addOnCompleteListener(task -> locationTask.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;

                // Initialize map fragment
                SupportMapFragment supportMapFragment = (SupportMapFragment)
                        getChildFragmentManager().findFragmentById(R.id.google_map);
                assert supportMapFragment != null;
                supportMapFragment.getMapAsync(MapsFragment.this);
            }
        }));
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getCurrentLocation();
//            } else {
//                Toast.makeText(this.getContext(), "Location permission is denied, please allow the permission to see your location", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    private void signOut() {
        mAuth.signOut();
        Toast.makeText(getContext(), "Successful Sign Out.",
                Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
            LatLng currLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(currLocation).title("My Location"));
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(currLocation));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLocation, 10));
    }


}