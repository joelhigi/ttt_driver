package com.tartantransporttracker.ui.busStop;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tartantransporttracker.R;
import com.tartantransporttracker.managers.BusStopManager;
import com.tartantransporttracker.models.BusStop;
import com.tartantransporttracker.ui.route.ViewRouteAdapter;

import java.util.ArrayList;
import java.util.List;

public class BusStopView extends AppCompatActivity {
    private BusStopManager busStopManager = BusStopManager.getInstance();
    TextView busStopNum;
    List<BusStop> bStops = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_stop);
        busStopNum = findViewById(R.id.numberOfBusStops);
        if(!populateBusStopList())
        {
            Toast.makeText(this, "No Bus STOP found", Toast.LENGTH_LONG);
        }
    }

    public boolean populateBusStopList() {
        RecyclerView recyclerView = findViewById(R.id.busStopViewList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        busStopManager.findAllBusStops()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot doc:list){
                                BusStop route = doc.toObject(BusStop.class);
                                bStops.add(route);
                                BusStopAdapter adapter = new BusStopAdapter(bStops);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyItemInserted(bStops.size() - 1);
                            }
                            busStopNum.setText(String.valueOf(bStops.size()));
                        }else{
                            Log.w("Message:","No data found in the database");
                        }
                    }
                });
        if (bStops == null) {
            return false;
        }
        return true;
    }

}