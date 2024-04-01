package com.example.roommateapp.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roommateapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private int CHECK_MAPS_PERMISSIONS = 1;
    private static final int REQUEST_CODE = 101;
    private GoogleMap mMap;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        getCurrentLocation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.maps_fragment, container, false);
        return view;
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;

                    // Initialize map fragment
                    SupportMapFragment supportMapFragment = (SupportMapFragment)
                            getChildFragmentManager().findFragmentById(R.id.google_map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MapsFragment.this);
                    // Async map
//                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
//                        @Override
//                        public void onMapReady(GoogleMap googleMap) {
//                            // When map is loaded
//                            LatLng currLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//                            googleMap.addMarker(new MarkerOptions().position(currLocation).title("My Location"));
//                            googleMap.animateCamera(CameraUpdateFactory.newLatLng(currLocation));
//                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLocation, 10));
////                            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
////                                @Override
////                                public void onMapClick(LatLng latLng) {
////                                    // When clicked on map
////                                    // Initialize marker options
////                                    MarkerOptions markerOptions = new MarkerOptions();
////                                    // Set position of marker
////                                    markerOptions.position(latLng);
////                                    // Set title of marker
////                                    markerOptions.title(latLng.latitude + " : " + latLng.longitude);
////                                    // Remove all marker
////                                    googleMap.clear();
////                                    // Animating to zoom the marker
////                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
////                                    // Add marker on map
////                                    googleMap.addMarker(markerOptions);
////                                }
////                            });
//                        }
//                    });
                }
            }
        });
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




    @Override
    public void onMapReady(GoogleMap googleMap) {
            LatLng currLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(currLocation).title("My Location"));
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(currLocation));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLocation, 10));
    }


}