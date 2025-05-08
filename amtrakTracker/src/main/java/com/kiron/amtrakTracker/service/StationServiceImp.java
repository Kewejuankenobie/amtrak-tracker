package com.kiron.amtrakTracker.service;

import com.google.transit.realtime.GtfsRealtime;
import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import com.google.transit.realtime.GtfsRealtime.FeedMessage;
import com.kiron.amtrakTracker.model.StationTimeboard;
import com.kiron.amtrakTracker.model.TimeboardRow;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    public StationTimeboard getTrainsAtStation(String code) throws IOException {
        //First, get all train gtfs-rt
        URL urlAm = new URL("https://asm-backend.transitdocs.com/gtfs/amtrak");
        URL urlVia = new URL("https://asm-backend.transitdocs.com/gtfs/via");
        FeedMessage amFeed = FeedMessage.parseFrom(urlAm.openStream());
        FeedMessage viaFeed = FeedMessage.parseFrom(urlVia.openStream());

        StationAmtrak station = stationRepository.findById(code).orElse(null);
        if (station == null) {
            return null;
        }

        StationTimeboard timeboard = new StationTimeboard(code, station.getName(), station.getWebsite());


        List<StopTimesAmtrak> allStops = stopTimeRepository.findAllByStop_Id(code);

        for (StopTimesAmtrak stopTime : allStops) {
            TimeboardRow row = new TimeboardRow();
            row.setScheduled_arrival(parseTime(stopTime.getArrival_time()));
            row.setScheduled_departure(parseTime(stopTime.getDeparture_time()));
            row.setLate_arrival(false);
            row.setLate_departure(false);
            TripAmtrak trip = tripRepository.findById(stopTime.getTrip_id()).orElse(null);
            if (trip == null) {
                continue;
            }

            row.setNumber(trip.getNumber());
            row.setDestination(trip.getDestination());
            RouteAmtrak route = routeRepository.findById(trip.getRoute_id()).orElse(null);
            if (route == null) {
                continue;
            }
            row.setName(route.getRoute_name());
            int stopSequence = stopTime.getStop_sequence() - 1;

            //Next, check updated data, if there, then we add to the timeboard and change arrival and departure times if needed
            //We need to loop through all entities because one trip id can have multiple entities (different days)
            for (int i = 0; i < amFeed.getEntityCount(); i++) {
                FeedEntity entity = amFeed.getEntity(i);
                if (entity.hasTripUpdate()) {
                    String tripId = entity.getTripUpdate().getTrip().getTripId();
                    if (!tripId.equals(trip.getTrip_id())) {
                        continue;
                    }
                    //There are a few cases where the stop sequence of the stop time is out of range (Empire
                    // Builder from PDX at CHI for instance)
                    if (entity.getTripUpdate().getStopTimeUpdateCount() <= stopSequence) {
                        continue;
                    }


                    GtfsRealtime.TripUpdate.StopTimeUpdate update = entity.getTripUpdate().getStopTimeUpdate(stopSequence);

                    //If we are on another entity for the same trip id, then we need to add a new row
                    if (row.getDate() != null) {
                        row = new TimeboardRow(row, false);
                    }
                    
                    if (update.hasArrival()) {
                        row.setActual_time(update.getArrival().getTime());
                        row.setDate(formatDate(row.getActual_time()));
                        row.setArrival(formatEpoch(update.getArrival().getTime()));
                        if (update.getArrival().getDelay() > 0) {
                            row.setLate_arrival(true);
                        }
                    }
                    if (update.hasDeparture()) {
                        if (row.getDate() == null) {
                            row.setActual_time(update.getDeparture().getTime());
                            row.setDate(formatDate(row.getActual_time()));
                        }
                        row.setDeparture(formatEpoch(update.getDeparture().getTime()));
                        if (update.getDeparture().getDelay() > 0) {
                            row.setLate_departure(true);
                        }
                    }
                    //If there is no arrival or departure, it is most likely a rescheduled train (1xxx), so we will assume
                    //its date is today
                    if (!update.hasArrival() && !update.hasDeparture()) {
                        row.setDate(formatDate(Instant.now().getEpochSecond()));
                    }
                    timeboard.addRow(row);
                }
            }
        }

        timeboard.sortTimeboard();
        return timeboard;
    }

    @Override
    public void updateGTFS() throws IOException, CsvValidationException {
        //Updates the static GTFS database tables for Amtrak Trains

        //First, we need to get the gtfs data and get their zip entries
        URL urlAm = new URL("https://content.amtrak.com/content/gtfs/GTFS.zip");
        HttpURLConnection conn = (HttpURLConnection) urlAm.openConnection();
        conn.setRequestMethod("GET");
        InputStream inputStream = conn.getInputStream();
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ZipEntry zipEntry;

        List<StationAmtrak> stations = new ArrayList<>();
        List<StopTimesAmtrak> stopTimes = new ArrayList<>();
        List<RouteAmtrak> routes = new ArrayList<>();
        List<TripAmtrak> trips = new ArrayList<>();

        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            if (zipEntry.getName().equals("stops.txt") || zipEntry.getName().equals("stop_times.txt")
            || zipEntry.getName().equals("routes.txt") || zipEntry.getName().equals("trips.txt")) {

                //Reads the zip files input stream to byte arrays, which will later be read from to prevent
                //the inability to read all zip files
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
                        //Reading each line into an array, we add each index of line to the according object based on
                        //which csv file is being read
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

    @Override
    public Set<StationAmtrak> getStationByCode(String query) {
        List<StationAmtrak> stations = stationRepository.findByIdContainsIgnoreCase(query);
        return new HashSet<>(stations);
    }

    @Override
    public Set<StationAmtrak> getStationByName(String query) {
        List<StationAmtrak> stations = stationRepository.findByNameContainsIgnoreCase(query);
        return new HashSet<>(stations);
    }

    @Override
    public List<StationAmtrak> getAllStations() {
        return stationRepository.findAll();
    }

    private String parseTime(String time) {
        //Converts time in the total time format to a standard 12 hour format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm:ss");

        StringTokenizer st = new StringTokenizer(time, ":");
        String[] parsedTokens = new String[3];
        parsedTokens[0] = st.nextToken();
        if (Integer.parseInt(parsedTokens[0]) > 23) {
            //Handles when we have more than 23 hours
            int replacement = Integer.parseInt(parsedTokens[0]);
            while (replacement > 23) {
                replacement -= 24;
            }
            parsedTokens[0] = String.format("%02d", replacement);
        } else if (Integer.parseInt(parsedTokens[0]) < 10) {
            parsedTokens[0] = String.format("%02d", Integer.parseInt(parsedTokens[0]));
        }
        parsedTokens[1] = st.nextToken();
        parsedTokens[2] = st.nextToken();

        return formatter.format(formatter2.parse(parsedTokens[0] + ":" + parsedTokens[1] + ":" + parsedTokens[2]));
    }

    private String formatEpoch(Long epoch) {
        //Formats epoch time to the 12 hour format
        Instant instant = Instant.ofEpochSecond(epoch);
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zone).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        return formatter.format(localDateTime);
    }

    private String formatDate(Long epoch) {
        //Formats epoch time to the month/day date format
        Instant instant = Instant.ofEpochSecond(epoch);
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zone).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
        return formatter.format(localDateTime);
    }

}
