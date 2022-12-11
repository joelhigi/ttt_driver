package com.tartantransporttracker.ui.route;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tartantransporttracker.DrawerBaseActivity;
import com.tartantransporttracker.R;
import com.tartantransporttracker.databinding.ActivityCreateRouteBinding;
import com.tartantransporttracker.managers.RouteManager;
import com.tartantransporttracker.models.Route;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CreateRouteActivity extends DrawerBaseActivity {

    Button btnAddRoute;
    Button btnViewStops;
    EditText routeName;
    RouteManager routeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_route);

        routeManager = new RouteManager();

        btnAddRoute = (Button) findViewById(R.id.createRouteBtn);
        btnViewStops = findViewById(R.id.viewStops);

        routeName = findViewById(R.id.route_name);

        btnAddRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = routeName.getText().toString();

                Boolean routeExists = routeExists(name);
                if (!routeExists) {
                    Route route = new Route(name);
                    routeManager.createRoute(route);

                    routeName.setText("");
                    routeName.clearFocus();

                    Toast.makeText(getApplicationContext(), "Route created", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), AdminViewRoute.class);
                    startActivity(intent);
                } else {

                }
            }
        });

        btnViewStops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateBusStopActivity.class);
                startActivity(intent);
            }
        });
    }

    private List<Route> getAvailableRoutes()
    {
        List<Route> allRoutes = new ArrayList<>();
        routeManager.findAllRoutes().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Route route = document.toObject(Route.class);
                                allRoutes.add(route);
                            }
                        }
                    }
                }
        );
        return allRoutes;
    }

    private boolean routeExists (String name)
    {
        List<Route> routes = getAvailableRoutes();
        Iterator<Route> iterator = routes.iterator();
        if (iterator.hasNext())
        {
            if (iterator.next().getName().equalsIgnoreCase(name))
            {
                return true;
            }
        }
        return false;
    }
}