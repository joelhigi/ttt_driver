package com.tartantransporttracker.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tartantransporttracker.driver.databinding.ActivityMapBinding;
import com.tartantransporttracker.driver.managers.UserManager;
import com.tartantransporttracker.driver.service.LocationSharingService;

import java.util.ArrayList;
import java.util.List;


public class MapActivity extends DrawerBaseActivity {

    ActivityMapBinding activityMapBinding;
    private static final int RC_SIGN_IN = 123;
    private UserManager userManager = UserManager.getInstance();


    private Switch location_switch;
    private boolean location_service = false;

    private FirebaseFirestore tttFireStore;
    private FirebaseUser tttUser;

    private String uid;
    private String userName;
    private String userRole;
    private String[] routes;
    private ArrayList<String> receivedString = new ArrayList<>();
    private ArrayList<String> receivedID = new ArrayList<>();
    private String chosenRoute;
    private String chosenRouteID;
    private String refAddress;
    private Double latitude;
    private Double longitude;
    private FirebaseDatabase tttRealTime;
    private DatabaseReference tttRealTimeRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMapBinding = activityMapBinding.inflate(getLayoutInflater());

        setContentView(activityMapBinding.getRoot());
        nameActivityTitle(getString(R.string.driver_map));

        location_switch = findViewById(R.id.driver_location_switch);
        location_switch.setText(getString(R.string.sharing_switch));

        ActivityCompat.requestPermissions(MapActivity.this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, 100);

        tttUser = FirebaseAuth.getInstance().getCurrentUser();
        tttFireStore = FirebaseFirestore.getInstance();
        uid = tttUser.getUid();
        userName = tttUser.getDisplayName();

        DocumentReference docRef = tttFireStore.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        userRole = document.get("role").toString();
                    } else {
                        Log.d("Failed", "No such document");
                    }
                } else {
                    Log.d("Failed 2", "get failed with ", task.getException());
                }
            }
        });


        location_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                location_service = isChecked;
                if(location_service)
                {
                    CollectionReference routeColl = tttFireStore.collection("routes");
                    routeColl.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            QuerySnapshot routeQ = task.getResult();

                            List<DocumentSnapshot> docs = routeQ.getDocuments();
                            for(DocumentSnapshot d:docs)
                            {

                                receivedString.add(d.get("name").toString());
                            }


                            routes = new String[receivedString.size()];
                            for (int i = 0; i < receivedString.size(); i++)
                            {
                                routes[i] = receivedString.get(i);
                                receivedID.add(routeQ.getDocuments().get(i).getId());
                            }


                            AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                            builder.setTitle("Pick a Route");
                            builder.setItems(routes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int choice) {
                                    chosenRoute = routes[choice];
                                    chosenRouteID = receivedID.get(choice);
                                    DocumentReference notify = tttFireStore.collection("routes").document(chosenRouteID);
                                    notify.update("time",Timestamp.now());

                                    Intent intent = new Intent(getBaseContext(), LocationSharingService.class);
                                    intent.putExtra("userRole",userRole);
                                    intent.putExtra("userID",uid);
                                    intent.putExtra("userName",userName);
                                    intent.putExtra("chosenRoute",chosenRoute);
                                    startService(intent);

                                    tttRealTime = FirebaseDatabase.getInstance("https://noble-radio-299516-default-rtdb.europe-west1.firebasedatabase.app/");
                                    refAddress = "journeys/routes/"+chosenRoute;
                                    tttRealTimeRef = tttRealTime.getReference(refAddress);

                                    Log.e("OO",refAddress);
                                    //Listening for LatLng changes
                                    ValueEventListener tttRealTimeListener = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            if(snapshot.exists()) {
                                                boolean shareStatus = snapshot.child("sharingStatus").getValue(Boolean.class);
                                                if (shareStatus) {
                                                    latitude = snapshot.child("latitude").getValue(Double.class);
                                                    longitude = snapshot.child("longitude").getValue(Double.class);
                                                    FragmentManager fragManager = getSupportFragmentManager();
                                                    FragmentTransaction fragSwitch = fragManager.beginTransaction();
                                                    FragmentContainerView fragContainer = findViewById(R.id.fragmentContainerView);
                                                    fragContainer.removeAllViews();
                                                    fragSwitch.replace(R.id.fragmentContainerView, new DriverMapFragment(latitude,longitude));
                                                    fragSwitch.commit();
                                                }

                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Log.e("Authorization Error","Database Inaccessible");
                                        }
                                    };
                                    tttRealTimeRef.addValueEventListener(tttRealTimeListener);
                                }

                            });
                            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    location_switch.setChecked(false);
                                }
                            });
                            builder.show();
                        }
                    });

                }
                else
                {
                    receivedString.clear();
                    stopService(new Intent(getBaseContext(), LocationSharingService.class));
                }
            }
        });


    }


}