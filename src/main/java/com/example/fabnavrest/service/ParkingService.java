package com.example.fabnavrest.service;

import com.example.fabnavrest.object.Parking;
import com.google.cloud.firestore.DocumentSnapshot;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParkingService {

    public Map<String, Object> convertToMap(Parking parking) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        /*for (Field field : Parking.class.getDeclaredFields()) {
            // Skip this if you intend to access to public fields only

            if(field.getType().isArray()){
                //map.put(field.getName(), Arrays.asList(field.get(parking)));
            }else{
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                map.put(field.getName(), field.get(parking));
            }
            map.put("rateHistory",Arrays.asList(parking.getRateHistory()));
            map.put("rateTimeHistory",Arrays.asList(parking.getRateTimeHistory()));
            map.put("safetyHistory",Arrays.asList(parking.getSafetyHistory()));
            map.put("safetyTimeHistory",Arrays.asList(parking.getSafetyTimeHistory()));
            map.put("occupancyHistory",Arrays.asList(parking.getOccupancyHistory()));
            map.put("occupancyTimeHistory",Arrays.asList(parking.getOccupancyTimeHistory()));
        }*/

        map.put("id",parking.getId());
        map.put("name",parking.getName());
        map.put("latitude",parking.getLatitude());
        map.put("longitude",parking.getLongitude());
        map.put("finalRate",parking.getFinalRate());
        map.put("finalSafety",parking.getFinalSafety());
        map.put("finalOccupancy",parking.getFinalOccupancy());
        map.put("rateHistory", Arrays.asList(parking.getRateHistory()));
        map.put("safetyHistory",Arrays.asList(parking.getSafetyHistory()));
        map.put("occupancyHistory",Arrays.asList(parking.getOccupancyHistory()));

        return map;
    }

    public Parking convertToParking(DocumentSnapshot doc){
        Parking p = new Parking();
        p.setId(doc.getData().get("id").toString());
        p.setName(doc.getData().get("name").toString());
        p.setLatitude((Double) doc.getData().get("latitude"));
        p.setLongitude((Double) doc.getData().get("longitude"));
        p.setFinalRate((Long) doc.getData().get("finalRate"));
        p.setFinalSafety((Boolean) doc.getData().get("finalSafety"));
        if(doc.getData().get("finalOccupancy")!=null){
            p.setFinalOccupancy(Parking.occupancy.valueOf(doc.getData().get("finalOccupancy").toString()));
        }

        List<Long> l = (List<Long>) doc.getData().get("rateHistory");
        if(l == null){
            p.setRateHistory(new Long[0]);
        }else{
            p.setRateHistory(l.toArray(Long[]::new));
        }

        List<Boolean> safety = (List<Boolean>)doc.getData().get("safetyHistory");
        if(safety == null){
            p.setSafetyHistory(new  Boolean[0]);
        }else{
            p.setSafetyHistory(safety.toArray(Boolean[]::new));
        }


        List<String> occ = (List<String>)doc.getData().get("occupancyHistory");
        if(occ == null){
            p.setOccupancyHistory(new Parking.occupancy[0]);
        }else{
            Parking.occupancy[] arr = new Parking.occupancy[occ.size()];
            for(int i=0;i<occ.size();i++){
                arr[i] = Parking.occupancy.valueOf(occ.get(i));
            }
            p.setOccupancyHistory(arr);
        }
        return p;
    }
}
