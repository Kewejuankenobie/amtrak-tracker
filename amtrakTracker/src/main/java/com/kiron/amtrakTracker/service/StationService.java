package com.kiron.amtrakTracker.service;

import com.kiron.amtrakTracker.model.StationTimeboard;
import com.kiron.amtrakTracker.model.gtfs.StationAmtrak;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface StationService {

    public StationTimeboard getTrainsAtStation(String code) throws IOException;

    void updateGTFS() throws IOException, CsvValidationException;

    Set<StationAmtrak> getStationByCode(String query);

    Set<StationAmtrak> getStationByName(String query);

    List<StationAmtrak> getAllStations();
}
