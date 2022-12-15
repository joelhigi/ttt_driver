package com.tartantransporttracker.driver.ui.busStop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tartantransporttracker.driver.DrawerBaseActivity;
import com.tartantransporttracker.driver.R;
import com.tartantransporttracker.driver.databinding.ActivityCreateRouteBinding;
import com.tartantransporttracker.driver.managers.BusStopManager;
import com.tartantransporttracker.driver.managers.RouteManager;
import com.tartantransporttracker.driver.models.BusStop;
import com.tartantransporttracker.driver.models.Route;

import java.util.ArrayList;

public class CreateBusStopActivity extends DrawerBaseActivity {

    private ArrayList<Route> items;
    private Spinner spinner;
    private Button btnCreateBusStop;
    private EditText edt_address;
    private EditText edt_position;
    private BusStopManager busStopManager;
    private RouteManager routeManager;
    private Route selectedRoute = new Route();
    private String busStopId;
    private BusStop busStop;
    private TextView managebusStop;
    private ActivityCreateRouteBinding activityCreateRouteBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCreateRouteBinding = ActivityCreateRouteBinding.inflate(getLayoutInflater());
        setContentView(activityCreateRouteBinding.getRoot());
        setContentView(R.layout.activity_create_bus_stop);
        nameActivityTitle(getString(R.string.create_busStop));
        busStopManager = new BusStopManager();
        routeManager = new RouteManager();


        Intent intent = getIntent();
//        busStopId = intent.getStringExtra("id");

        edt_address = findViewById(R.id.edtAddress);
        edt_position = findViewById(R.id.edtPosition);
        btnCreateBusStop = findViewById(R.id.btn_add_bus_stop);
        managebusStop = findViewById(R.id.manage_bus_stop);
        spinner = findViewById(R.id.selectRouteSpinner);

        items = new ArrayList<>();
        busStop = (BusStop) intent.getParcelableExtra("id");
        managebusStop.setText("Create Bus Stop");
        if (busStop != null) {
            nameActivityTitle(getString(R.string.update_bus_stop_title));
            btnCreateBusStop.setText("Update Bus Stop");
            managebusStop.setText("Update Bus Stop");
            ArrayList<String> selectedBusStopNames = new ArrayList<>();
            selectedBusStopNames.add(busStop.getBusStopName());
            ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, selectedBusStopNames);
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            spinner.setAdapter(adapter);
            edt_address.setText(busStop.getBusStopName());
            edt_position.setText(String.valueOf(busStop.getPosition()));
        }
        setUpRoutesSpinner();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Route route = (Route) parent.getItemAtPosition(position);
                if (route != null) {
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
                BusStop busStopObj = new BusStop(address, selectedRoute, position);
                if (busStop != null) {
                    busStopObj.setRoute(busStop.getRoute());
                    busStopManager.updateBusStop(busStop.getId(), busStopObj);
                    Toast.makeText(CreateBusStopActivity.this, "Bus stop updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    busStopManager.createBusStop(busStopObj);
                    Toast.makeText(CreateBusStopActivity.this, "Bus stop created", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(), BusStopView.class);
                startActivity(intent);
            }
        });
    }

    private void setUpRoutesSpinner() {
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, items);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);

        spinner.setAdapter(adapter);

        routeManager.findAllRoutes().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Route route = document.toObject(Route.class);
                        items.add(route);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private ArrayList<Route> getAvailableRoutes() {
        ArrayList<Route> allRoutes = new ArrayList<>();
        routeManager.findAllRoutes().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
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