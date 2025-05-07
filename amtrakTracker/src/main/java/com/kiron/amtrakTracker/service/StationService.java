package com.kiron.amtrakTracker.service;

import com.kiron.amtrakTracker.model.StationTimeboard;
import com.kiron.amtrakTracker.model.gtfs.StationAmtrak;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;

public interface StationService {

    public StationTimeboard getTrainsAtStation(String code) throws IOException;

    void updateGTFS() throws IOException, CsvValidationException;
}
