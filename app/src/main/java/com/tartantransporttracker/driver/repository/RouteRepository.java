package com.tartantransporttracker.driver.repository;

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
import com.tartantransporttracker.driver.models.Route;

import java.util.ArrayList;
import java.util.List;

public class RouteRepository {

    private static volatile RouteRepository instance;
    private static  final String COLLECTION_NAME="routes";

    public  RouteRepository(){}

    public static RouteRepository getInstance(){
        synchronized (RouteRepository.class){
           RouteRepository result = instance;
            if(result != null){
                return  result;
            }
            if(instance == null){
                instance = new RouteRepository();
            }
            return instance;
        }
    }

    // firestore functions

    // get document reference
    private CollectionReference getRoutesCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // create route in Firestore
    public void createRoute(Route route){
           this.getRoutesCollection().add(route)
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
        List<Route> routes = new ArrayList<>();
        return this.getRoutesCollection().get();
    }

    //Get Single route from firestore
    public Task<DocumentSnapshot> getRoute(String id){
        if(id !=null){
            return  this.getRoutesCollection().document(id).get();
        }
        return null;
    }


    // Update Route
    public void updateRoute(String id, Route updatedRoute){
        this.getRoutesCollection()
                .document(id)
                .set(updatedRoute)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.w(TAG,"Route updated successfully");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"Route not updated");
                    }
                });
    }
    // Delete the Route from Firestore
    public void deleteRoute(String id) {
        if(id != null){
            this.getRoutesCollection().document(id).delete();
        }
    }
    
}
