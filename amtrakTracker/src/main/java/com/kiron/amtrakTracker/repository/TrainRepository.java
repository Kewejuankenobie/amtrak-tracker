package com.kiron.amtrakTracker.repository;

import com.kiron.amtrakTracker.model.TrainParsed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TrainRepository extends JpaRepository<TrainParsed, String> {
    @Transactional
    List<TrainParsed> findByNameContainsIgnoreCase(String name);
    @Transactional
    List<TrainParsed> findByNumber(Integer number);
}
