package com.tartantransporttracker.driver.ui.route;

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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tartantransporttracker.driver.R;
import com.tartantransporttracker.driver.managers.RouteManager;
import com.tartantransporttracker.driver.models.Route;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UpdateRouteActivity extends AppCompatActivity {

    private Button updateBtn;
    private EditText edtName;
    private RouteManager routeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_route);

        updateBtn = (Button) findViewById(R.id.btnUpdateRoute);
        edtName = findViewById(R.id.route_name);
        routeManager = new RouteManager();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();

                Boolean routeExists = routeExists(name);
                if (!routeExists) {
                    Route route = new Route(name);
                    routeManager.createRoute(route);

                    edtName.setText("");
                    edtName.clearFocus();

                    Toast.makeText(getApplicationContext(), "Route created", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), AdminViewRoute.class);
                    startActivity(intent);
                } else {

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