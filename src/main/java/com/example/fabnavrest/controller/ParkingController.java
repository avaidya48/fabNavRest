package com.example.fabnavrest.controller;

import com.example.fabnavrest.object.Parking;
import com.example.fabnavrest.object.Survey;
import com.example.fabnavrest.object.User;
import com.example.fabnavrest.service.FirebaseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.sql.Timestamp;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

@RestController
public class ParkingController {

    @Autowired
    FirebaseService firebaseService;

    @PostMapping(value = "/createParking" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    public String saveParkingDetails(@RequestBody Parking parking) throws ExecutionException, InterruptedException, JsonProcessingException, JSONException, IllegalAccessException {
        return firebaseService.saveParkingDetails(parking);
    }

    @PutMapping(value = "/survey" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    public String updateSurveyData(@RequestBody Survey survey) throws ExecutionException, InterruptedException, JsonProcessingException, JSONException, IllegalAccessException {
        return firebaseService.updateSurveyData(survey);
    }

    @GetMapping("/getSuggestedParking")
    public Parking getUserDetails(@RequestHeader String userName, @RequestHeader String latitude, @RequestHeader String longitude) throws ExecutionException, InterruptedException {
        return firebaseService.getSuggestedParking(userName, Double.parseDouble(latitude), Double.parseDouble(longitude));
    }

}
