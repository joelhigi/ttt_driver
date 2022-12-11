package com.tartantransporttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tartantransporttracker.databinding.ActivityMapBinding;

public class MapActivity extends DrawerBaseActivity {

    ActivityMapBinding activityMapBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMapBinding = activityMapBinding.inflate(getLayoutInflater());
        setContentView(activityMapBinding.getRoot());
        nameActivityTitle("map");
    }
}