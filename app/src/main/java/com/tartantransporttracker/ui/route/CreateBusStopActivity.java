package com.tartantransporttracker.ui.route;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tartantransporttracker.DrawerBaseActivity;
import com.tartantransporttracker.R;
import com.tartantransporttracker.databinding.ActivityCreateBusStopBinding;
import com.tartantransporttracker.managers.BusStopManager;
import com.tartantransporttracker.managers.RouteManager;
import com.tartantransporttracker.models.BusStop;
import com.tartantransporttracker.models.Route;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CreateBusStopActivity extends DrawerBaseActivity {

    private ArrayList<Route> items;
    private Spinner spinner;
    private Button btnCreateBusStop;
    private EditText edt_address;
    private EditText edt_position;
    private BusStopManager busStopManager;
    private RouteManager routeManager;
    private Route selectedRoute = new Route();
    ActivityCreateBusStopBinding activityCreateBusStopBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCreateBusStopBinding = activityCreateBusStopBinding.inflate(getLayoutInflater());
        setContentView(activityCreateBusStopBinding.getRoot());
        nameActivityTitle("Create Bus Stop");

        busStopManager = new BusStopManager();
        routeManager = new RouteManager();

        edt_address = findViewById(R.id.edtAddress);
        edt_position = findViewById(R.id.edtPosition);
        btnCreateBusStop = findViewById(R.id.btn_add_bus_stop);
        spinner = findViewById(R.id.selectRouteSpinner);

        items = new ArrayList<>();

        setUpRoutesSpinner();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Route route = (Route) parent.getItemAtPosition(position);
                if (route != null)
                {
                    selectedRoute = route;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("SPINNER", "Nothing selected");
            }
        });

        btnCreateBusStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = edt_address.getText().toString();
                Integer position = Integer.parseInt(edt_position.getText().toString());

                BusStop busStop = new BusStop(address, selectedRoute, position);
                busStopManager.createBusStop(busStop);

                Toast.makeText(CreateBusStopActivity.this, "Bus stop created", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpRoutesSpinner()
    {
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, items);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner.setAdapter(adapter);

        routeManager.findAllRoutes().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Route route = document.toObject(Route.class);
                        items.add(route);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private ArrayList<Route> getAvailableRoutes()
    {
        ArrayList<Route> allRoutes = new ArrayList<>();
        routeManager.findAllRoutes().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Route route = document.toObject(Route.class);
                                items.add(route);
                            }
                        }
                    }
                }
        );
        return allRoutes;
    }
}