package com.kiron.amtrakTracker.repository;

import com.kiron.amtrakTracker.model.gtfs.StationAmtrak;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<StationAmtrak, String> {

    @Transactional
    List<StationAmtrak> findByIdContainsIgnoreCase(String query);

    @Transactional
    List<StationAmtrak> findByNameContainsIgnoreCase(String query);
}
