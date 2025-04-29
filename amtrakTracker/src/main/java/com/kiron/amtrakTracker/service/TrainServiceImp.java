package com.kiron.amtrakTracker.service;

import com.kiron.amtrakTracker.model.TrainParsed;
import com.kiron.amtrakTracker.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainServiceImp implements TrainService {

    @Autowired
    private TrainRepository trainRepository;

    @Override
    public TrainParsed addTrain(TrainParsed train) {
        return trainRepository.save(train);
    }

    @Override
    public List<TrainParsed> getAllTrains() {
        return trainRepository.findAll();
    }

    @Override
    public List<TrainParsed> getTrainsByName(String query) {
        return trainRepository.findByNameContainsIgnoreCase(query);
    }

    @Override
    public List<TrainParsed> getTrainsByNumber(Integer i) {
        return trainRepository.findByNumber(i);
    }
}
