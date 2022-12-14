package com.tartantransporttracker.driver.ui.busStop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tartantransporttracker.driver.DrawerBaseActivity;
import com.tartantransporttracker.driver.R;
import com.tartantransporttracker.driver.databinding.ActivityBusStopBinding;
import com.tartantransporttracker.driver.managers.BusStopManager;
import com.tartantransporttracker.driver.models.BusStop;

import java.util.ArrayList;
import java.util.List;

public class BusStopView extends DrawerBaseActivity {
    private BusStopManager busStopManager = BusStopManager.getInstance();
    private TextView busStopNum;
    private FloatingActionButton busStopBtn;
    private List<BusStop> bStops = new ArrayList<>();
    private ActivityBusStopBinding activityBusStopBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBusStopBinding = ActivityBusStopBinding.inflate(getLayoutInflater());
        setContentView(activityBusStopBinding.getRoot());
        nameActivityTitle(getString(R.string.create_busStop));
        busStopNum = findViewById(R.id.numberOfBusStops);
        busStopBtn = findViewById(R.id.busStopBtn);
        if(!populateBusStopList())
        {
            Toast.makeText(this, "No Bus STOP found", Toast.LENGTH_LONG);
        }
        busStopBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, CreateBusStopActivity.class);
            startActivity(intent);
        });
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