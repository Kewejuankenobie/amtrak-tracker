package com.kiron.amtrakTracker.scheduled;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiron.amtrakTracker.model.TrainApiModel;
import com.kiron.amtrakTracker.model.TrainParsed;
import com.kiron.amtrakTracker.service.TrainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Component
@Slf4j
public class TrainAPIUpdate {

    @Autowired
    private TrainService trainService;

    @Scheduled(fixedRate = 120000)
    public void updateTrains() throws IOException, URISyntaxException {
        URL trainUrl = new URI("https://asm-backend.transitdocs.com/map").toURL();
        ObjectMapper mapper = new ObjectMapper();
        TrainApiModel[] trains = mapper.readValue(trainUrl, TrainApiModel[].class);
        //These train objects need cleaning up before sending to user, perhaps a second train object

        trainService.setAllInactive();

        for (TrainApiModel train : trains) {
            TrainParsed parsedTrain = trainService.addTrain(new TrainParsed(train));
        }

        trainService.deleteInactiveTrains();
        log.info("Updated Trains, there are " + trains.length + " trains");
    }

}
