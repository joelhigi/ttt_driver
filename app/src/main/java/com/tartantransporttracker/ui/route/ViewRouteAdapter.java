package com.tartantransporttracker.ui.route;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tartantransporttracker.R;
import com.tartantransporttracker.managers.BusStopManager;
import com.tartantransporttracker.models.BusStop;
import com.tartantransporttracker.models.Route;

import java.util.ArrayList;
import java.util.List;

public class ViewRouteAdapter extends RecyclerView.Adapter<ViewRouteVH> {
    List<Route> routes;
    List<BusStop> busStops = new ArrayList<>();
    private BusStopManager busStopManager = BusStopManager.getInstance();

    public ViewRouteAdapter(List<Route> routeItem) {
        this.routes = routeItem;
    }

    @NonNull
    @Override
    public ViewRouteVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_item, parent, false);
        return new ViewRouteVH(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewRouteVH holder, int position) {
        try {
            Route route = routes.get(position);
            busStopManager.findAllBusStops()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                String secondBStop = "";
                                int count = 1;
                                for (DocumentSnapshot doc : list) {
                                    BusStop bs = doc.toObject(BusStop.class);
                                    if (bs.getRoute() != null) {
                                        if (bs.getRoute().getName() != null) {
                                            if (bs.getRoute().getName().equalsIgnoreCase(route.getName())) {
                                                secondBStop += "" + count + ". " + bs.getBusStopName() + "\n";
                                                count++;
                                            }
                                        }
                                    }
                                }
                                holder.fromVenue.setText(secondBStop);
                            } else {
                                Log.w("Message:", "No data found in the database");
                            }
                        }
                    });
            holder.routeName.setText(route.getName());
        } catch (Exception ex) {
            Log.w("Message:", "Unable to retrieve bus stop");
        }
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    public boolean populateBusStopList() {
        busStopManager.findAllBusStops()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot doc : list) {
                                BusStop route = doc.toObject(BusStop.class);
                                busStops.add(route);
                            }
                        } else {
                            Log.w("Message:", "No data found in the database");
                        }
                    }
                });
        if (busStops == null) {
            return false;
        }
        return true;
    }
}
