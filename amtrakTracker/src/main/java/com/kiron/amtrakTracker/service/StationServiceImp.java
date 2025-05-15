package com.kiron.amtrakTracker.service;

import com.google.transit.realtime.GtfsRealtime;
import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import com.google.transit.realtime.GtfsRealtime.FeedMessage;
import com.kiron.amtrakTracker.model.StationTimeboard;
import com.kiron.amtrakTracker.model.TimeboardRow;
import com.kiron.amtrakTracker.model.gtfs.Route;
import com.kiron.amtrakTracker.model.gtfs.Station;
import com.kiron.amtrakTracker.model.gtfs.StopTimes;
import com.kiron.amtrakTracker.model.gtfs.Trip;
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
import java.time.*;
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

        Station station = stationRepository.findByCode(code);
        if (station == null) {
            return null;
        }

        StationTimeboard timeboard = new StationTimeboard(code, station.getName(), station.getWebsite());


        List<StopTimes> allStops = stopTimeRepository.findAllByStop_Id(station.getId());

        for (StopTimes stopTime : allStops) {

            if (code.length() == 3) {
                buildRow(amFeed, stopTime, timeboard);
            } else {
                buildRow(viaFeed, stopTime, timeboard);
            }
        }

        timeboard.sortTimeboard();
        return timeboard;
    }

    private void buildRow(FeedMessage feed, StopTimes stopTime, StationTimeboard timeboard) {

        String timeZone =stationRepository.findById(stopTime.getStop_id()).isPresent() ?
                stationRepository.findById(stopTime.getStop_id()).get().getTime_zone() : "America/New_York";

        ZonedDateTime t1 = ZonedDateTime.now(ZoneId.of(timeZone));
        ZonedDateTime t2 = ZonedDateTime.now(ZoneId.of("America/New_York"));

        int hourOffset = t1.getHour() - t2.getHour();

        TimeboardRow row = new TimeboardRow();
        row.setScheduled_arrival(parseTime(stopTime.getArrival_time(), hourOffset));
        row.setScheduled_departure(parseTime(stopTime.getDeparture_time(), hourOffset));
        row.setLate_arrival(false);
        row.setLate_departure(false);
        Trip trip = tripRepository.findById(stopTime.getTrip_id()).orElse(null);
        if (trip == null) {
            return;
        }

        row.setNumber(trip.getNumber());
        row.setDestination(trip.getDestination());
        Route route = routeRepository.findById(trip.getRoute_id()).orElse(null);
        if (route == null) {
            return;
        }
        row.setName(route.getRoute_name());
        int stopSequence = stopTime.getStop_sequence() - 1;
        if (trip.getRoute_id().equals("SJ2")) {
            ++stopSequence;
        }

        //Next, check updated data, if there, then we add to the timeboard and change arrival and departure times if needed
        //We need to loop through all entities because one trip id can have multiple entities (different days)
        for (int i = 0; i < feed.getEntityCount(); i++) {
            FeedEntity entity = feed.getEntity(i);
            if (entity.hasTripUpdate()) {
                String tripId = entity.getTripUpdate().getTrip().getTripId();
                if (!tripId.equals(trip.getTrip_id()) && !tripId.contains("_AMTK_" + trip.getTrip_id())) {
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
                    row.setArrival(formatEpoch(update.getArrival().getTime(), timeZone));
                    if (update.getArrival().getDelay() > 0) {
                        row.setLate_arrival(true);
                    }
                }
                if (update.hasDeparture()) {
                    if (row.getDate() == null) {
                        row.setActual_time(update.getDeparture().getTime());
                        row.setDate(formatDate(row.getActual_time()));
                    }
                    row.setDeparture(formatEpoch(update.getDeparture().getTime(), timeZone));
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

    @Override
    public void updateGTFS() throws IOException, CsvValidationException {
        //Updates the static GTFS database tables for Amtrak Trains

        //First, we need to get the gtfs data and get their zip entries
        URL urlAm = new URL("https://content.amtrak.com/content/gtfs/GTFS.zip");
        URL urlVia = new URL("https://www.viarail.ca/sites/all/files/gtfs/viarail.zip");
        URL urlSanJ = new URL("https://d34tiw64n5z4oh.cloudfront.net/wp-content/uploads/SJJPA_03182025-1.zip");

        List<Station> stations = new ArrayList<>();
        List<StopTimes> stopTimes = new ArrayList<>();
        List<Route> routes = new ArrayList<>();
        List<Trip> trips = new ArrayList<>();

        updateGTFSFromCSV(urlAm, stations, stopTimes, routes, trips, 0);
        updateGTFSFromCSV(urlVia, stations, stopTimes, routes, trips, 1);
        updateGTFSFromCSV(urlSanJ, stations, stopTimes, routes, trips, 2);

        stationRepository.saveAll(stations);
        stopTimeRepository.saveAll(stopTimes);
        routeRepository.saveAll(routes);
        tripRepository.saveAll(trips);
    }

    private void updateGTFSFromCSV(URL url, List<Station> stations, List<StopTimes> stopTimes,
                                   List<Route> routes, List<Trip> trips,
                                   int type) throws IOException, CsvValidationException {

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        InputStream inputStream = conn.getInputStream();
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ZipEntry zipEntry;

        Long stopTimeSize = 0L;
        if (!stopTimes.isEmpty()) {
            stopTimeSize = stopTimes.getLast().getId();
        }
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

                        if (type == 0) {
                            updateAmtrakGTFS(zipEntry.getName(), line, stations, stopTimes, routes, trips, lineNum, stopTimeSize);

                        } else if (type == 1) {
                            updateViaGTFS(zipEntry.getName(), line, stations, stopTimes, routes, trips, lineNum, stopTimeSize);
                        } else {
                            updateSanJGTFS(zipEntry.getName(), line, stations, stopTimes, routes, trips, lineNum, stopTimeSize);
                        }
                        lineNum++;
                    }
                }
                zipInputStream.closeEntry();
            } else {
                zipInputStream.closeEntry();
            }
        }
    }

    private void updateSanJGTFS(String name, String[] line, List<Station> stations, List<StopTimes> stopTimes,
                                List<Route> routes, List<Trip> trips, Long lineNum, Long stopTimeSize) {
        if (name.equals("stops.txt") && line[0].length() == 3 && !line[7].contains("acerail")) {
            Station station = new Station();
            station.setId(line[0]);
            station.setCode(line[0]);
            station.setName(line[2]);
            station.setWebsite(line[7]);
            station.setTime_zone("America/Los_Angeles");
            stations.add(station);
        } else if (name.equals("stop_times.txt") && line[0].length() == 3) {
            StopTimes stopTime = new StopTimes();
            stopTime.setId(lineNum + stopTimeSize);
            stopTime.setTrip_id(line[0]);
            stopTime.setArrival_time(line[3]);
            stopTime.setDeparture_time(line[4]);
            stopTime.setStop_id(line[2]);
            stopTime.setStop_sequence(Integer.parseInt(line[1]));
            stopTimes.add(stopTime);
        } else if (name.equals("routes.txt") && line[0].equals("SJ2")) {
            Route route = new Route();
            route.setRoute_id(line[0]);
            route.setRoute_name("San Joaquins");
            routes.add(route);
        } else if (name.equals("trips.txt") && line[0].length() == 3) {
            Trip trip = new Trip();
            trip.setTrip_id(line[0]);
            trip.setRoute_id(line[1]);
            trip.setNumber(Integer.parseInt(line[0]));
            trip.setDestination(line[3]);
            trips.add(trip);
        }
    }

    private void updateAmtrakGTFS(String name, String[] line, List<Station> stations, List<StopTimes> stopTimes,
                                  List<Route> routes, List<Trip> trips, Long lineNum, Long stopTimeSize) {
        if (name.equals("stops.txt")) {
            Station station = new Station();
            station.setId(line[0]);
            station.setCode(line[0]);
            station.setName(line[1]);
            station.setWebsite(line[2]);
            station.setTime_zone(line[3]);
            stations.add(station);
        } else if (name.equals("stop_times.txt")) {
            StopTimes stopTime = new StopTimes();
            stopTime.setId(lineNum + stopTimeSize);
            stopTime.setTrip_id(line[0]);
            stopTime.setArrival_time(line[1]);
            stopTime.setDeparture_time(line[2]);
            stopTime.setStop_id(line[3]);
            stopTime.setStop_sequence(Integer.parseInt(line[4]));
            stopTimes.add(stopTime);
        } else if (name.equals("routes.txt")) {
            Route route = new Route();
            route.setRoute_id(line[0]);
            route.setRoute_name(line[3]);
            routes.add(route);
        } else if (name.equals("trips.txt")) {
            Trip trip = new Trip();
            trip.setTrip_id(line[2]);
            trip.setRoute_id(line[0]);
            trip.setNumber(Integer.parseInt(line[3]));
            trip.setDestination(line[6]);
            trips.add(trip);
        }
    }

    private void updateViaGTFS(String name, String[] line, List<Station> stations,
                               List<StopTimes> stopTimes, List<Route> routes, List<Trip> trips, Long lineNum,
                               Long stopTimeSize) {
        if (name.equals("stops.txt")) {
            Station station = new Station();
            station.setId(line[0]);
            station.setCode(line[1]);
            station.setName(line[2]);
            station.setTime_zone(line[6]);
            stations.add(station);
        } else if (name.equals("stop_times.txt")) {
            StopTimes stopTime = new StopTimes();
            stopTime.setId(lineNum + stopTimeSize);
            stopTime.setTrip_id(line[0]);
            stopTime.setArrival_time(line[1]);
            stopTime.setDeparture_time(line[2]);
            stopTime.setStop_id(line[3]);
            stopTime.setStop_sequence(Integer.parseInt(line[4]));
            stopTimes.add(stopTime);
        } else if (name.equals("routes.txt")) {
            Route route = new Route();
            route.setRoute_id(line[0]);
            route.setRoute_name(getViaRouteName(line[2]));
            routes.add(route);
        } else if (name.equals("trips.txt")) {
            Trip trip = new Trip();
            trip.setTrip_id(line[2]);
            trip.setRoute_id(line[0]);
            if (line[4].isEmpty()) {
                trip.setNumber(0);
            } else if (line[4].contains("-")) {
                StringTokenizer st = new StringTokenizer(line[4], "-");
                trip.setNumber(Integer.parseInt(st.nextToken()));
            } else {
                trip.setNumber(Integer.parseInt(line[4]));
                //maple leaf case
            }
            trip.setDestination(line[5]);
            trips.add(trip);
        }
    }

    private String getViaRouteName(String defaultRoute) {
        if (defaultRoute.equals("Vancouver - Toronto")) {
            return "Canadian";
        } else if (defaultRoute.equals("Montréal - Halifax")) {
            return "Ocean";
        } else if (defaultRoute.equals("Toronto - New York")) {
            return "Maple Leaf";
        } else if (defaultRoute.equals("Sudbury - White River")) {
            return "Lake Superior";
        } else if (defaultRoute.equals("Jasper - Prince Rupert")) {
            return "Skeena";
        } else if (defaultRoute.equals("Winnipeg - Churchill") || defaultRoute.equals("The Pas - Churchill")) {
            return "Hudson Bay";
        } else if (defaultRoute.equals("Montréal - Senneterre")) {
            return "Abitibi";
        } else if (defaultRoute.equals("Montréal - Jonquière")) {
            return "Saguenay";
        } else {
            return "Corridor: " + defaultRoute;
        }
    }

    @Override
    public Set<Station> getStationByCode(String query) {
        List<Station> stations = stationRepository.findByCodeContainsIgnoreCase(query);
        return new HashSet<>(stations);
    }

    @Override
    public Set<Station> getStationByName(String query) {
        Set<Station> stations = new HashSet<>(stationRepository.findByNameContainsIgnoreCase(query));
        String nextQuery = query.replace("e", "é");
        stations.addAll(stationRepository.findByNameContainsIgnoreCase(nextQuery));
        return new HashSet<>(stations);
    }

    @Override
    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }

    private String parseTime(String time, int offset) {
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
            replacement += offset;
            if (replacement < 0) {
                replacement += 24;
            }
            parsedTokens[0] = String.format("%02d", replacement);
        } else {
            int replacement = Integer.parseInt(parsedTokens[0]) + offset;
            if (replacement < 0) {
                replacement += 24;
            }
            parsedTokens[0] = String.format("%02d", replacement);
        }
        parsedTokens[1] = st.nextToken();
        parsedTokens[2] = st.nextToken();

        return formatter.format(formatter2.parse(parsedTokens[0] + ":" + parsedTokens[1] + ":" + parsedTokens[2]));
    }

    private String formatEpoch(Long epoch, String timeZone) {
        //Formats epoch time to the 12 hour format
        Instant instant = Instant.ofEpochSecond(epoch);
        ZoneId zone = ZoneId.of(timeZone);
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
