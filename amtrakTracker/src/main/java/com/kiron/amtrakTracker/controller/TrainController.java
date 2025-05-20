package com.kiron.amtrakTracker.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.transit.realtime.GtfsRealtime;
import com.kiron.amtrakTracker.model.TrainApiModel;
import com.kiron.amtrakTracker.model.TrainParsed;
import com.kiron.amtrakTracker.model.gtfs.Route;
import com.kiron.amtrakTracker.model.gtfs.Station;
import com.kiron.amtrakTracker.model.gtfs.Trip;
import com.kiron.amtrakTracker.repository.RouteRepository;
import com.kiron.amtrakTracker.repository.TripRepository;
import com.kiron.amtrakTracker.service.StationService;
import com.kiron.amtrakTracker.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.lang.Integer.parseInt;

@RestController
@RequestMapping("/train")
@CrossOrigin
public class TrainController {

    /*
     * This controller will call from the API, so as there is no database control (yet), no need for a service
     */

    @Autowired
    private TrainService trainService;

    @Autowired
    private StationService StationService;
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private TripRepository tripRepository;

    @PostMapping("/update")
    public ResponseEntity<?> updateAllTrains() throws IOException, URISyntaxException {
        Map<String, Object> trainResponse = new HashMap<String, Object>();
        URL trainUrl = new URI("https://asm-backend.transitdocs.com/map").toURL();
        ObjectMapper mapper = new ObjectMapper();
        TrainApiModel[] trains = mapper.readValue(trainUrl, TrainApiModel[].class);
        //These train objects need cleaning up before sending to user, perhaps a second train object

        trainService.setAllInactive();

        List<TrainParsed> parsedTrains = new ArrayList<TrainParsed>();
        for (TrainApiModel train : trains) {
            TrainParsed parsedTrain = new TrainParsed(train);

            Station station;
            try {
                station = StationService.getStationByCode(parsedTrain.getNext_station()).iterator().next();
            } catch (NoSuchElementException e) {
                System.out.println("Station not found for " + parsedTrain.getNext_station());
                parsedTrains.add(parsedTrain);
                continue;
            }


            //Set the correct time for arrival
            Instant instant = Instant.ofEpochSecond(parsedTrain.getArrival_epoch());
            ZoneId zone = ZoneId.of(station.getTime_zone());
            LocalDateTime localDateTime = instant.atZone(zone).toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
            parsedTrain.setScheduled_arrival(formatter.format(localDateTime));

            parsedTrains.add(parsedTrain);
            trainService.addTrain(parsedTrain);
        }

        trainService.deleteInactiveTrains();

        trainResponse.put("status", 1);
        trainResponse.put("data", parsedTrains);
        return new ResponseEntity<>(trainResponse, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllTrains() {
        Map<String, Object> trainResponse = new HashMap<String, Object>();

        List<TrainParsed> parsedTrains = trainService.getAllTrains();
        parsedTrains.sort(Comparator.comparing(TrainParsed::getNumber));
        trainResponse.put("status", 1);
        trainResponse.put("data", parsedTrains);
        return new ResponseEntity<>(trainResponse, HttpStatus.OK);
    }

    @GetMapping("/search/{query}")
    public ResponseEntity<?> search(@PathVariable String query) {
        Map<String, Object> trainResponse = new HashMap<String, Object>();

        List<TrainParsed> parsedTrains = trainService.getTrainsByName(query);
        List<TrainParsed> numberResults;
        try {
            numberResults = trainService.getTrainsByNumber(parseInt(query));
        } catch (NumberFormatException e) {
            numberResults = Collections.emptyList();
        }

        //Handle search of railroad when nothing else pops up
        if (parsedTrains.isEmpty() && numberResults.isEmpty()) {
            parsedTrains = trainService.getTrainsByRailroad(query);
        }

        parsedTrains.addAll(numberResults);
        parsedTrains.sort(Comparator.comparing(TrainParsed::getNumber));
        trainResponse.put("status", 1);
        trainResponse.put("data", parsedTrains);
        return new ResponseEntity<>(trainResponse, HttpStatus.OK);
    }


    /*
    This is here in case needed later, but not used currently
     */
    @GetMapping("/closest/{latitude}/{longitude}")
    public ResponseEntity<?> closest(@PathVariable double latitude, @PathVariable double longitude) {
        Map<String, Object> trainResponse = new HashMap<String, Object>();

        List<TrainParsed> allTrains = trainService.getAllTrains();
        Map<Double, TrainParsed> distanceMap = new HashMap<Double, TrainParsed>();
        List<Double> distanceList = new ArrayList<>();

        for (TrainParsed train : allTrains) {
            if (train.getLatitude() == null || train.getLongitude() == null) {
                continue;
            }
            Double distance = Math.sqrt((train.getLatitude() - latitude) * (train.getLatitude() - latitude) +
                    (train.getLongitude() - longitude) * (train.getLongitude() - longitude));

            distanceMap.put(distance, train);
            distanceList.add(distance);
        }

        distanceList.sort(Comparator.comparing(Double::valueOf));

        List<TrainParsed> closestTrains = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            closestTrains.add(distanceMap.get(distanceList.get(i)));
        }

        trainResponse.put("status", 1);
        trainResponse.put("data", closestTrains);
        return new ResponseEntity<>(trainResponse, HttpStatus.OK);
    }
}
