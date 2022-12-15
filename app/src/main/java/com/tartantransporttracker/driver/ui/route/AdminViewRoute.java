package com.tartantransporttracker.driver.ui.route;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tartantransporttracker.driver.DrawerBaseActivity;
import com.tartantransporttracker.driver.R;
import com.tartantransporttracker.driver.databinding.ActivityAdminViewRouteBinding;
import com.tartantransporttracker.driver.managers.BusStopManager;
import com.tartantransporttracker.driver.managers.RouteManager;
import com.tartantransporttracker.driver.managers.UserManager;
import com.tartantransporttracker.driver.models.BusStop;
import com.tartantransporttracker.driver.models.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class AdminViewRoute extends DrawerBaseActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityAdminViewRouteBinding activityAdminViewRouteBinding;
    private RouteManager routeManager = RouteManager.getInstance();
    private BusStopManager busStopManager = BusStopManager.getInstance();
    private UserManager user = UserManager.getInstance();
    private List<Route> routes = new ArrayList<>();
    private TextView busStopNum ;
    private TextView routesNum ;
    private ViewRouteAdapter adapter;
    private RecyclerView recyclerView;
    private SearchView searchRouteBtn;
    private FloatingActionButton addRoute;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAdminViewRouteBinding = ActivityAdminViewRouteBinding.inflate(getLayoutInflater());
//        setContentView(activityAdminViewRouteBinding.getRoot());
        setContentView(R.layout.activity_view_route);
        nameActivityTitle("Manage Routes");
        busStopNum = findViewById(R.id.busStopNum);
        routesNum = findViewById(R.id.routesNum);
        recyclerView = findViewById(R.id.listOfRoute);
        addRoute = findViewById(R.id.addRoute);
        searchRouteBtn = findViewById(R.id.searchView);

        getLayoutInflater().inflate(R.layout.activity_drawer_base, null).getVisibility();
        addRoute.setOnClickListener(view -> {
            Intent intent = new Intent(this, CreateRouteActivity.class);
            startActivity(intent);
        });
        searchRouteBtn.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterRoutes(newText);
                return false;
            }
        });

        findAllRoute();
    }

    private void filterRoutes(String searchText) {
        List<Route> filteredRoutes = new ArrayList<>();
        for(Route route : routes)
        {
            if(route.getName().toLowerCase(Locale.ROOT).contains(searchText.toLowerCase(Locale.ROOT))){
                filteredRoutes.add(route);
            }
        }
        if(filteredRoutes.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "No routes found!!", Toast.LENGTH_SHORT).show();
        }else{
            adapter.filterRoutes(filteredRoutes);
        }

    }

    public void showStatistic()
    {
        List<BusStop> bStops = new ArrayList<>();
        busStopManager.findAllBusStops()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot doc:list){
                                BusStop route = doc.toObject(BusStop.class);
                                bStops.add(route);
                            }
                            busStopNum.setText(String.valueOf(bStops.size()));
                        }else{

                        }
                    }
                });
    }
    public void findAllRoute()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        showStatistic();
        routeManager.findAllRoutes().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot doc:list){
                                Route route = doc.toObject(Route.class);
                                routes.add(route);
                                adapter = new ViewRouteAdapter(routes);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyItemInserted(routes.size() -1);
                            }
                            routesNum.setText(String.valueOf(routes.size()));

                        }else{
                            Log.w("Message:","No data found in the database");
                        }
                    }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.SecondFragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

}