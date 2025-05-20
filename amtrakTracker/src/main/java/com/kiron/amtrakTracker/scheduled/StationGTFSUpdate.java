package com.kiron.amtrakTracker.scheduled;

import com.kiron.amtrakTracker.service.StationService;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class StationGTFSUpdate {

    @Autowired
    private StationService stationService;

    @Scheduled(fixedDelay = 7, timeUnit = TimeUnit.DAYS)
    public void updateStation() {

        try {
            stationService.updateGTFS();
            log.info("Updated Station GTFS");
        } catch (IOException | CsvValidationException e) {
            log.error("Error updating station GTFS due to error:", e);
        }
    }
}
