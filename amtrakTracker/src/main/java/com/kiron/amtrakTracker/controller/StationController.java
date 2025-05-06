package com.kiron.amtrakTracker.controller;

import com.kiron.amtrakTracker.model.gtfs.StationAmtrak;
import com.kiron.amtrakTracker.service.StationService;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/station")
@CrossOrigin
public class StationController {

    @Autowired
    private StationService stationService;

    //Gets all trains updated stopping at station code
    @GetMapping("/get/{code}")
    public ResponseEntity<?> station(@PathVariable String code) throws IOException {
        Map<String, Object> stationResponse = new HashMap<String, Object>();

        StationAmtrak station = stationService.getTrainsAtStation(code);

        stationResponse.put("status", 1);
        stationResponse.put("data", "Temp");
        return new ResponseEntity<>(stationResponse, HttpStatus.OK);
    }

    @PostMapping(value ="/updateStation")
    public ResponseEntity<?> updateStation(
    ) throws IOException, CsvValidationException {
        Map<String, Object> stationResponse = new HashMap<String, Object>();

        stationService.updateGTFS();

        stationResponse.put("status", 1);
        return new ResponseEntity<>(stationResponse, HttpStatus.OK);
    }
}
