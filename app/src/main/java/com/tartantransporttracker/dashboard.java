package com.tartantransporttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tartantransporttracker.databinding.ActivityDashboardBinding;

public class dashboard extends DrawerBaseActivity {

    ActivityDashboardBinding activityDashboardBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardBinding.getRoot());
        nameActivityTitle("joel");
    }
}