package com.kiron.amtrakTracker.service;

import com.kiron.amtrakTracker.model.TrainParsed;

import java.util.List;

public interface TrainService {

    public TrainParsed addTrain(TrainParsed train);

    public List<TrainParsed> getAllTrains();

    List<TrainParsed> getTrainsByName(String query);

    List<TrainParsed> getTrainsByNumber(Integer i);

    void setAllInactive();

    void deleteInactiveTrains();
}
