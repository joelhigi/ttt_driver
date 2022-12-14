package com.tartantransporttracker.driver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DriverMapFragment extends Fragment {

    private Double latitude = -1.9353;
    private Double longitude = 30.1586;
    private String title = "CMU Africa";

    public DriverMapFragment(Double lat,Double longi)
    {
        latitude = lat;
        longitude = longi;
        title = "You are here";
    }

    public DriverMapFragment()
    {

    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng current = new LatLng(latitude,longitude);
            googleMap.addMarker(new MarkerOptions().position(current).title(title));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(current));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 15));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_driver_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}