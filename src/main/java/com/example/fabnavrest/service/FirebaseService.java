package com.example.fabnavrest.service;

import com.example.fabnavrest.object.Parking;
import com.example.fabnavrest.object.Survey;
import com.example.fabnavrest.object.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.core.ApiFuture;
import java.sql.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.IgnoreExtraProperties;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.BinaryOperator;


@Service
@IgnoreExtraProperties
public class FirebaseService {

    @Autowired
    ParkingService parkingService;


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
        Map<String, Object> docData = parkingService.convertToMap(parking);
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("Parking").document(parking.getId()).set(docData);
        return collectionsApiFuture.get().getUpdateTime().toString();
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
        Map<Parking.occupancy,Integer> occupancyMap = new HashMap<>();
        occupancyMap.put(Parking.occupancy.valueOf("EMPTY"),4);
        occupancyMap.put(Parking.occupancy.valueOf("SOMEWHAT_FULL"),3);
        occupancyMap.put(Parking.occupancy.valueOf("HALF_FULL"),2);
        occupancyMap.put(Parking.occupancy.valueOf("ALMOST_FULL"),1);
        occupancyMap.put(Parking.occupancy.valueOf("FULL"),0);
        Firestore dbFirestore = FirestoreClient.getFirestore();

        ApiFuture<QuerySnapshot> future = dbFirestore.collection("Parking").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        User user = getUserDetails(userName);
        for (QueryDocumentSnapshot document : documents) {
            Parking p = parkingService.convertToParking(document);
            if(findDistance(lat, p.getLatitude(), lng, p.getLongitude()) < user.getRadius()){
                if(user.getSafetyPreference() && !p.getFinalSafety()){
                    continue;
                }
                if(user.getPaymentMax() < p.getFinalRate()){
                    continue;
                }
                if(occupancyMap.get(p.getFinalOccupancy()) < occupancyMap.get(user.getOccupancyPreference())){
                    continue;
                }
                return p;
            }
        }
        return null;

        /*Iterable<DocumentReference> docs = dbFirestore.collection("Parking").listDocuments();
        User user = getUserDetails(userName);
        for(DocumentReference doc: docs){

        }
        return null;*/
    }


    public String updateSurveyData(Survey survey) throws ExecutionException, InterruptedException, JSONException, JsonProcessingException, IllegalAccessException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("Parking").document(survey.getParkingId());
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        Parking parking = null;
        Integer maxSize = 5;
        if(document.exists()){
            parking = parkingService.convertToParking(document);
            Long[] rateHistory = parking.getRateHistory();
            List<Long> rateList = new ArrayList<Long>(Arrays.asList(rateHistory));
            rateList.add(survey.getRate());
            if(rateList.size()>maxSize){
                rateList.remove(0);
            }
            Long finaRate = rateList.stream()
                    .reduce(BinaryOperator.maxBy((o1, o2) -> Collections.frequency(rateList, o1) -
                            Collections.frequency(rateList, o2))).orElse(null);

            parking.setRateHistory(rateList.toArray(new Long[0]));
            parking.setFinalRate(finaRate);

            Boolean[] safetyHistory = parking.getSafetyHistory();
            List<Boolean> safetyList = new ArrayList<Boolean>(Arrays.asList(safetyHistory));
            safetyList.add(survey.getSafety());
            if(safetyList.size()>maxSize){
                safetyList.remove(0);
            }
            Boolean finalSafety = safetyList.stream()
                    .reduce(BinaryOperator.maxBy((o1, o2) -> Collections.frequency(safetyList, o1) -
                            Collections.frequency(safetyList, o2))).orElse(null);

            parking.setSafetyHistory(safetyList.toArray(new Boolean[0]));
            parking.setFinalSafety(finalSafety);

            Parking.occupancy[] occupancyHistory = parking.getOccupancyHistory();
            List<Parking.occupancy> occupancyList = new ArrayList<Parking.occupancy>(Arrays.asList(occupancyHistory));
            occupancyList.add(survey.getOccupancy());
            if(occupancyList.size()>maxSize){
                occupancyList.remove(0);
            }
            Parking.occupancy finalOccupancy = occupancyList.stream()
                    .reduce(BinaryOperator.maxBy((o1, o2) -> Collections.frequency(occupancyList, o1) -
                            Collections.frequency(occupancyList, o2))).orElse(null);

            parking.setOccupancyHistory(occupancyList.toArray(new Parking.occupancy[0]));
            parking.setFinalOccupancy(finalOccupancy);

            saveParkingDetails(parking);
            updateUserDetails(survey.getUserName());
            return "Done";
        }



        return  "Parking Entry Not Found";

    }

    public String updateUserDetails(String userName) throws ExecutionException, InterruptedException {
        User user = getUserDetails(userName);
        Integer num = user.getCompletedSurveys();
        if(num!=null)
            num++;
        user.setCompletedSurveys(num);
        Integer maxSurvey = 10;
        if(num!=null && num>=maxSurvey){
            user.setEligibleForRewards(true);
        }
        saveUserDetails(user);
        return  "Done";
    }






}
