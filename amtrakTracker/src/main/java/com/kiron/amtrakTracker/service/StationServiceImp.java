package com.kiron.amtrakTracker.service;

import com.google.transit.realtime.GtfsRealtime;
import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import com.google.transit.realtime.GtfsRealtime.FeedMessage;
import com.kiron.amtrakTracker.model.gtfs.RouteAmtrak;
import com.kiron.amtrakTracker.model.gtfs.StationAmtrak;
import com.kiron.amtrakTracker.model.gtfs.StopTimesAmtrak;
import com.kiron.amtrakTracker.model.gtfs.TripAmtrak;
import com.kiron.amtrakTracker.repository.RouteRepository;
import com.kiron.amtrakTracker.repository.StationRepository;
import com.kiron.amtrakTracker.repository.StopTimeRepository;
import com.kiron.amtrakTracker.repository.TripRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class StationServiceImp implements StationService {


    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private StopTimeRepository stopTimeRepository;
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private TripRepository tripRepository;

    @Override
    public StationAmtrak getTrainsAtStation(String code) throws IOException {
        //First, get all train gtfs-rt
        URL urlAm = new URL("https://asm-backend.transitdocs.com/gtfs/amtrak");
        URL urlVia = new URL("https://asm-backend.transitdocs.com/gtfs/via");
        FeedMessage amFeed = FeedMessage.parseFrom(urlAm.openStream());
        FeedMessage viaFeed = FeedMessage.parseFrom(urlVia.openStream());

        for (FeedEntity entity : amFeed.getEntityList()) {
            String tripId = entity.getVehicle().getTrip().getTripId();
            String trainId = entity.getVehicle().getVehicle().getId();
            if (entity.hasTripUpdate()) {
                long i = entity.getTripUpdate().getStopTimeUpdate(1).getArrival().getTime();

                for (GtfsRealtime.TripUpdate.StopTimeUpdate stopTimeUpdate : entity.getTripUpdate().getStopTimeUpdateList()) {
                    if (stopTimeUpdate.getStopId().equals(code)) {

                    }
                }
            }
        }

        //We need static GTFS Data stored in a database to match the route numbers to train
        // names and trip id to train number (short train name)
        return null;
    }

    @Override
    public void updateGTFS() throws IOException, CsvValidationException {

        URL urlAm = new URL("https://content.amtrak.com/content/gtfs/GTFS.zip");
        HttpURLConnection conn = (HttpURLConnection) urlAm.openConnection();
        conn.setRequestMethod("GET");
        InputStream inputStream = conn.getInputStream();
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ZipEntry zipEntry;

        List<String> fname = new ArrayList<String>();
        List<StationAmtrak> stations = new ArrayList<>();
        List<StopTimesAmtrak> stopTimes = new ArrayList<>();
        List<RouteAmtrak> routes = new ArrayList<>();
        List<TripAmtrak> trips = new ArrayList<>();

        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            if (zipEntry.getName().equals("stops.txt") || zipEntry.getName().equals("stop_times.txt")
            || zipEntry.getName().equals("routes.txt") || zipEntry.getName().equals("trips.txt")) {
                //Also need stop_times, routes, and trips

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int length;
                while ((length = zipInputStream.read(buffer)) > 0) {
                    baos.write(buffer, 0, length);
                }


                try(InputStream stream = new ByteArrayInputStream(baos.toByteArray());
                    InputStreamReader isr = new InputStreamReader(stream, StandardCharsets.UTF_8);
                    CSVReader csvReader = new CSVReader(isr);) {

                    String[] line;
                    boolean firstLine = true;
                    Long lineNum = 0L;
                    while ((line = csvReader.readNext()) != null) {
                        if (firstLine) {
                            firstLine = false;
                            continue;
                        }

                        if (zipEntry.getName().equals("stops.txt")) {
                            StationAmtrak station = new StationAmtrak();
                            station.setId(line[0]);
                            station.setName(line[1]);
                            station.setWebsite(line[2]);
                            stations.add(station);
                        } else if (zipEntry.getName().equals("stop_times.txt")) {
                            StopTimesAmtrak stopTime = new StopTimesAmtrak();
                            stopTime.setId(lineNum);
                            stopTime.setTrip_id(line[0]);
                            stopTime.setArrival_time(line[1]);
                            stopTime.setDeparture_time(line[2]);
                            stopTime.setStop_id(line[3]);
                            stopTime.setStop_sequence(Integer.parseInt(line[4]));
                            stopTimes.add(stopTime);
                        } else if (zipEntry.getName().equals("routes.txt")) {
                            RouteAmtrak route = new RouteAmtrak();
                            route.setRoute_id(line[0]);
                            route.setRoute_name(line[3]);
                            routes.add(route);
                        } else if (zipEntry.getName().equals("trips.txt")) {
                            TripAmtrak trip = new TripAmtrak();
                            trip.setTrip_id(line[2]);
                            trip.setRoute_id(line[0]);
                            trip.setNumber(Integer.parseInt(line[3]));
                            trip.setDestination(line[6]);
                            trips.add(trip);
                        }
                        lineNum++;
                    }
                }
                zipInputStream.closeEntry();
            } else {
                zipInputStream.closeEntry();
            }
        }
        stationRepository.saveAll(stations);
        stopTimeRepository.saveAll(stopTimes);
        routeRepository.saveAll(routes);
        tripRepository.saveAll(trips);
    }

}
