package com.example.fabnavrest.controller;

import com.example.fabnavrest.object.Parking;
import com.example.fabnavrest.object.User;
import com.example.fabnavrest.service.FirebaseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class ParkingController {

    @Autowired
    FirebaseService firebaseService;

    @PostMapping(value = "/createParking" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    public String saveParkingDetails(@RequestBody Parking parking) throws ExecutionException, InterruptedException, JsonProcessingException, JSONException, IllegalAccessException {
        return firebaseService.saveParkingDetails(parking);
    }
}
