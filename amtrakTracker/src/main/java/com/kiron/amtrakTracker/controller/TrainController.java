package com.kiron.amtrakTracker.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiron.amtrakTracker.model.TrainApiModel;
import com.kiron.amtrakTracker.model.TrainParsed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/train")
@CrossOrigin
public class TrainController {

    /*
     * This controller will call from the API, so as there is no database control (yet), no need for a service
     */

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllTrains() throws IOException, URISyntaxException {
        Map<String, Object> trainResponse = new HashMap<String, Object>();
        URL trainUrl = new URI("https://asm-backend.transitdocs.com/map").toURL();
        ObjectMapper mapper = new ObjectMapper();
        TrainApiModel[] trains = mapper.readValue(trainUrl, TrainApiModel[].class);
        //These train objects need cleaning up before sending to user, perhaps a second train object
        List<TrainParsed> parsedTrains = new ArrayList<TrainParsed>();
        for (TrainApiModel train : trains) {
            TrainParsed parsedTrain = new TrainParsed(train);
            parsedTrains.add(parsedTrain);
        }
        trainResponse.put("status", 1);
        trainResponse.put("data", parsedTrains);
        return new ResponseEntity<>(trainResponse, HttpStatus.OK);
    }
}
