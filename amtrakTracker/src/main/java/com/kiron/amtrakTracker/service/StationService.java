package com.kiron.amtrakTracker.service;

import com.kiron.amtrakTracker.model.StationTimeboard;
import com.kiron.amtrakTracker.model.gtfs.Station;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface StationService {

    public StationTimeboard getTrainsAtStation(String code) throws IOException;

    void updateGTFS() throws IOException, CsvValidationException;

    Set<Station> getStationByCode(String query);

    Set<Station> getStationByName(String query);

    List<Station> getAllStations();

    void addStationAdmin(String code, double lat, double lng, String geolocKey) throws IOException;
}
