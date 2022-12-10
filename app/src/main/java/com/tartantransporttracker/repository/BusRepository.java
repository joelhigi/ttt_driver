package com.tartantransporttracker.repository;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tartantransporttracker.models.BusStop;

import java.util.ArrayList;
import java.util.List;

public class BusRepository {
    private static volatile BusRepository instance;
    private static  final String COLLECTION_NAME="busStops";

    public  BusRepository(){}

    public static BusRepository getInstance(){
        synchronized (BusRepository.class){
            BusRepository result = instance;
            if(result != null){
                return  result;
            }
            if(instance == null){
                instance = new BusRepository();
            }
            return instance;
        }
    }

    // firestore functions

    // get document reference
    private CollectionReference getBusStopsCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // create route in Firestore
    public void createBusStop(BusStop route){
        this.getBusStopsCollection().add(route)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    // get all data from firestore
    public Task<QuerySnapshot> findAll(){
//        List<BusStop> busStops = new ArrayList<>();
//        this.getBusStopsCollection().get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        if(!queryDocumentSnapshots.isEmpty()){
//                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
//                            for(DocumentSnapshot doc:list){
//                                BusStop route = doc.toObject(BusStop.class);
//                                busStops.add(route);
//                            }
//                        }else{
//                            Log.w(TAG,"No data found in the database");
//                        }
//                    }
//                });
        return this.getBusStopsCollection().get();
    }


    //Get Single route from firestore
    public Task<DocumentSnapshot> getBusStop(String id){
        if(id !=null){
            return  this.getBusStopsCollection().document(id).get();
        }
        return null;
    }


    // Update BusStop
    public void updateBusStop(String id,BusStop updatedBusStop){
        this.getBusStopsCollection()
                .document(id)
                .set(updatedBusStop)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.w(TAG,"BusStop updated successfully");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"BusStop not updated");
                    }
                });
    }

    // Delete the BusStop from Firestore
    public void deleteBusStop(String id) {
        if(id != null){
            this.getBusStopsCollection().document(id).delete();
        }
    }
}
