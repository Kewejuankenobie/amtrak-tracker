package com.kiron.amtrakTracker.controller;

import com.kiron.amtrakTracker.model.StationTimeboard;
import com.kiron.amtrakTracker.model.gtfs.Station;
import com.kiron.amtrakTracker.service.StationService;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

        StationTimeboard timeboard = stationService.getTrainsAtStation(code);

        stationResponse.put("status", 1);
        stationResponse.put("data", timeboard);
        return new ResponseEntity<>(stationResponse, HttpStatus.OK);
    }

    @GetMapping("/search/{query}")
    public ResponseEntity<?> search(@PathVariable String query) {
        Map<String, Object> stationResponse = new HashMap<String, Object>();

        Set<Station> codeResults = stationService.getStationByCode(query);
        Set<Station> nameResults = stationService.getStationByName(query);

        codeResults.addAll(nameResults);

        stationResponse.put("status", 1);
        stationResponse.put("data", codeResults);
        return new ResponseEntity<>(stationResponse, HttpStatus.OK);
    }

    @GetMapping("/getAllStations")
    public ResponseEntity<?> getAllStations() {
        Map<String, Object> stationResponse = new HashMap<String, Object>();

        List<Station> stations = stationService.getAllStations();

        stationResponse.put("status", 1);
        stationResponse.put("data", stations);
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
