package com.tartantransporttracker.ui.route;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
import com.google.android.material.navigation.NavigationView;
import com.tartantransporttracker.BaseActivity;
import com.tartantransporttracker.DrawerBaseActivity;
import com.tartantransporttracker.MainActivity;
import com.tartantransporttracker.R;
import com.tartantransporttracker.managers.BusStopManager;
import com.tartantransporttracker.managers.RouteManager;
import com.tartantransporttracker.managers.UserManager;
import com.tartantransporttracker.models.BusStop;
import com.tartantransporttracker.models.Route;

import java.util.ArrayList;
import java.util.List;

import com.tartantransporttracker.databinding.ActivityAdminViewRouteBinding;
import com.tartantransporttracker.models.User;
import com.tartantransporttracker.ui.busStop.BusStopAdapter;

public class AdminViewRoute extends DrawerBaseActivity{

    private AppBarConfiguration appBarConfiguration;
    ActivityAdminViewRouteBinding activityAdminViewRouteBinding;

    Button deleteBtn;
    FloatingActionButton floatingActionButton;
    private RouteManager routeManager = RouteManager.getInstance();
    private BusStopManager busStopManager = BusStopManager.getInstance();
    private UserManager user = UserManager.getInstance();
    private List<Route> routes = new ArrayList<>();
    TextView busStopNum ;
    TextView routesNum ;
    RecyclerView recyclerView;
    FloatingActionButton addRoute;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAdminViewRouteBinding = ActivityAdminViewRouteBinding.inflate(getLayoutInflater());
        setContentView(activityAdminViewRouteBinding.getRoot());
        setContentView(R.layout.activity_view_route);
        nameActivityTitle("Manage Routes");
        busStopNum = findViewById(R.id.busStopNum);
        routesNum = findViewById(R.id.routesNum);
        recyclerView = findViewById(R.id.listOfRoute);
        addRoute = findViewById(R.id.addRoute);

        getLayoutInflater().inflate(R.layout.activity_drawer_base, null).getVisibility();
        addRoute.setOnClickListener(view -> {
            Intent intent = new Intent(this, CreateRouteActivity.class);
            startActivity(intent);
        });

        findAllRoute();
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
                            Log.w("Message:","No data found in the database");
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
                                ViewRouteAdapter adapter = new ViewRouteAdapter(routes);
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