package com.example.fabnavrest.service;

import com.example.fabnavrest.object.Parking;
import com.example.fabnavrest.object.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firestore.v1.Document;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {



    public String saveUserDetails(User user) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("Users").document(user.getUserName()).set(user);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public User getUserDetails(String userName) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("Users").document(userName);
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DocumentSnapshot document = future.get();

        User user = null;

        if(document.exists()){
            user = document.toObject(User.class);
        }
        return user;
    }

    public Integer findRadius(String userName) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("Users").document(userName);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();

        User user = null;

        if(document.exists()){
            user = document.toObject(User.class);
        }
        return user.getRadius();
    }

    public String deleteUser(String userName) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("Users").document(userName).delete();
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public String saveParkingDetails(Parking parking) throws ExecutionException, InterruptedException, JsonProcessingException, JSONException, IllegalAccessException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("Parking").document(parking.getId());
        Map<String, Object> docData = convertToMap(parking);
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("Parking").document(parking.getId()).set(docData);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public Map<String, Object> convertToMap(Parking parking) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        for (Field field : Parking.class.getDeclaredFields()) {
            // Skip this if you intend to access to public fields only
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            if(field.getType().isArray()){
                //map.put(field.getName(), Arrays.asList(field.get(parking)));
            }else{
                map.put(field.getName(), field.get(parking));
            }
            map.put("rateHistory",Arrays.asList(parking.getRateHistory()));
            map.put("rateTimeHistory",Arrays.asList(parking.getRateTimeHistory()));
            map.put("safetyHistory",Arrays.asList(parking.getSafetyHistory()));
            map.put("safetyTimeHistory",Arrays.asList(parking.getSafetyTimeHistory()));
            map.put("occupancyHistory",Arrays.asList(parking.getOccupancyHistory()));
            map.put("occupancyTimeHistory",Arrays.asList(parking.getOccupancyTimeHistory()));
        }
        return map;
    }

    public static double findDistance (double LatOne, double LatTwo, double LonOne, double LonTwo)
    {
        // toRadians function converts degrees to radians.
        LonOne = Math.toRadians(LonOne);
        LonTwo = Math.toRadians(LonTwo);
        LatOne = Math.toRadians(LatOne);
        LatTwo = Math.toRadians(LatTwo);

        // Here the Haversine formula is being created
        double deltaLon = LonTwo - LonOne;
        double deltaLat = LatTwo - LatOne;
        double formula = Math.pow(Math.sin(deltaLat / 2), 2)
                + Math.cos(LatOne) * Math.cos(LatTwo)
                * Math.pow(Math.sin(deltaLon / 2),2);
        double fOutput = 2 * Math.asin(Math.sqrt(formula));

        // Earth's Radius multiplied by OP
        double r = 3956;
        return(fOutput * r);

    }

    public Parking getSuggestedParking(String userName, Double lat, Double lng) throws ExecutionException, InterruptedException {
        Map<String,Integer> occupancyMap = new HashMap<>();
        occupancyMap.put("EMPTY",4);
        occupancyMap.put("SOMEWHAT_FULL",3);
        occupancyMap.put("HALF_FULL",2);
        occupancyMap.put("ALMOST_FULL",1);
        occupancyMap.put("FULL",0);
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Iterable<DocumentReference> docs = dbFirestore.collection("Parking").listDocuments();
        User user = getUserDetails(userName);
        for(DocumentReference doc: docs){
            ApiFuture<DocumentSnapshot> future = doc.get();
            DocumentSnapshot document = future.get();
            Parking p = document.toObject(Parking.class);
            if(findDistance(lat, p.getLatitude(), lng, p.getLongitude()) < user.getRadius()){
                if(user.getSafetyPreference() && !p.getFinalSafety()){
                    break;
                }
                if(user.getPaymentMax() < p.getFinalRate()){
                    break;
                }
                if(occupancyMap.get(p.getFinalOccupancy()) < occupancyMap.get(user.getOccupancyPreference())){
                    break;
                }
                return p;
            }
        }
        return null;
    }



}
