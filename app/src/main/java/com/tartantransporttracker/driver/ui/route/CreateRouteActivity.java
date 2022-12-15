package com.tartantransporttracker.driver.ui.route;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tartantransporttracker.driver.DrawerBaseActivity;
import com.tartantransporttracker.driver.R;
import com.tartantransporttracker.driver.databinding.ActivityCreateRouteBinding;
import com.tartantransporttracker.driver.models.Route;
import com.tartantransporttracker.driver.ui.busStop.CreateBusStopActivity;
import com.tartantransporttracker.driver.managers.RouteManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CreateRouteActivity extends DrawerBaseActivity {

    private Button btnAddRoute;
    private EditText routeName;
    private RouteManager routeManager;
    private ActivityCreateRouteBinding activityCreateRouteBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCreateRouteBinding = ActivityCreateRouteBinding.inflate(getLayoutInflater());
        setContentView(activityCreateRouteBinding.getRoot());
        nameActivityTitle(getString(R.string.create_route));

        routeManager = new RouteManager();

        btnAddRoute = (Button) findViewById(R.id.createRouteBtn);

        routeName = findViewById(R.id.route_name);

        btnAddRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = routeName.getText().toString();

                Boolean routeExists = routeExists(name);
                if (!routeExists) {
                    if(!name.isEmpty())
                    {
                        Route route = new Route(name);
                        routeManager.createRoute(route);

                        routeName.setText("");
                        routeName.clearFocus();

                        Toast.makeText(getApplicationContext(), "Route created", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), AdminViewRoute.class);
                        startActivity(intent);
                    }else
                    {
                        Toast.makeText(getApplicationContext(), "Route name is empty!", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Route already exist", Toast.LENGTH_LONG).show();
                }
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