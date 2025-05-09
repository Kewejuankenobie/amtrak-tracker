package com.kiron.amtrakTracker.repository;

import com.kiron.amtrakTracker.model.gtfs.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, String> {

    @Transactional
    List<Station> findByCodeContainsIgnoreCase(String query);

    @Transactional
    List<Station> findByNameContainsIgnoreCase(String query);

    @Transactional
    Station findByCode(String code);
}
